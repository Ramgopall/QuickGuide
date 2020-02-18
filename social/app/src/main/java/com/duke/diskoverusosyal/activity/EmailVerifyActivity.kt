package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.otp_verify.OtpVerifyModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_email_verify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerifyActivity : AppCompatActivity() {

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_email_verify)
        id = intent?.getStringExtra("id")!!
        val email = intent?.getStringExtra("email")!!
        tvEmailOtp.text = "We have sent a code on your email\n$email"

        etEmailOtpFirst .addTextChangedListener(GenericTextWatcher(etEmailOtpFirst ))
        etEmailOtpSecond.addTextChangedListener(GenericTextWatcher(etEmailOtpSecond))
        etEmailOtpThird .addTextChangedListener(GenericTextWatcher(etEmailOtpThird ))
        etEmailOtpFourth.addTextChangedListener(GenericTextWatcher(etEmailOtpFourth))

        btnEmailOtpVerify.setOnClickListener {
            val tempFirstEditText =  etEmailOtpFirst.text.toString().trim()
            val tempSecondEditText = etEmailOtpSecond.text.toString().trim()
            val tempThirdEditText =  etEmailOtpThird.text.toString().trim()
            val tempFourthEditText = etEmailOtpFourth.text.toString().trim()

            if (tempFirstEditText == "" || tempSecondEditText == "" || tempThirdEditText == "" || tempFourthEditText == "") {
                Utils.showToast(this@EmailVerifyActivity,"Please Enter Email Code First")
                return@setOnClickListener
            }

            val tempOtp = tempFirstEditText + tempSecondEditText + tempThirdEditText + tempFourthEditText
            submitClick(tempOtp)
        }
    }

    inner class GenericTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun afterTextChanged(editable: Editable) {
            // TODO Auto-generated method stub
            val text = editable.toString()
            when (view.id) {

                R.id.etEmailOtpFirst -> if (text.length == 1)
                    etEmailOtpSecond.requestFocus()
                R.id.etEmailOtpSecond -> if (text.length == 1)
                    etEmailOtpThird.requestFocus()
                else if (text.isEmpty())
                    etEmailOtpFirst.requestFocus()
                R.id.etEmailOtpThird -> if (text.length == 1)
                    etEmailOtpFourth.requestFocus()
                else if (text.isEmpty())
                    etEmailOtpSecond.requestFocus()
                R.id.etEmailOtpFourth -> if (text.isEmpty())
                    etEmailOtpThird.requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            // TODO Auto-generated method stub
        }

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            // TODO Auto-generated method stub
        }
    }

    private fun submitClick(tempOtp: String) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().emailVerify(id, tempOtp)
        call.enqueue(object : Callback<OtpVerifyModel> {
            override fun onResponse(call: Call<OtpVerifyModel>?, response: Response<OtpVerifyModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        Utils.setUserChangeLoginStatus(true)
                        startActivity(Intent(this@EmailVerifyActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Utils.showToast(this@EmailVerifyActivity,data?.msg!!)
                    }
                } else {
                    Utils.showToast(this@EmailVerifyActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<OtpVerifyModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@EmailVerifyActivity,resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onBackPressed() {
        Utils.showToast(this@EmailVerifyActivity,"Cannot Go back")
    }

}
