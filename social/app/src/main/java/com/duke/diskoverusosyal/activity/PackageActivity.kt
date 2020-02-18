package com.duke.diskoverusosyal.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_Package_Adapter
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.packageList.PackageListModel
import com.duke.diskoverusosyal.model.paymentToken.PayMentTokenModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils


import kotlinx.android.synthetic.main.activity_package.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener


class PackageActivity : AppCompatActivity(), PaymentResultWithDataListener {

    var transectionId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)
        val checkLimitReached = intent.getBooleanExtra("checkLimitReached",true)
        if (checkLimitReached){
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Hi")
            dialog.setMessage("It looks like you've reached your upload videos limit. If you want to increase the limit then you have to purchase the package.")
            dialog.setNegativeButton("Okay") { _, _ -> }
            dialog.setCancelable(false)
            dialog.create().show()
        }

        ivPackageBack.setOnClickListener { finish() }
        getData()
    }

    private fun getData() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getPackage()
        call.enqueue(object : Callback<PackageListModel> {

            override fun onResponse(
                call: Call<PackageListModel>?,
                response: Response<PackageListModel>?
            ) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    if (data?.status!!) {
                        rvPackage.visibility = View.VISIBLE
                        rvPackage.layoutManager = GridLayoutManager(this@PackageActivity, 2) as RecyclerView.LayoutManager?
                        rvPackage.isNestedScrollingEnabled = false
                        rvPackage.adapter = RecyclerView_Package_Adapter(data.response?.data,
                            object : RecyclerView_Package_Adapter.OnItemClickListener {
                                override fun onItemClick(id: String?) {
//                                    if (Utils.getUserPhone()==""){
//                                        Utils.showLongToast(this@PackageActivity,"Add a phone number in your profile.")
//                                    }
//                                    else {
                                        getPaymentToken(id)
//                                    }
                                }
                            }
                        )
                        Utils.showImage(this@PackageActivity,data.banner,ivPackageBanner)
                        ivPackageBanner.setOnClickListener {
                            startActivity(
                                Intent(this@PackageActivity,BannerTermsActivity::class.java)
                                    .putExtra("Desc",data.description)
                            )
                        }
                    } else {
                        Utils.showToast(this@PackageActivity, data.message)
                    }
                } else {
                    Utils.showToast(this@PackageActivity, resources.getString(R.string.no_response))
                }
            }

            override fun onFailure(call: Call<PackageListModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@PackageActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    private fun getPaymentToken(id:String?) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getPaymentToken(Utils.getUserSess(),id)
        call.enqueue(object : Callback<PayMentTokenModel> {

            override fun onResponse(
                call: Call<PayMentTokenModel>?,
                response: Response<PayMentTokenModel>?
            ) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    val name = Utils.getUserName()
                    transectionId = data?.transection!!

                    val checkout = Checkout()
                    checkout.setImage(R.drawable.launcher_icon)
                    val activity = this@PackageActivity
                    try {
                        val options = JSONObject()
                        options.put("name", name)
                        options.put("order_id", data.cftoken)
                        options.put("currency", "USD")
                        options.put("amount", data.amount)
                        checkout.open(activity, options)
                    } catch (e: Exception) {
                        Utils.showToast(this@PackageActivity,"Error in starting Razorpay Checkout")
                    }

                } else {
                    Utils.showToast(this@PackageActivity, resources.getString(R.string.no_response))
                }
            }

            override fun onFailure(call: Call<PayMentTokenModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@PackageActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        val call = ApiClient().getClient().submitTransectionDonateion("success",paymentData?.orderId,paymentData?.paymentId,paymentData?.signature)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(call: Call<CommonResponseModel>?, response: Response<CommonResponseModel>?) { }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) { }
        })

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Package Bought  Successfully")
        dialog.setMessage("Your transaction id is\n$transectionId")
        dialog.setNegativeButton("Okay") { _, _ -> finish()}
        dialog.setCancelable(false)
        dialog.create().show()
        Checkout.clearUserData(this@PackageActivity)
    }

    override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
        val call = ApiClient().getClient().submitTransectionDonateion("failed",transectionId,"","")
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(call: Call<CommonResponseModel>?, response: Response<CommonResponseModel>?) { }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) { }
        })

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Transaction Failed")
        dialog.setMessage("$description\nYour transaction id is\n$transectionId")
        dialog.setNegativeButton("Okay") { _, _ -> finish()}
        dialog.setCancelable(false)
        dialog.create().show()
    }
}
