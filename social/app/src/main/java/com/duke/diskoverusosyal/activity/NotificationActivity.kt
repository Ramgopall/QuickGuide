package com.duke.diskoverusosyal.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_Notification_Adapter
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.notification.NotificationModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        ivNotificationBack.setOnClickListener { onBackPressed() }
        getData()
        markReaded()
    }

    private fun getData() {
        pbNotification.visibility = View.VISIBLE
        rvNotification.visibility = View.GONE
        val call = ApiClient().getClient().getNotification(Utils.getUserSess()!!)
        call.enqueue(object : Callback<NotificationModel> {
            override fun onResponse(
                call: Call<NotificationModel>?,
                response: Response<NotificationModel>?
            ) {
                try {
                    pbNotification.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            rvNotification.visibility = View.VISIBLE
                            rvNotification.layoutManager =
                                LinearLayoutManager(this@NotificationActivity)
                            rvNotification.adapter =
                                RecyclerView_Notification_Adapter(data.response?.data,
                                    this@NotificationActivity,
                                    object : RecyclerView_Notification_Adapter.OnItemClicks {
                                        override fun refreshRecycler() {
                                            getData()
                                        }
                                    }
                                )
                        } else {
                            Utils.showToast(this@NotificationActivity, data.message)
                        }
                    } else {
                        Utils.showToast(
                            this@NotificationActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }

            override fun onFailure(call: Call<NotificationModel>?, t: Throwable?) {
                try {
                    pbNotification.visibility = View.GONE
                    Utils.showToast(
                        this@NotificationActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "yup")
                }
            }
        })
    }

    private fun markReaded() {
        val call = ApiClient().getClient().markReadedNotification(Utils.getUserSess()!!)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {

            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {

            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
