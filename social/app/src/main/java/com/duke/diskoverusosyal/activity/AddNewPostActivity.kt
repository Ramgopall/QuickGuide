package com.duke.diskoverusosyal.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import com.duke.diskoverusosyal.ProgressRequestBody
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.converter.VideoCompress
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.trimmer.utils.FileUtils
import com.duke.diskoverusosyal.utils.Util
import com.duke.diskoverusosyal.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_new_post.*
import kotlinx.android.synthetic.main.dialog_choose_camera_or_galler.*
import kotlinx.android.synthetic.main.dialog_choose_photo_or_video.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddNewPostActivity : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {

    var isVideoUploadable: Boolean? = false
    var isIntented: Boolean? = false
    var photoPath: String? = ""
    var videoPath: String? = ""

    private val REQUEST_VIDEO_TRIMMER = 0x01
    var type: String? = ""
    var desc: String? = ""


    private var startTime: Long = 0
    private var endTime: Long = 0

    companion object {
        val EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_post)
        if (!Utils.isUserLogin()!!){
            startActivity(
                Intent(this@AddNewPostActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        ivAddPostBack.setOnClickListener { onBackPressed() }
        ivAddPostSelect.setOnClickListener { showActionDialog() }
        checkStatus()
        btnAddPostSubmit.setOnClickListener {
            desc = etAddPostDesc.text.toString().trim()
            if (videoPath=="" && photoPath == ""){
                Utils.showToast(this,"Firstly you have to select a video or photo.")
                return@setOnClickListener
            }
            if (videoPath!=""){
                type = "2"
                val file = File(videoPath!!)
                val file_size = file.length()/1024
                if (file_size<=5120){
                    hitData(videoPath)
                }
                else{
                    compressVideo(videoPath)
                }

            }else{
                type = "1"
                hitData(photoPath)
            }
        }
    }

    private fun checkStatus() {
        Log.d("aaaaaaaaaaaaa",Utils.getUserSess()!!)
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().isVideoUploadable(Utils.getUserSess())
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    isVideoUploadable = data?.status!!
                    if (intent?.extras !=null){
                        val uri = intent?.getParcelableExtra(Intent.EXTRA_STREAM) as Uri
                        val mime = MimeTypeMap.getSingleton()
                        val type = mime.getExtensionFromMimeType(contentResolver.getType(uri))
                        if (type == "mp4"){
                            isIntented = true
                            val intent = Intent(this@AddNewPostActivity, TrimmerActivity::class.java)
                            intent.putExtra(
                                EXTRA_VIDEO_PATH,
                                FileUtils.getPath(this@AddNewPostActivity, uri)
                            )
                            startActivityForResult(intent, 11)
                        }
                        if (type == "jpg" || type == "gif" || type == "png" || type == "bmp" || type == "webp" || type == "jpeg"){
                            isIntented = true
                            photoPath = Utils.getImagePath(uri,this@AddNewPostActivity)
                            Utils.showCenterCropImage(this@AddNewPostActivity, photoPath, ivAddPostSelect)
                        }

                    }
                } else {
                    Utils.showToast(
                        this@AddNewPostActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@AddNewPostActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    private fun showActionDialog() {
        photoPath = ""
        videoPath = ""

        val dialog = AlertDialog.Builder(this)
        dialog.setView(R.layout.dialog_choose_photo_or_video)
        val alertDialog = dialog.create()
        alertDialog.show()

        alertDialog.tvDialogChoosePhoto.setOnClickListener {
            alertDialog.dismiss()
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
        }
        alertDialog.tvDialogChooseVideo.setOnClickListener {
            if (isVideoUploadable!!) {
                showVideoSelectDialog()
                alertDialog.dismiss()
            } else {
                startActivity(
                    Intent(this@AddNewPostActivity, PackageActivity::class.java)
                        .putExtra("checkLimitReached",true)
                )
                finish()
            }
        }
    }

    private fun showVideoSelectDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setView(R.layout.dialog_choose_camera_or_galler)
        val alertDialog = dialog.create()
        alertDialog.show()

        alertDialog.tvDialogChooseCamera.setOnClickListener {
            alertDialog.dismiss()
            val videoCapture = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(videoCapture, REQUEST_VIDEO_TRIMMER)
        }
        alertDialog.tvDialogChooseGallery.setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent()
            intent.setTypeAndNormalize("video/*")
            intent.action = Intent.ACTION_GET_CONTENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Video"
                ), REQUEST_VIDEO_TRIMMER
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                photoPath = result.uri.path!!
                Utils.showCenterCropImage(this@AddNewPostActivity, photoPath, ivAddPostSelect)
            }
        }
        if (requestCode == REQUEST_VIDEO_TRIMMER) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedUri = data!!.data
                if (selectedUri != null) {
                    //Log.d("eeeeeeeee",selectedUri.toString())
                    //Log.d("eeeeeeeee",Utils.getVideoPaths(data,this@AddNewPostActivity)!!)

                    val intent = Intent(this@AddNewPostActivity, TrimmerActivity::class.java)
                    intent.putExtra(
                        EXTRA_VIDEO_PATH,
                        FileUtils.getPath(this@AddNewPostActivity, selectedUri)
                    )
                    startActivityForResult(intent, 11)
                } else {
                    Utils.showToast(this@AddNewPostActivity, "Cannot retrieve selected video")
                }
            }
        }
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                videoPath = data?.getStringExtra("path")!!
                Log.d("asasassasa",videoPath!!)
                Utils.showCenterCropImage(
                    this,
                    Utils.createVideoThumbNail(videoPath!!),
                    ivAddPostSelect
                )
            }
        }
    }

    private fun compressVideo(path: String?){
        val destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + "VID_" + SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()!!).format(Date()) + ".mp4"
        VideoCompress.compressVideoLow(
            path,
            destPath,
            object : VideoCompress.CompressListener {
                override fun onStart() {
                    Utils.showUploadingDialog(this@AddNewPostActivity,"Preparing")
                    startTime = System.currentTimeMillis()
                    Util.writeFile(
                        this@AddNewPostActivity,
                        "Start at: " + SimpleDateFormat("HH:mm:ss", getLocale()!!).format(
                            Date()
                        ) + "\n"
                    )
                }

                override fun onSuccess() {
                    Utils.finishUploadingLoading()
                    endTime = System.currentTimeMillis()
                    Util.writeFile(
                        this@AddNewPostActivity,
                        "End at: " + SimpleDateFormat("HH:mm:ss", getLocale()!!).format(
                            Date()
                        ) + "\n"
                    )
                    Util.writeFile(
                        this@AddNewPostActivity,
                        "Total: " + ((endTime - startTime) / 1000) + "s" + "\n"
                    )
                    Util.writeFile(this@AddNewPostActivity)
                    hitData(destPath)
                }

                override fun onFail() {
                    //endTime = System.currentTimeMillis()
                    Utils.finishUploadingLoading()
                    Util.writeFile(
                        this@AddNewPostActivity,
                        "Failed Compress!!!" + SimpleDateFormat(
                            "HH:mm:ss",
                            getLocale()!!
                        ).format(
                            Date()
                        )
                    )
                    Utils.showToast(this@AddNewPostActivity,"Failed to Prepare!")
                }

                override fun onProgress(percent: Float) {
                    Utils.changeUploadPercentage(percent.toInt())
                }
            })
    }

    private fun hitData(path: String?) {
        val requestBodySess = RequestBody.create(MediaType.parse("text/plain"), Utils.getUserSess()!!)
        val requestBodyDesc = RequestBody.create(MediaType.parse("text/plain"), desc!!)
        val requestBodyType = RequestBody.create(MediaType.parse("text/plain"), type!!)

        val fileBody = ProgressRequestBody(File(path!!), "*", this)
        val fileName = if (type=="1"){
            "image${path.substring(path.lastIndexOf("."))}"
        } else{
            "video${path.substring(path.lastIndexOf("."))}"
        }
        val multipartPhoto1 = MultipartBody.Part.createFormData("file",fileName,fileBody)


        Utils.showUploadingDialog(this,"Uploading")
        val call = ApiClient().getClient().uploadPost(requestBodySess,requestBodyType,requestBodyDesc,multipartPhoto1)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(call: Call<CommonResponseModel>?, response: Response<CommonResponseModel>?) {
                Utils.finishUploadingLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val status = response.body()?.status!!
                    val message = response.body()?.message
                    if (status) {
//                        val dialod = AlertDialog.Builder(this@AddNewPostActivity)
//                        dialod.setTitle("Successfully Uploaded")
//                        dialod.setMessage("Your post will be reviewed and it will become live within 48 hours.")
//                        dialod.setCancelable(false)
//                        dialod.setNegativeButton("Okay"){ _,_ ->
//                            finish()
//                        }
//                        dialod.create().show()
                        Utils.showToast(this@AddNewPostActivity,"Successfully Uploaded")
                        //if (isIntented!!){
                            startActivity(
                                Intent(this@AddNewPostActivity, HomeActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
//                        }
//                        else{
//                            finish()
//                        }
                    } else {
                        Utils.showToast(this@AddNewPostActivity,message)
                    }
                } else {
                    Utils.showToast(this@AddNewPostActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.finishUploadingLoading()
                Utils.showToast(this@AddNewPostActivity,resources.getString(R.string.api_failure))
            }
        })

    }

    override fun onProgressUpdate(percentage: Int) {
        Utils.changeUploadPercentage(percentage)
    }

    override fun onError() {

    }

    override fun onFinish() {
        Utils.finishUploadingLoading()
    }

    private fun getLocale(): Locale? {
        val config = resources.configuration
        var sysLocale: Locale? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config)
        } else {
            sysLocale = getSystemLocaleLegacy(config)
        }

        return sysLocale
    }

    fun getSystemLocaleLegacy(config: Configuration): Locale {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale {
        return config.locales.get(0)
    }

}
