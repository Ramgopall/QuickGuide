package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.offer.OfferModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_offer_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_view)
        val id = intent?.getStringExtra("id")
        ivOfferBack.setOnClickListener { onBackPressed() }
        getPosts(id)
    }

    private fun getPosts(id: String?) {
        pbOffer.visibility = View.VISIBLE
        val call = ApiClient().getClient().getOfferDetail(id)
        call.enqueue(object : Callback<OfferModel> {
            override fun onResponse(
                call: Call<OfferModel>?,
                response: Response<OfferModel>?
            ) {
                pbOffer.visibility = View.GONE
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    if (data?.status!!) {
                        llOfferPost.visibility = View.VISIBLE
                        Utils.showRoundImage(
                            this@OfferViewActivity,
                            data.response?.data?.get(0)?.url,
                            ivOfferPost
                        )
                        tvOfferName.text = data.response?.data?.get(0)?.name
                        val dateText =  "Valid From "+Utils.dateConvert(data.response?.data?.get(0)?.datefrom)+" to "+Utils.dateConvert(data.response?.data?.get(0)?.dateto)
                        tvOfferDates.text = dateText
                        tvOfferDesc.text = Html.fromHtml(data.response?.data?.get(0)?.text)
                    } else {
                        Utils.showToast(this@OfferViewActivity, data.message)
                    }
                } else {
                    Utils.showToast(
                        this@OfferViewActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<OfferModel>?, t: Throwable?) {
                pbOffer.visibility = View.GONE
                Utils.showToast(this@OfferViewActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
