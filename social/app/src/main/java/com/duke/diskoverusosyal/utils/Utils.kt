package com.duke.diskoverusosyal.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duke.diskoverusosyal.R
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import kotlinx.android.synthetic.main.dialog_upload_progress.*
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private var progressDialog: Dialog? = null
    private var alertDialog: AlertDialog? = null

    private val SHARED_PREFERENCES = "DiskoverUKlub"

    private var sPreferences: SharedPreferences? = null

    private val IS_USER_LOGIN = "IS_USER_LOGIN"
    private val USER_ID         = "USER_ID"
    private val USER_NAME       = "USER_NAME"
    private val USER_EMAIL      = "USER_EMAIL"
    private val USER_REFFEREL   = "USER_REFFEREL"
    private val USER_PHONE      = "USER_PHONE"
    private val USER_PHOTO      = "USER_PHOTO"
    private val USER_SESS       = "USER_SESS"
    private val USER_GENDER     = "USER_GENDER"


    fun init(context: Context?) {
        sPreferences = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun isUserLogin(): Boolean? {
        return sPreferences?.getBoolean(IS_USER_LOGIN, false)
    }

    fun getUserId(): String? {
        return sPreferences?.getString(USER_ID, "")
    }

    fun getUserName(): String? {
        return sPreferences?.getString(USER_NAME, "")
    }

    fun getUserEmail(): String? {
        return sPreferences?.getString(USER_EMAIL, "")
    }

    fun getUserRefferel(): String? {
        return sPreferences?.getString(USER_REFFEREL, "")
    }

    fun getUserPhone(): String? {
        return sPreferences?.getString(USER_PHONE, "")
    }

    fun getUserPhoto(): String? {
        return sPreferences?.getString(USER_PHOTO, "")
    }

    fun getUserSess(): String? {
        return sPreferences?.getString(USER_SESS, "")
    }

    fun getUserGender(): String? {
        return sPreferences?.getString(USER_GENDER, "")
    }

    fun setUserChangeLoginStatus(status: Boolean){
        sPreferences?.edit()
            ?.putBoolean(IS_USER_LOGIN, status)
            ?.apply()
    }

    fun updateUserName(name: String){
        sPreferences?.edit()
            ?.putString(USER_NAME, name)
            ?.apply()
    }

    fun updateUserGender(gender: String){
        sPreferences?.edit()
            ?.putString(USER_GENDER, gender)
            ?.apply()
    }

    fun updateUserPhoto(photo: String){
        sPreferences?.edit()
            ?.putString(USER_PHOTO, photo)
            ?.apply()
    }

    fun updateUserPhone(phone: String){
        sPreferences?.edit()
            ?.putString(USER_PHONE, phone)
            ?.apply()
    }

    fun setUserLogin(status: Boolean,id: String?,name: String?,email: String?,refferel: String?,phone: String?,photo: String?,sess: String?,gender: String?) {
        sPreferences?.edit()
            ?.putBoolean(IS_USER_LOGIN, status)
            ?.putString(USER_ID      , id)
            ?.putString(USER_NAME    , name)
            ?.putString(USER_EMAIL   , email)
            ?.putString(USER_REFFEREL, refferel)
            ?.putString(USER_PHONE   , phone)
            ?.putString(USER_PHOTO   , photo)
            ?.putString(USER_SESS    , sess)
            ?.putString(USER_GENDER  , gender)
            ?.apply()
    }

    fun clearUser() {
        sPreferences?.edit()
            ?.clear()
            ?.apply()
    }

    fun showImage(context: Context?, imagePath: String?, imageView: ImageView?) {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(imagePath)
            .apply(RequestOptions().placeholder(circularProgressDrawable))
            .into(imageView!!)
    }

    fun showImage(context: Context?, imagePath: Bitmap?, imageView: ImageView?) {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(imagePath)
            .apply(RequestOptions().placeholder(circularProgressDrawable))
            .into(imageView!!)
    }

    fun showImage(context: Context?, imagePath: Int?, imageView: ImageView?) {
        Glide.with(context!!)
            .load(imagePath)
            .into(imageView!!)
    }

    fun showCenterCropImage(context: Context?, imagePath: Any?, imageView: ImageView?) {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(imagePath)
            .apply(RequestOptions().centerCrop().placeholder(circularProgressDrawable))
            .into(imageView!!)
    }

    fun showThumbnailFromUrl(context: Context?, imagePath: Any?, imageView: ImageView?) {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val interval = 1000
        val options = RequestOptions().frame(interval.toLong()).centerCrop().placeholder(circularProgressDrawable)
        Glide.with(context)
            .asBitmap()
            .load(imagePath)
            .apply(options)
            .into(imageView!!)
    }

    fun showRoundImage(context: Context?, imagePath: String?, imageView: ImageView?) {
//        val circularProgressDrawable = CircularProgressDrawable(context!!)
//        circularProgressDrawable.strokeWidth = 5f
//        circularProgressDrawable.centerRadius = 30f
//        circularProgressDrawable.start()

        Glide.with(context!!)
            .load(imagePath)
            .apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.dummy)
            )
            .into(imageView!!)
    }

    fun showToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    @Suppress("DEPRECATION")
    fun getImagePath(imageUri: Uri, context: Context?): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(imageUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return picturePath
    }

    @Suppress("DEPRECATION")
    fun getVideoPath(videoUri: Uri, context: Context?): String? {
        val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
        // TRY getActivity() as well if not work
        val cursor = context?.contentResolver?.query(videoUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val videoPath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return videoPath
    }

    fun getVideoPaths(data: Intent, context: Context?) :String {
        var filePath = ""
        var wholeID=""
        val selectedImage = data.getData()

        wholeID = DocumentsContract.getDocumentId(selectedImage)

        val id = wholeID.split(":")[1]

        val column = arrayOf({MediaStore.Video.Media.DATA}.toString())

        // where id is equal to
        val sel = MediaStore.Images.Media._ID + "=?"

        val cursor = context?.getContentResolver()?.
                query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf({id}.toString()), null)


        val columnIndex = cursor?.getColumnIndex(column[0])

        if (cursor?.moveToFirst()!!) {
            filePath = cursor.getString(columnIndex!!)
        }
        cursor.close()
        return filePath
    }

    @Suppress("DEPRECATION")
    fun createVideoThumbNail(path: String): Bitmap {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND)!!
    }


    fun showLoadingDialog(context: Context?) {
        if (!(progressDialog != null && progressDialog!!.isShowing)) {
            progressDialog = Dialog(context!!)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog!!.setContentView(R.layout.progress_dialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun cancelLoading() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.cancel()
    }

    fun showUploadingDialog(context: Context?,title:String) {
        if (!(alertDialog != null && alertDialog!!.isShowing)) {
            val dialog = AlertDialog.Builder(context)
            dialog.setView(R.layout.dialog_upload_progress)
            dialog.setCancelable(false)
            alertDialog = dialog.create()
            alertDialog?.show()
            alertDialog?.tvUploadTitle?.text = title
        }
    }

    fun changeUploadPercentage(progress:Int) {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog?.pbUpload?.progress = progress
            alertDialog?.tvUpload?.text = "$progress%"
        }
    }

    fun finishUploadingLoading() {
        if (alertDialog != null && alertDialog!!.isShowing)
            alertDialog?.dismiss()
    }


    fun rand() : String {
        val rnds1 = (1000..9999).random()
        return "$rnds1"
    }

    fun getCurrentTimeStamp(): String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }

    fun dateConvert(firstDate:String?): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val date = formatter.parse(firstDate!!)!!
        val tempDay = SimpleDateFormat("dd",Locale.US).format(date)
        val tempMonth = SimpleDateFormat("MMM",Locale.US).format(date)
        val tempYear = SimpleDateFormat("yyyy",Locale.US).format(date)
        val tempTime = SimpleDateFormat("KK:mm a",Locale.US).format(date)
        return "$tempMonth $tempDay,$tempYear $tempTime"
    }

    fun loadCountryJSONFromAsset(context: Context?): String? {
        var json: String? = null
        try {
            val iss = context?.assets?.open("countries.json")
            val size = iss?.available()!!
            val buffer = ByteArray(size)
            iss.read(buffer)
            iss.close()
            val charset: Charset = Charsets.UTF_8
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
