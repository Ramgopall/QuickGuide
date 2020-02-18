package com.duke.diskoverusosyal.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_Home_Adapter
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.VersionCheckModel
import com.duke.diskoverusosyal.model.home.HomeModel
import com.duke.diskoverusosyal.model.updateToken.UpdateTokenModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.retrofitData.ApiClientForCheckVersion
import com.duke.diskoverusosyal.utils.Utils
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    var PAGE_MAXNUM = 0
    var PAge_NUM = 1
    companion object {
        var homeAdapter: RecyclerView_Home_Adapter? = null
    }
    var layoutManager: LinearLayoutManager? = null
    private var loading = true
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    var positionAdapter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_home)
        //Utils.showRoundImage(this,Utils.getUserPhoto(),ivHomeProfile)
        onNavigationClicks()
        ivHomeProfile.setOnClickListener { openNavigationDrawer() }
        rlHomeNotification.setOnClickListener {
            ivHomeNotificationCount.visibility = View.GONE
            startActivity(
                Intent(this, NotificationActivity::class.java)
            )
        }
        fabHomeAdd.setOnClickListener {
            startActivity(Intent(this, AddNewPostActivity::class.java))
        }
        fabHomeMsg.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ChatUsersActivity::class.java))
        }
        ivHomeLogo.setOnClickListener {
            PAGE_MAXNUM = 0
            PAge_NUM = 1
            loading = true
            pastVisiblesItems = 0
            visibleItemCount = 0
            totalItemCount = 0
            //Utils.showRoundImage(this,Utils.getUserPhoto(),ivHomeProfile)
            getData()
        }
        ivHomeReferTop.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ReferAndEarnActivity::class.java))
        }
        ivHomeSearchTop.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
        }
        srlHome.setColorSchemeResources(R.color.colorBlack)
        srlHome.setOnRefreshListener {
            PAGE_MAXNUM = 0
            PAge_NUM = 1
            loading = true
            pastVisiblesItems = 0
            visibleItemCount = 0
            totalItemCount = 0
            //Utils.showRoundImage(this,Utils.getUserPhoto(),ivHomeProfile)
            getData()
        }
        getData()
        isUpdateAvalable()
        isUserBloacked()
        updateToken()
    }

    fun openNavigationDrawer() {
        if (!dlHome?.isDrawerOpen(GravityCompat.START)!!) {
            dlHome.openDrawer(GravityCompat.START)
        }
    }

    fun onNavigationClicks() {
        Utils.showRoundImage(this, Utils.getUserPhoto(), llNdPhoto)
        llNdName.text = Utils.getUserName()

        llNdName.setOnClickListener {
            startActivity(
                Intent(this@HomeActivity, MyProfileActivity::class.java)
                    .putExtra("id", Utils.getUserSess())
            )
        }
        llNdPhoto.setOnClickListener {
            startActivity(
                Intent(this@HomeActivity, MyProfileActivity::class.java)
                    .putExtra("id", Utils.getUserSess())
            )
        }
        llNdProfile.setOnClickListener {
            startActivity(
                Intent(this@HomeActivity, MyProfileActivity::class.java)
                    .putExtra("id", Utils.getUserSess())
            )
        }
        llNdMessages.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ChatUsersActivity::class.java))
        }
        llNdWallet.setOnClickListener {
            startActivity(Intent(this@HomeActivity, MyWalletActivity::class.java))
        }
        llNdSearch.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
        }
        llNdNotification.setOnClickListener {
            ivHomeNotificationCount.visibility = View.GONE
            startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
        }
        llNdPrices.setOnClickListener {
            startActivity(
                Intent(this@HomeActivity, PackageActivity::class.java)
                    .putExtra("checkLimitReached", false)
            )
        }
        llNdRefer.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ReferAndEarnActivity::class.java))
        }
        llNdHelp.setOnClickListener {
            startActivity(Intent(this@HomeActivity, HelpSupportActivity::class.java))
        }
        llNdLogout.setOnClickListener {
            Utils.clearUser()
            startActivity(
                Intent(this@HomeActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

    }

    fun closeNavigationDrawer() {
        dlHome?.closeDrawers()
    }

    private fun getData() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getHomeData(Utils.getUserSess(), PAge_NUM)
        call.enqueue(object : Callback<HomeModel> {
            override fun onResponse(
                call: Call<HomeModel>?,
                response: Response<HomeModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        if (response.body()?.status!!) {
                            if (response.body()?.totalnotseen != "0" && response.body()?.totalnotseen != "") {
                                ivHomeNotificationCount.visibility = View.VISIBLE
                                ivHomeNotificationCount.text = response.body()?.totalnotseen
                            }

                            ivHomeBanner.visibility = View.VISIBLE
                            ivHomeHideBanner.visibility = View.VISIBLE

                            Utils.showImage(this@HomeActivity, response.body()?.bonanza, ivHomeBanner)
                            ivHomeBanner.setOnClickListener {
                                startActivity(
                                    Intent(this@HomeActivity,BannerTermsActivity::class.java)
                                        .putExtra("Desc",response.body()?.detail)
                                )
                            }
                            ivHomeHideBanner.setOnClickListener {
                                ivHomeBanner.visibility = View.GONE
                                ivHomeHideBanner.visibility = View.GONE
                            }

                            val data = response.body()?.response?.data
                            layoutManager = LinearLayoutManager(this@HomeActivity)
                            rvHome.layoutManager = layoutManager
                            rvHome.setMediaObjects(data)
                            homeAdapter = RecyclerView_Home_Adapter(data, initGlide(),rvHome)
                            rvHome.adapter = homeAdapter
                            //rvHome.isNestedScrollingEnabled = false
                            rvHome.smoothScrollBy(0, 1)
                            rvHome.smoothScrollBy(0, -1)

                            PAGE_MAXNUM = if (response.body()?.totalpages!!.toInt() == 0) {
                                1
                            } else {
                                response.body()?.totalpages!!
                            }
                            PAge_NUM += 1

                        } else {
                            Utils.showToast(this@HomeActivity, response.body()?.message)
                        }
                    } else {
                        Utils.showToast(
                            this@HomeActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<HomeModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@HomeActivity, resources.getString(R.string.api_failure))
            }
        })

        srlHome.isRefreshing = false
        rvHome.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1))  //check for scroll down
                    {
                        visibleItemCount = layoutManager?.childCount!!
                        totalItemCount = layoutManager?.itemCount!!
                        pastVisiblesItems = layoutManager?.findFirstVisibleItemPosition()!!

                        if (loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false
                                getMoreData()
                                //Do pagination.. i.e. fetch new data
                            }
                        }
                    }

                }
            }
        )
    }

    private fun getMoreData() {
        pbHomeBottom.visibility = View.VISIBLE
        val call = ApiClient().getClient().getHomeData(Utils.getUserSess(), PAge_NUM)
        call.enqueue(object : Callback<HomeModel> {
            override fun onResponse(
                call: Call<HomeModel>?,
                response: Response<HomeModel>?
            ) {
                try {
                    pbHomeBottom.visibility = View.GONE
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()?.response?.data
                        homeAdapter?.addMoreDataToList(data)
                        rvHome.smoothScrollBy(0, 100)
                        PAge_NUM += 1
                        if (PAge_NUM <= PAGE_MAXNUM) {
                            loading = true
                        }

                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "No Response from server!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.d("bottomLoader", "" + e.printStackTrace().toString())
                }
            }

            override fun onFailure(call: Call<HomeModel>?, t: Throwable?) {
                pbHomeBottom.visibility = View.GONE
            }
        })


    }

    private fun isUpdateAvalable() {
        val call = ApiClientForCheckVersion().getClient().checkAppVersion(Utils.getUserSess()!!,"v3.2")
        call.enqueue(object : Callback<VersionCheckModel> {
            override fun onResponse(
                call: Call<VersionCheckModel>?,
                response: Response<VersionCheckModel>?
            ) {
                try {
                    if (response?.body()?.status!!) {
                       if (response.body()?.message == "v3.2"){
                           Log.d("TAG","App is up to date")
                       }
                        else{
                           val dialog = AlertDialog.Builder(this@HomeActivity)
                           dialog.setTitle("New Update Available")
                           dialog.setMessage("Update ${response.body()?.vcode} is available to download. Downloading the latest update you will get the latest features, improvements and bug fixes of Sosyal.")
                           dialog.setNegativeButton("Remind Later") { _, _ -> }
                           dialog.setPositiveButton("Update") { _, _ ->
                               dialog.create().dismiss()
                               val i = Intent(Intent.ACTION_VIEW)
                               i.data = Uri.parse("https://play.google.com/store/apps/details?id=com.duke.diskoverusosyal")
                               startActivity(i)
                           }
                           dialog.setCancelable(false)
                           dialog.create().show()
                       }
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<VersionCheckModel>?, t: Throwable?) {

            }
        })
    }

    private fun isUserBloacked() {
        val call = ApiClient().getClient().isUserBlocked(Utils.getUserSess()!!)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                try {
                    if (!response?.body()?.status!!) {
                        Utils.clearUser()
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {

            }
        })
    }

    fun updateToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val newToken = instanceIdResult.token
            Log.d("tokennnnn", newToken)
            val call = ApiClient().getClient().updateToken(Utils.getUserSess(), newToken)
            call.enqueue(object : Callback<UpdateTokenModel> {

                override fun onResponse(
                    call: Call<UpdateTokenModel>?,
                    response: Response<UpdateTokenModel>?
                ) {
                }

                override fun onFailure(call: Call<UpdateTokenModel>?, t: Throwable?) {}
            })
        }
    }

    override fun onResume() {
        Utils.showRoundImage(this, Utils.getUserPhoto(), llNdPhoto)
        //Utils.showRoundImage(this,Utils.getUserPhoto(),ivHomeProfile)
        if (rvHome != null && layoutManager !=  null) {
            val temp = if (layoutManager?.findFirstCompletelyVisibleItemPosition()!! != -1){
                homeAdapter?.getItemViewType(layoutManager?.findFirstCompletelyVisibleItemPosition()!!)
            }
            else{
                -1
            }
            rvHome.onResumePlayer(
                temp!!
            )
        }
        super.onResume()
    }

    override fun onStop() {
        if (rvHome != null) {
            rvHome.onPausePlayer()
        }
        super.onStop()
    }

    private fun initGlide(): RequestManager {
        val options = RequestOptions()

        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

    override fun onDestroy() {
        if (rvHome != null) {
            rvHome.releasePlayer()
        }
        super.onDestroy()
    }
}

