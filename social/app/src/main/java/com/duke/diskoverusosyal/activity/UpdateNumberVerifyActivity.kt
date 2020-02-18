package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.updateNumber.UpdateNumberModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_update_number_verify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateNumberVerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_number_verify)

        val otp = intent?.getStringExtra("otp")
        val number = intent?.getStringExtra("number")

        etOtpFirst.addTextChangedListener(GenericTextWatcher(etOtpFirst))
        etOtpSecond.addTextChangedListener(GenericTextWatcher(etOtpSecond))
        etOtpThird.addTextChangedListener(GenericTextWatcher(etOtpThird))
        etOtpFourth.addTextChangedListener(GenericTextWatcher(etOtpFourth))

        btnOtpVerify.setOnClickListener {
            val tempFirstEditText = etOtpFirst.text.toString().trim()
            val tempSecondEditText = etOtpSecond.text.toString().trim()
            val tempThirdEditText = etOtpThird.text.toString().trim()
            val tempFourthEditText = etOtpFourth.text.toString().trim()

            if (tempFirstEditText == "" || tempSecondEditText == "" || tempThirdEditText == "" || tempFourthEditText == "") {
                Utils.showToast(this@UpdateNumberVerifyActivity, "Please Enter Otp First")
                return@setOnClickListener
            }

            val tempOtp = tempFirstEditText + tempSecondEditText + tempThirdEditText + tempFourthEditText
            if (otp == tempOtp) {
                submitClick(number)
            } else {
                Utils.showToast(this, "Wrong Otp")
            }
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

    private fun submitClick(number: String?) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().updateNumber(Utils.getUserSess(), number)
        call.enqueue(object : Callback<UpdateNumberModel> {
            override fun onResponse(call: Call<UpdateNumberModel>?, response: Response<UpdateNumberModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response
                    if (data?.status == "SUCCESS") {
                        Utils.updateUserPhone(number!!)
                        finish()
                    } else {
                        Utils.showToast(this@UpdateNumberVerifyActivity, "Failed")
                    }
                } else {
                    Utils.showToast(this@UpdateNumberVerifyActivity, resources.getString(R.string.no_response))
                }
            }

            override fun onFailure(call: Call<UpdateNumberModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@UpdateNumberVerifyActivity, resources.getString(R.string.api_failure))
            }
        })
    }
}
