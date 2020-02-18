package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.CheckReferCodeModel
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.sendOtp.SendOtpModel
import com.duke.diskoverusosyal.model.updateProfile.UpdateProfileModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_update_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {
    var photo = ""
    var checkRef = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        getData()
        ivUpdateProfileBack.setOnClickListener { finish() }

        tvUpdateProfilePhoneUpdate.visibility = View.INVISIBLE
        Utils.showRoundImage(this, Utils.getUserPhoto()!!, ivUpdateProfilePhoto)
        etUpdateProfileName.setText(Utils.getUserName()!!)
        etUpdateProfileEmail.setText(Utils.getUserEmail()!!)
        etUpdateProfilePhone.setText(Utils.getUserPhone()!!)
        etUpdateProfileEmail.isEnabled = false
        etUpdateProfilePhone.isEnabled = false

        if (Utils.getUserGender() == "1") {
            rbUpdateProfileMale.isChecked = true
        }
        if (Utils.getUserGender() == "2") {
            rbUpdateProfileFeMale.isChecked = true
        }

        if (Utils.getUserPhone() == "") {
            tvUpdateProfilePhone.visibility = View.GONE
            rlUpdateProfilePhone.visibility = View.GONE
        }

        ivUpdateProfilePhone.setOnClickListener {
            tvUpdateProfilePhoneUpdate.visibility = View.VISIBLE
            etUpdateProfilePhone.isEnabled = true
            etUpdateProfilePhone.requestFocus()
        }

        ivUpdateProfilePhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 2)
        }

        ivUpdateProfileSubmit.setOnClickListener {
            val tempName = etUpdateProfileName.text.toString().trim()
            val tempSkill = etUpdateProfileSkill.text.toString().trim()
            val tempExp = etUpdateProfileExperience.text.toString().trim()
            val tempDetail = etUpdateProfilePdetail.text.toString().trim()
            val tempRef = etUpdateProfileRef.text.toString().trim()
            if (tempName == "") {
                etUpdateProfileName.requestFocus()
                etUpdateProfileName.error = "Name is Required"
                return@setOnClickListener
            }

            submit(tempName,tempSkill,tempExp,tempDetail)
            if (!checkRef && tempRef!=""){
                submitRef(tempRef)
            }
        }

        tvUpdateProfilePhoneUpdate.setOnClickListener {
            val tempNumber = etUpdateProfilePhone.text.toString().trim()
            if (tempNumber == "") {
                etUpdateProfilePhone.requestFocus()
                etUpdateProfilePhone.error = "Number is Required"
                return@setOnClickListener
            }
            updateNumber(tempNumber)
        }
        btnUpdateProfileDeactive.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Deactivate Account!")
            dialog.setMessage("Are you sure you want to deactivate this account?")
            dialog.setPositiveButton("Yes") { _, _ ->
                deactive()
                dialog.create().dismiss()
            }
            dialog.setNegativeButton("No") { _, _ -> }
            dialog.setCancelable(false)
            dialog.create().show()
        }
        btnUpdateProfileDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Permanent Delete Account!")
            dialog.setMessage("Are you sure you want to delete this account?")
            dialog.setPositiveButton("Yes") { _, _ ->
                delete()
                dialog.create().dismiss()
            }
            dialog.setNegativeButton("No") { _, _ -> }
            dialog.setCancelable(false)
            dialog.create().show()
        }
    }

    private fun getData() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().checkReferCode(Utils.getUserSess()!!)
        call.enqueue(object : Callback<CheckReferCodeModel> {
            override fun onResponse(
                call: Call<CheckReferCodeModel>?,
                response: Response<CheckReferCodeModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        checkRef = data?.status!!
                        if (data.status) {
                            etUpdateProfileRef.setText(data.message)
                            etUpdateProfileRef.isEnabled = false
                        }

                        if (data.skill!=null) {
                            etUpdateProfileSkill.setText(data.skill.toString())
                        }
                        if (data.exp!=null) {
                            etUpdateProfileExperience.setText(data.exp.toString())
                        }
                        if (data.detail!=null) {
                            etUpdateProfilePdetail.setText(data.detail.toString())
                        }
                    } else {
                        Utils.showToast(
                            this@UpdateProfileActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                    llUpdateProfile.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<CheckReferCodeModel>?, t: Throwable?) {
                try {
                    llUpdateProfile.visibility = View.VISIBLE
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            photo = Utils.getImagePath(imageUri!!, this@UpdateProfileActivity)!!
            Utils.showRoundImage(this, photo, ivUpdateProfilePhoto)
        }
    }

    private fun submit(
        tempName: String,
        tempSkill: String,
        tempExp: String,
        tempDetail: String
    ) {
        val gender = if (rbUpdateProfileMale.isChecked) {
            "1"
        } else {
            "2"
        }
        val requestBodySess = RequestBody.create(MediaType.parse("text/plain"), Utils.getUserSess()!!)
        val requestBodyName = RequestBody.create(MediaType.parse("text/plain"), tempName)
        val requestBodySkill = RequestBody.create(MediaType.parse("text/plain"), tempSkill)
        val requestBodyExp = RequestBody.create(MediaType.parse("text/plain"), tempExp)
        val requestBodyDetail = RequestBody.create(MediaType.parse("text/plain"), tempDetail)
        val requestBodyGender = RequestBody.create(MediaType.parse("text/plain"), gender)

        val requestBodyPhoto1 = if (photo == "") {
            RequestBody.create(MediaType.parse("image/*"), "")
        } else {
            RequestBody.create(MediaType.parse("image/*"), File(photo))
        }

        val multipartPhoto1 = if (photo == "") {
            MultipartBody.Part.createFormData("file", "", requestBodyPhoto1)
        } else {
            MultipartBody.Part.createFormData("file", "imageFile.jpg", requestBodyPhoto1)
        }

        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient()
            .updateProfile(requestBodySess, requestBodyName, requestBodyGender,requestBodyExp,requestBodyDetail,requestBodySkill, multipartPhoto1)
        call.enqueue(object : Callback<UpdateProfileModel> {
            override fun onResponse(
                call: Call<UpdateProfileModel>?,
                response: Response<UpdateProfileModel>?
            ) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        Utils.updateUserGender(gender)
                        Utils.updateUserName(tempName)
                        Utils.updateUserPhoto(data.url!!)
                        Utils.showToast(this@UpdateProfileActivity, data.msg!!)
                    } else {
                        Utils.showToast(this@UpdateProfileActivity, data?.msg!!)
                    }
                } else {
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<UpdateProfileModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(
                    this@UpdateProfileActivity,
                    resources.getString(R.string.api_failure)
                )
            }
        })

    }

    private fun submitRef(tempName: String) {
        val call = ApiClient().getClient().insertReferCode(Utils.getUserSess(),"SO", tempName)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                try {
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            etUpdateProfileRef.isEnabled = false
                        } else {
                            Utils.showToast(this@UpdateProfileActivity, data.message!!)
                        }
                    } else {
                        Utils.showToast(
                            this@UpdateProfileActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                try {
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })

    }

    private fun deactive() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().deactive(Utils.getUserSess())
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            Utils.clearUser()
                            startActivity(
                                Intent(this@UpdateProfileActivity, LoginActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        } else {
                            Utils.showToast(this@UpdateProfileActivity, data.message!!)
                        }
                    } else {
                        Utils.showToast(
                            this@UpdateProfileActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })

    }

    private fun delete() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().deleteAccount(Utils.getUserSess())
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            Utils.clearUser()
                            startActivity(
                                Intent(this@UpdateProfileActivity, LoginActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        } else {
                            Utils.showToast(this@UpdateProfileActivity, data.message!!)
                        }
                    } else {
                        Utils.showToast(
                            this@UpdateProfileActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })

    }

    private fun updateNumber(tempNumber: String) {
        Utils.showLoadingDialog(this)
        val otp = Utils.rand()
        Log.d("otp", otp)
        val call = ApiClient().getClient().sendOtp(otp, tempNumber)
        call.enqueue(object : Callback<SendOtpModel> {
            override fun onResponse(call: Call<SendOtpModel>?, response: Response<SendOtpModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        startActivity(
                            Intent(
                                this@UpdateProfileActivity,
                                UpdateNumberVerifyActivity::class.java
                            )
                                .putExtra("otp", otp)
                                .putExtra("number", tempNumber)
                        )
                    } else {
                        Utils.showToast(this@UpdateProfileActivity, data?.msg!!)
                    }
                } else {
                    Utils.showToast(
                        this@UpdateProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<SendOtpModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(
                    this@UpdateProfileActivity,
                    resources.getString(R.string.api_failure)
                )
            }
        })

    }
}
