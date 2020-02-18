package com.duke.diskoverusosyal.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_ChatUsers_Adapter
import com.duke.diskoverusosyal.model.chatUsers.ChatUsersModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_chat_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_users)
        ivChatUsersBack.setOnClickListener { onBackPressed() }
        srlChatUsers.setColorSchemeResources(R.color.colorBlack)
        srlChatUsers.setOnRefreshListener {
            getPosts()
        }
    }

    override fun onResume() {
        getPosts()
        super.onResume()
    }

    private fun getPosts() {
        pbChatUsers.visibility = View.VISIBLE
        rvChatUsers.visibility = View.GONE
        srlChatUsers.visibility = View.GONE
        val call = ApiClient().getClient().getChatUsers(Utils.getUserSess())
        call.enqueue(object : Callback<ChatUsersModel> {
            override fun onResponse(
                call: Call<ChatUsersModel>?,
                response: Response<ChatUsersModel>?
            ) {
                try {
                    pbChatUsers.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            rvChatUsers.visibility = View.VISIBLE
                            rvChatUsers.layoutManager = LinearLayoutManager(this@ChatUsersActivity)
                            rvChatUsers.adapter = RecyclerView_ChatUsers_Adapter(
                                data.response,
                                this@ChatUsersActivity
                            )
                        } else {
                            Utils.showToast(this@ChatUsersActivity, data.message)
                        }
                    }
                    else {
                        Utils.showToast(
                            this@ChatUsersActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                    srlChatUsers.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<ChatUsersModel>?, t: Throwable?) {
                try {
                    pbChatUsers.visibility = View.GONE
                    srlChatUsers.visibility = View.VISIBLE
                    Utils.showToast(
                        this@ChatUsersActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
        srlChatUsers.isRefreshing = false
    }

    override fun onBackPressed() {
        finish()
    }
}
