package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.bankInfo.BankInfoModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_withdraw.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)
        ivWithDrawBack.setOnClickListener { finish() }
        val amount = intent?.getStringExtra("amount")
        etWithDrawAmount.setText(amount)
        btnWithDrawSubmit.setOnClickListener {
            val tempBName = etWithDrawBName.text.toString().trim()
            val tempUName = etWithDrawUName.text.toString().trim()
            val tempAccountNo = etWithDrawAccountNo.text.toString().trim()
            val tempIfsc = etWithDrawIfsc.text.toString().trim()
            val tempAmount = etWithDrawAmount.text.toString().trim()
            if (tempBName == "") {
                etWithDrawBName.requestFocus()
                etWithDrawBName.error = "Bank name is required"
                return@setOnClickListener
            }
            if (tempUName == "") {
                etWithDrawUName.requestFocus()
                etWithDrawUName.error = "Account Holder Name is required"
                return@setOnClickListener
            }
            if (tempAccountNo == "") {
                etWithDrawAccountNo.requestFocus()
                etWithDrawAccountNo.error = "Account Number is required"
                return@setOnClickListener
            }
            if (tempAccountNo.length < 10) {
                etWithDrawAccountNo.requestFocus()
                etWithDrawAccountNo.error = "Enter a valid Account Number"
                return@setOnClickListener
            }
            if (tempIfsc == "") {
                etWithDrawIfsc.requestFocus()
                etWithDrawIfsc.error = "IFSC Code is required"
                return@setOnClickListener
            }
            if (tempAmount == "" && tempAmount == "0") {
                etWithDrawAmount.requestFocus()
                etWithDrawAmount.error = "Withdrawal Amount is required"
                return@setOnClickListener
            }
            if (tempAmount.toDouble() > amount?.toDouble()!!) {
                etWithDrawAmount.requestFocus()
                etWithDrawAmount.error =
                    "Your requesting amount is greater then withdrawal limit i.e $amount"
                return@setOnClickListener
            }
            submitRequest(
                tempBName,
                tempUName,
                tempAccountNo,
                tempIfsc,
                tempAmount
            )
        }
        getData()
    }

    private fun getData() {
        pbWithDraw.visibility = View.VISIBLE
        cvWithDraw.visibility = View.GONE
        val call = ApiClient().getClient().getBankInfo(Utils.getUserSess()!!)
        call.enqueue(object : Callback<BankInfoModel> {
            override fun onResponse(
                call: Call<BankInfoModel>?,
                response: Response<BankInfoModel>?
            ) {
                try {
                    pbWithDraw.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        cvWithDraw.visibility = View.VISIBLE
                        if (data?.status!!) {
                            etWithDrawBName.setText(data.response?.data?.get(0)?.bname)
                            etWithDrawUName.setText(data.response?.data?.get(0)?.name)
                            etWithDrawAccountNo.setText(data.response?.data?.get(0)?.accountno)
                            etWithDrawIfsc.setText(data.response?.data?.get(0)?.ifsc)
                        }
                    } else {
                        Utils.showToast(
                            this@WithdrawActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<BankInfoModel>?, t: Throwable?) {
                try {
                    pbWithDraw.visibility = View.GONE
                    Utils.showToast(
                        this@WithdrawActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

    private fun submitRequest(
        tempBName: String,
        tempUName: String,
        tempAccountNo: String,
        tempIfsc: String,
        tempAmount: String
    ) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().requestWithdraw(
            tempUName,
            tempBName,
            tempAccountNo,
            tempIfsc,
            tempAmount,
            Utils.getUserSess()!!
        )
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
                            val dialog = AlertDialog.Builder(this@WithdrawActivity)
                            dialog.setTitle("Request Submitted Successfully")
                            dialog.setMessage("Your request to withdrawal the wallet money has been submitted successfully.")
                            dialog.setNegativeButton("Okay") { _, _ -> finish() }
                            dialog.setCancelable(false)
                            dialog.create().show()
                        } else {
                            Utils.showToast(this@WithdrawActivity, data.message)
                        }
                    } else {
                        Utils.showToast(
                            this@WithdrawActivity,
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
                        this@WithdrawActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

}
