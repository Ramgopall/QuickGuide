package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.otp_verify.OtpVerifyModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {

    var id = ""
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_otp)

        id = intent?.getStringExtra("id")!!
        val number = intent?.getStringExtra("number")!!

        tvOtp.text = "We have sent an otp on your number\n$number"

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                resendOtp()
            }
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                tvOtpTimer.text = "OTP auto resend in $timeLeft sec"
            }
        }
        countDownTimer?.start()

        etOtpFirst .addTextChangedListener(GenericTextWatcher(etOtpFirst ))
        etOtpSecond.addTextChangedListener(GenericTextWatcher(etOtpSecond))
        etOtpThird .addTextChangedListener(GenericTextWatcher(etOtpThird ))
        etOtpFourth.addTextChangedListener(GenericTextWatcher(etOtpFourth))

        btnOtpVerify.setOnClickListener {
            val tempFirstEditText =  etOtpFirst.text.toString().trim()
            val tempSecondEditText = etOtpSecond.text.toString().trim()
            val tempThirdEditText =  etOtpThird.text.toString().trim()
            val tempFourthEditText = etOtpFourth.text.toString().trim()

            if (tempFirstEditText == "" || tempSecondEditText == "" || tempThirdEditText == "" || tempFourthEditText == "") {
                Utils.showToast(this@OtpActivity,"Please Enter Otp First")
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

                R.id.etOtpFirst -> if (text.length == 1)
                    etOtpSecond.requestFocus()
                R.id.etOtpSecond -> if (text.length == 1)
                    etOtpThird.requestFocus()
                else if (text.isEmpty())
                    etOtpFirst.requestFocus()
                R.id.etOtpThird -> if (text.length == 1)
                    etOtpFourth.requestFocus()
                else if (text.isEmpty())
                    etOtpSecond.requestFocus()
                R.id.etOtpFourth -> if (text.isEmpty())
                    etOtpThird.requestFocus()
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
        val call = ApiClient().getClient().verifyOtp(id, tempOtp)
        call.enqueue(object : Callback<OtpVerifyModel> {
            override fun onResponse(call: Call<OtpVerifyModel>?, response: Response<OtpVerifyModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        countDownTimer?.cancel()
                        Utils.setUserChangeLoginStatus(true)
                        startActivity(Intent(this@OtpActivity, HomeActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finish()
                    } else {
                        Utils.showToast(this@OtpActivity,data?.msg!!)
                    }
                } else {
                    Utils.showToast(this@OtpActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<OtpVerifyModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@OtpActivity,resources.getString(R.string.api_failure))
            }
        })
    }

    private fun resendOtp() {
        try {
            Utils.showLoadingDialog(this)
        } catch (e: Exception) {
            Log.d("progressDialog", e.printStackTrace().toString())
        }
        val call = ApiClient().getClient().resendOtp(id)
        call.enqueue(object : Callback<OtpVerifyModel> {
            override fun onResponse(call: Call<OtpVerifyModel>?, response: Response<OtpVerifyModel>?) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()?.response?.get(0)
                        if (data?.status == "SUCCESS") {
                            val msg = "OTP resent successfully"
                            tvOtpTimer.text = msg
                        } else {
                            Utils.showToast(this@OtpActivity,data?.msg!!)
                        }
                    } else {
                        Utils.showToast(this@OtpActivity,resources.getString(R.string.no_response))
                    }
                } catch (e: Exception) {
                    Log.d("progressDialog", e.printStackTrace().toString())
                }
            }
            override fun onFailure(call: Call<OtpVerifyModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(this@OtpActivity,resources.getString(R.string.api_failure))
                } catch (e: Exception) {
                    Log.d("progressDialog", e.printStackTrace().toString())
                }
            }
        })
    }

    override fun onBackPressed() {
        Utils.showToast(this@OtpActivity,"Cannot Go back")
    }

}
