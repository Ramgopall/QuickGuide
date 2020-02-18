package com.duke.diskoverusosyal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_MyProfile_Adapter
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.userPosts.UserPostsModel
import com.duke.diskoverusosyal.model.userProfile.UserProfileModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_my_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileActivity : AppCompatActivity() {
    var userIdd: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        userIdd = intent.getStringExtra("id")
        if (Utils.getUserSess() != userIdd) {
            btnMyProfileEdit.visibility = View.INVISIBLE
            fabMyProfileAdd.visibility = View.INVISIBLE
            llMyProfileWallet.visibility = View.INVISIBLE
            btnMyProfileBlock.visibility = View.VISIBLE
            tvMyProfileWallet.isEnabled = false
        }
        else{
            btnMyProfileBlock.visibility = View.GONE
        }
        ivMyProfileBack.setOnClickListener { finish() }
        tvMyProfileWallet.setOnClickListener {
            startActivity(Intent(this, MyWalletActivity::class.java))
        }
        btnMyProfileEdit.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }
        fabMyProfileAdd.setOnClickListener {
            startActivity(Intent(this, AddNewPostActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getData(userIdd)
    }

    private fun getData(id: String?) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getUserProfile(id, Utils.getUserSess())
        call.enqueue(object : Callback<UserProfileModel> {
            override fun onResponse(
                call: Call<UserProfileModel>?,
                response: Response<UserProfileModel>?
            )
            {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    when {
                        data?.chatstatus == "0" -> {
                            btnMyProfileInvite.visibility = View.GONE
                        }
                        data?.chatstatus == "1" -> {
                            btnMyProfileInvite.visibility = View.VISIBLE
                            btnMyProfileBlock.text = "Block"
                        }
                        data?.chatstatus == "2" -> {
                            btnMyProfileInvite.visibility = View.VISIBLE
                            btnMyProfileInvite.text = "Invitation Sent"
                        }
                        data?.chatstatus == "3" -> {
                            btnMyProfileInvite.visibility = View.VISIBLE
                            btnMyProfileInvite.text = "Send Message"
                        }
                        data?.chatstatus == "4" -> {
                            btnMyProfileInvite.visibility = View.GONE
                            btnMyProfileBlock.text = "UnBlock"
                        }
                        data?.chatstatus == "5" -> {
                            btnMyProfileInvite.visibility = View.GONE
                            btnMyProfileBlock.visibility = View.GONE
                        }
                    }
                    btnMyProfileInvite.setOnClickListener {
                        when {
                            data?.chatstatus == "1" -> {
                                sendInvitation()
                            }
                            data?.chatstatus == "6" -> {
                                sendInvitation()
                            }
                            data?.chatstatus == "3" -> {
                                startActivity(
                                    Intent(this@MyProfileActivity, ChatActivity::class.java)
                                        .putExtra("id", id)
                                        .putExtra("name", data.name)
                                        .putExtra("url", data.image)
                                )
                            }
                        }
                    }
                    btnMyProfileBlock.setOnClickListener {
                        val tempText = btnMyProfileBlock.text.toString()
                        if (tempText=="UnBlock"){
                            blockFriend("2")
                        }
                        else{
                            blockFriend("1")
                        }
                    }
                    Utils.showRoundImage(this@MyProfileActivity, data?.image, ivMyProfilePhoto)
                    tvMyProfileName.text = data?.name


                    if (data?.skill!=null) {
                        tvMyProfileSkill.text = data.skill.toString()
                    }
                    else{
                        llMyProfileSkill.visibility = View.GONE
                    }

                    if (data?.exp!=null) {
                        tvMyProfileExp.text = data.exp.toString()
                    }
                    else{
                        llMyProfileExp.visibility = View.GONE
                    }

                    if (data?.detail!=null) {
                        tvMyProfileDetails.text = data.detail.toString()
                    }
                    else{
                        tvMyProfileDetails.visibility = View.GONE
                    }


                    tvMyProfilePostCount.text = data?.totalpost
                    tvMyProfileWallet.text = "$${data?.wallet}"
                    if (data?.status!!) {
                        getPosts(id)
                    }
                } else {
                    Utils.showToast(
                        this@MyProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<UserProfileModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@MyProfileActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    private fun getPosts(id: String?) {
        pbMyProfile.visibility = View.VISIBLE
        val call = ApiClient().getClient().getUserPosts(id)
        call.enqueue(object : Callback<UserPostsModel> {
            override fun onResponse(
                call: Call<UserPostsModel>?,
                response: Response<UserPostsModel>?
            ) {
                pbMyProfile.visibility = View.GONE
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    if (data?.status!!) {
                        rvMyProfile.visibility = View.VISIBLE
                        rvMyProfile.layoutManager = GridLayoutManager(this@MyProfileActivity, 3)
                        rvMyProfile.adapter = RecyclerView_MyProfile_Adapter(
                            data.response?.data,
                            this@MyProfileActivity
                        )
                    } else {
                        rvMyProfile.visibility = View.GONE
                        Utils.showToast(this@MyProfileActivity, "No Post Available!")
                    }
                } else {
                    Utils.showToast(
                        this@MyProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<UserPostsModel>?, t: Throwable?) {
                pbMyProfile.visibility = View.GONE
                Utils.showToast(this@MyProfileActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    private fun sendInvitation() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().inviteFriend(userIdd, Utils.getUserSess(), "invited")
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            )
            {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    Utils.showToast(this@MyProfileActivity, response.body()?.message)
                    if (response.body()?.status!!) {
                        getData(userIdd)
                    }
                } else {
                    Utils.showToast(
                        this@MyProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@MyProfileActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    private fun blockFriend(type:String) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().blockFriend(userIdd, Utils.getUserSess(), type)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            )
            {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    Utils.showToast(this@MyProfileActivity, response.body()?.message)
                    if (response.body()?.status!!) {
                        getData(userIdd)
                    }
                } else {
                    Utils.showToast(
                        this@MyProfileActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@MyProfileActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
