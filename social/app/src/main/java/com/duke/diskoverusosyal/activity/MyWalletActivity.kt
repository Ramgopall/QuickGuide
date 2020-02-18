package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_MyWallet_Adapter
import com.duke.diskoverusosyal.model.wallet.WalletModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_my_wallet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWalletActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        ivMyWalletBack.setOnClickListener { onBackPressed() }
        tvMyWalletName.text = Utils.getUserName()
        val imageUrl = Utils.getUserPhoto()
        Utils.showRoundImage(this,imageUrl,ivMyWalletPhoto)
        getData()
    }

    private fun getData() {
        pbMyWallet.visibility = View.VISIBLE
        llMyWallet.visibility = View.GONE
        val call = ApiClient().getClient().getWallet(Utils.getUserSess()!!)
        call.enqueue(object : Callback<WalletModel> {
            override fun onResponse(call: Call<WalletModel>?, response: Response<WalletModel>?) {
                try {
                    pbMyWallet.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        llMyWallet.visibility = View.VISIBLE
                        if (data?.status!!) {
                            rvMyWallet.layoutManager = LinearLayoutManager(this@MyWalletActivity)
                            rvMyWallet.adapter = RecyclerView_MyWallet_Adapter(data.wallet?.data, this@MyWalletActivity)
                        }
                        else{
                            Utils.showToast(this@MyWalletActivity, data.message)
                        }
                        val tempTotal = "$ "+response.body()?.total
                        tvMyWalletAmount.text = tempTotal

                        btnMyWalletClaim.setOnClickListener {
                            if (response.body()?.total?.toDouble()!! < response.body()?.min?.toDouble()!!){
                                Utils.showLongToast(this@MyWalletActivity,"Minimum Withdrawal Amount is $${response.body()?.min} and your wallet has not the sufficient amount")
                                return@setOnClickListener
                            }
                            startActivity(
                                Intent(this@MyWalletActivity,WithdrawActivity::class.java)
                                    .putExtra("amount",response.body()?.max)
                            )
                        }


                    } else {
                        Utils.showToast(this@MyWalletActivity, resources.getString(R.string.no_response))
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<WalletModel>?, t: Throwable?) {
                try {
                    pbMyWallet.visibility = View.GONE
                    Utils.showToast(this@MyWalletActivity, resources.getString(R.string.api_failure))
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
