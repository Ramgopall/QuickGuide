package com.duke.diskoverusosyal.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.forgot.ForgotPasswordModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils.cancelLoading
import com.duke.diskoverusosyal.utils.Utils.isValidEmail
import com.duke.diskoverusosyal.utils.Utils.showLoadingDialog
import com.duke.diskoverusosyal.utils.Utils.showToast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        btnForgot.setOnClickListener {
            val email = etForgot.text.toString().trim()
            if (email == "") {
                etForgot.requestFocus()
                etForgot.error = "Email is required"
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                etForgot.requestFocus()
                etForgot.error = "Enter a valid Email"
                return@setOnClickListener
            }
            submitClick(email)
        }
    }

    private fun submitClick(email: String) {
        showLoadingDialog(this)
        val call = ApiClient().getClient().forgot(email)
        call.enqueue(object : Callback<ForgotPasswordModel> {
            override fun onResponse(call: Call<ForgotPasswordModel>?, response: Response<ForgotPasswordModel>?) {
                cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status=="SUCCESS") {
                        val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                        dialog.setTitle("Submit Successfully")
                        dialog.setMessage(data.msg)
                        dialog.setCancelable(false)
                        dialog.setPositiveButton("okay") { _, _ ->
                            finish()
                        }
                        val alertDialog = dialog.create()
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"))


                    } else {
                        val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                        dialog.setTitle("Failed")
                        dialog.setMessage(data?.msg)
                        dialog.setCancelable(false)
                        dialog.setNegativeButton("okay") { _, _ -> }
                        val alertDialog = dialog.create()
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"))

                    }
                } else {
                    showToast(
                        this@ForgotPasswordActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<ForgotPasswordModel>?, t: Throwable?) {
                cancelLoading()
                showToast(
                    this@ForgotPasswordActivity,
                    resources.getString(R.string.api_failure)
                )
            }
        })
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
