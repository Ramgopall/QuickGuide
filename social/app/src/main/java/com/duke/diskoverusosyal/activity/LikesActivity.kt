package com.duke.diskoverusosyal.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_Likes_Adapter
import com.duke.diskoverusosyal.model.likeList.LikeListModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_likes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        ivLikesBack.setOnClickListener { onBackPressed() }
        val id = intent?.getStringExtra("id")
        getPosts(id)
    }

    private fun getPosts(id: String?) {
        pbLikes.visibility = View.VISIBLE
        val call = ApiClient().getClient().getLikeList(id)
        call.enqueue(object : Callback<LikeListModel> {
            override fun onResponse(
                call: Call<LikeListModel>?,
                response: Response<LikeListModel>?
            ) {
                try {
                    pbLikes.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            rvLikes.visibility = View.VISIBLE
                            rvLikes.layoutManager = LinearLayoutManager(this@LikesActivity)
                            rvLikes.adapter =
                                RecyclerView_Likes_Adapter(data.response?.data, this@LikesActivity)
                        } else {
                            Utils.showToast(this@LikesActivity, data.message)
                        }
                    } else {
                        Utils.showToast(
                            this@LikesActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<LikeListModel>?, t: Throwable?) {
                try {
                    pbLikes.visibility = View.GONE
                    Utils.showToast(this@LikesActivity, resources.getString(R.string.api_failure))
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
