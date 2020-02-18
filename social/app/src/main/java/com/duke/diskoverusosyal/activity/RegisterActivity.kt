package com.duke.diskoverusosyal.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.register.RegisterModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import com.duke.diskoverusosyal.utils.Utils.loadCountryJSONFromAsset
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity() {

    private val PERMISSION_CALLBACK_CONSTANT = 100
    var permissionsRequired = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val mobileCodeList = arrayListOf<String>()
    private val countryList = arrayListOf<String>()
    val countryIdList = arrayListOf<String>()
    var countryId = ""
    var mobileCode = ""
    var photo = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)
        rbRegisterMale.isChecked = true
        country()
        runtimePermission()

        ivRegisterPhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 2)
        }

        btnRegisterLogin.setOnClickListener {
           startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }

        btnRegisterSubmit.setOnClickListener {
            val tempName = etRegisterName.text.toString().trim()
            val tempEmail = etRegisterEmail.text.toString().trim()
            val tempNumber = etRegisterNumber.text.toString().trim()
            val tempPassword = etRegisterPassword.text.toString().trim()
            val tempRCode = etRegisterReferralCode.text.toString().trim()

            if (tempName == "") {
                etRegisterName.requestFocus()
                etRegisterName.error = "Name is required"
                return@setOnClickListener
            }
            if (tempEmail == "") {
                etRegisterEmail.requestFocus()
                etRegisterEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (!Utils.isValidEmail(tempEmail)) {
                etRegisterEmail.requestFocus()
                etRegisterEmail.error = "Enter valid a email address"
                return@setOnClickListener
            }
            if (tempNumber == "") {
                etRegisterNumber.requestFocus()
                etRegisterNumber.error = "Number is required"
                return@setOnClickListener
            }
            if (tempNumber.length < 10) {
                etRegisterNumber.requestFocus()
                etRegisterNumber.error = "Enter valid a number"
                return@setOnClickListener
            }
            if (tempPassword == "") {
                etRegisterPassword.requestFocus()
                etRegisterPassword.error = "Password is required"
                return@setOnClickListener
            }

            submit(tempName, tempEmail, tempNumber, tempPassword, tempRCode)
        }
    }


    private fun runtimePermission(){
        // Multiple Runtime Permission
        if (ActivityCompat.checkSelfPermission(this@RegisterActivity, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@RegisterActivity, permissionsRequired[0])) {
                ActivityCompat.requestPermissions(this@RegisterActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            } else {
                ActivityCompat.requestPermissions(this@RegisterActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }
            if (allgranted) { }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this@RegisterActivity, permissionsRequired[0])) {
                ActivityCompat.requestPermissions(this@RegisterActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }
    }

    private fun country() {
        Thread(Runnable {
            val obj = JSONObject(loadCountryJSONFromAsset(this)!!)
            val mArray = obj.getJSONArray("countries")
            for (i in 0 until mArray.length()) {
                val joInside = mArray.getJSONObject(i)
                mobileCodeList.add(joInside.getString("phonecode"))
                countryIdList.add(joInside.getString("id"))
                countryList.add(joInside.getString("spn"))
            }
            runOnUiThread {
                val adapter = ArrayAdapter(this, R.layout.spinner_items, countryList)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spnRegisterCode.adapter = adapter
                spnRegisterCode.setSelection(100)

                spnRegisterCode?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        countryId = countryIdList[position]
                        mobileCode = mobileCodeList[position]
                    }
                }
            }
        }).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            photo = Utils.getImagePath(imageUri!!, this@RegisterActivity)!!
            Utils.showRoundImage(this,photo,ivRegisterPhoto)
        }
    }

    private fun submit(
        tempName: String, tempEmail: String, tempNumber: String, tempPassword: String,
        tempRCode: String
    ) {
        val gender = if (rbRegisterMale.isChecked){"1"} else{"2"}
        val requestBodyName = RequestBody.create(MediaType.parse("text/plain"), tempName)
        val requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), tempEmail)
        val requestBodyNumber = RequestBody.create(MediaType.parse("text/plain"), tempNumber)
        val requestBodyPassword = RequestBody.create(MediaType.parse("text/plain"), tempPassword)
        val requestBodyRCode = RequestBody.create(MediaType.parse("text/plain"), tempRCode)
        val requestBodyGender = RequestBody.create(MediaType.parse("text/plain"), gender)
        val requestBodyMobileCode = RequestBody.create(MediaType.parse("text/plain"), mobileCode)
        val requestBodyType = RequestBody.create(MediaType.parse("text/plain"), "SO")

        val requestBodyPhoto1 = if (photo == "") { RequestBody.create(MediaType.parse("image/*"), "")
        } else { RequestBody.create(MediaType.parse("image/*"), File(photo)) }

        val multipartPhoto1 = if (photo == "") { MultipartBody.Part.createFormData("file", "", requestBodyPhoto1)
        } else { MultipartBody.Part.createFormData("file", "imageFile.jpg", requestBodyPhoto1) }

        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().register(requestBodyName,requestBodyEmail,requestBodyNumber,
            requestBodyRCode,requestBodyPassword,requestBodyGender,requestBodyMobileCode,requestBodyType,multipartPhoto1)
        call.enqueue(object : Callback<RegisterModel> {
            override fun onResponse(call: Call<RegisterModel>?, response: Response<RegisterModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        Utils.setUserLogin(false,data.last_id,data.name,data.email,data.reffrel,data.phone,data.photo,data.sess!!,data.gender)
                        if (mobileCode=="91"){
                            startActivity(
                                Intent(this@RegisterActivity, OtpActivity::class.java)
                                    .putExtra("id", data.sess)
                                    .putExtra("number", data.phone)
                            )
                        }else{
                            startActivity(
                                Intent(this@RegisterActivity, EmailVerifyActivity::class.java)
                                    .putExtra("id", data.sess)
                                    .putExtra("email", data.email)
                            )
                        }

                        finish()
                    } else {
                        Utils.showToast(this@RegisterActivity,data?.status!!)
                    }
                } else {
                    Utils.showToast(this@RegisterActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<RegisterModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@RegisterActivity,resources.getString(R.string.api_failure))
            }
        })

    }

}
