package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.refer.ReferModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_refer_and_earn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferAndEarnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_and_earn)
        ivReferBack.setOnClickListener { finish() }
        getData()
    }

    private fun getData() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getRefer(Utils.getUserSess()!!)
        call.enqueue(object : Callback<ReferModel> {
            override fun onResponse(call: Call<ReferModel>?, response: Response<ReferModel>?) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        tvReferText.text = data?.text
                        tvReferShare.visibility = View.VISIBLE
                        tvReferCode.text = "Your code is: ${data?.reffrel}"
                        tvReferShare.text = "Share your code: ${data?.reffrel}"
                        tvReferShare.setOnClickListener {
                            val text =
                                "I am enjoying earning with DiskoverU Sosyal. Join me using my code " + data?.reffrel + " and get "+data?.amount+" on your registration.\n\nhttps://play.google.com/store/apps/details?id=com.duke.diskoverusosyal"
                            val i = Intent(Intent.ACTION_SEND)
                            i.type = "text/plain"
                            i.putExtra(Intent.EXTRA_SUBJECT, "DiskoverU LifeStyle")
                            i.putExtra(Intent.EXTRA_TEXT, text)
                            startActivity(Intent.createChooser(i, "choose one"))
                        }

                    } else {
                        Utils.showToast(
                            this@ReferAndEarnActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<ReferModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@ReferAndEarnActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

}
