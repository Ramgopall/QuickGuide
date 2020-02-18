package com.duke.diskoverusosyal.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.singlePost.SinglePostModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.activity_single_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.exoplayer2.DefaultLoadControl

class SinglePostActivity : AppCompatActivity() {

    var videoPlayer: SimpleExoPlayer? = null
    var isIntented: Boolean? = false

    var id : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        if (!Utils.isUserLogin()!!) {
            startActivity(
                Intent(this@SinglePostActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        ivSinglePostBack.setOnClickListener { onBackPressed() }
        val data = intent.data
        id = if (data != null) {
            isIntented = true
            data.getQueryParameter("id")
        } else {
            intent?.getStringExtra("id")
        }
    }

    override fun onResume() {
        getPosts()
        super.onResume()
    }

    private fun getPosts() {
        pbSinglePost.visibility = View.VISIBLE
        val call = ApiClient().getClient().getSinglePosts(id, Utils.getUserSess())
        call.enqueue(object : Callback<SinglePostModel> {
            override fun onResponse(
                call: Call<SinglePostModel>?,
                response: Response<SinglePostModel>?
            ) {
                pbSinglePost.visibility = View.GONE
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    if (data?.status!!) {
                        llSinglePost.visibility = View.VISIBLE
                        Utils.showRoundImage(
                            this@SinglePostActivity,
                            data.image,
                            ivSinglePostProfile
                        )
                        tvSinglePostLikes.text = data.totallike + " Likes"
                        tvSinglePostComments.text = data.totalcoment + " Comments"
                        tvSinglePostName.text = data.name
                        tvSinglePostDate.text = Utils.dateConvert(data.date)
                        tvSinglePostDesc.text = data.desc
                        tvSinglePostLikes.setOnClickListener {
                            startActivity(
                                Intent(this@SinglePostActivity, LikesActivity::class.java)
                                    .putExtra("id", id)
                            )
                        }
                        ivSinglePostComment.setOnClickListener {
                            startActivity(
                                Intent(this@SinglePostActivity, CommentsActivity::class.java)
                                    .putExtra("id", id)
                                    .putExtra("sess", data.sess)
                            )
                        }
                        tvSinglePostComments.setOnClickListener {
                            startActivity(
                                Intent(this@SinglePostActivity, CommentsActivity::class.java)
                                    .putExtra("id", id)
                                    .putExtra("sess", data.sess)
                            )
                        }
                        ivSinglePostShare.setOnClickListener {
                            val i = Intent(Intent.ACTION_SEND)
                            i.type = "text/plain"
                            i.putExtra(Intent.EXTRA_SUBJECT, "DiskoverU Sosyal")
                            i.putExtra(Intent.EXTRA_TEXT, data.share_url)
                            startActivity(Intent.createChooser(i, "choose one"))
                        }
                        tvSinglePostName.setOnClickListener {
                            startActivity(
                                Intent(this@SinglePostActivity, MyProfileActivity::class.java)
                                    .putExtra("id", data.sess)
                            )
                        }
                        ivSinglePostProfile.setOnClickListener {
                            startActivity(
                                Intent(this@SinglePostActivity, MyProfileActivity::class.java)
                                    .putExtra("id", data.sess)
                            )
                        }

                        cbSinglePostLike.isChecked = data.ownlike == "1"

                        if (data.type == "1") {
                            ivSinglePostPost.visibility = View.VISIBLE
                            Utils.showImage(this@SinglePostActivity, data.url, ivSinglePostPost)
                            ivSinglePostPost.setOnClickListener {
                                if (data.type == "1") {
                                    startActivity(
                                        Intent(this@SinglePostActivity, ZoomActivity::class.java)
                                            .putExtra("image", data.url)
                                    )
                                }
                            }
                        } else {
                            rlSinglePostVideo.visibility = View.VISIBLE
                            vvSinglePost.visibility = View.VISIBLE
                            pbSinglePostVideo.visibility = View.VISIBLE

                            val renderersFactory = DefaultRenderersFactory(this@SinglePostActivity)

                            val loadControl = DefaultLoadControl.Builder()
                                .setAllocator(DefaultAllocator(true, 16))
                                .setBufferDurationsMs(
                                    2000,
                                    5000,
                                    1500,
                                    2000
                                )
                                .setTargetBufferBytes(-1)
                                .setPrioritizeTimeOverSizeThresholds(true)
                                .createDefaultLoadControl()

                            val trackSelectorDef = DefaultTrackSelector()
                            videoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory,
                                trackSelectorDef,loadControl
                            )

                            val defdataSourceFactory = DefaultDataSourceFactory(
                                this@SinglePostActivity,
                                "DiskoverU Sosyal"
                            )

                            val uriOfContentUrl = Uri.parse(data.url)
                            val mediaSource = ExtractorMediaSource.Factory(defdataSourceFactory)
                                .createMediaSource(uriOfContentUrl)

                            videoPlayer?.prepare(mediaSource)
                            videoPlayer?.playWhenReady = true
                            //videoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
                            vvSinglePost.useController = false
                            vvSinglePost.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            vvSinglePost.player = videoPlayer



                            videoPlayer?.addListener(object : Player.EventListener {
                                override fun onTimelineChanged(
                                    timeline: Timeline,
                                    manifest: Any?,
                                    reason: Int
                                ) {

                                }

                                override fun onTracksChanged(
                                    trackGroups: TrackGroupArray,
                                    trackSelections: TrackSelectionArray
                                ) {

                                }

                                override fun onLoadingChanged(isLoading: Boolean) {

                                }

                                override fun onPlayerStateChanged(
                                    playWhenReady: Boolean,
                                    playbackState: Int
                                ) {
                                    when (playbackState) {

                                        Player.STATE_BUFFERING -> {
                                            //Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                                            if (pbSinglePostVideo != null) {
                                                pbSinglePostVideo.visibility = View.VISIBLE
                                            }
                                        }
                                        Player.STATE_ENDED -> {
                                            //Log.d(TAG, "onPlayerStateChanged: Video ended.")
                                            videoPlayer?.seekTo(0)
                                        }
                                        Player.STATE_IDLE -> {
                                        }
                                        Player.STATE_READY -> {
                                            //Log.e(TAG, "onPlayerStateChanged: Ready to play.")
                                            if (pbSinglePostVideo != null) {
                                                pbSinglePostVideo.setVisibility(View.GONE)
                                            }
//                                            if (!isVideoViewAdded) {
//                                                addVideoView()
//                                            }
                                        }
                                        else -> {
                                        }
                                    }
                                }

                                override fun onRepeatModeChanged(repeatMode: Int) {

                                }

                                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

                                }

                                override fun onPlayerError(error: ExoPlaybackException) {

                                }

                                override fun onPositionDiscontinuity(reason: Int) {

                                }

                                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

                                }

                                override fun onSeekProcessed() {

                                }
                            })

                        }
                        cbSinglePostLike.setOnClickListener {
                            if (cbSinglePostLike.isChecked) {
                                val likee = data.totallike?.toInt()?.plus(1)
                                data.totallike = likee.toString()
                                tvSinglePostLikes.text = likee.toString() + " Likes"
                                likePost(id, "1")
                            } else {
                                val likee = data.totallike?.toInt()?.minus(1)
                                data.totallike = likee.toString()
                                tvSinglePostLikes.text = likee.toString() + " Likes"
                                likePost(id, "2")
                            }
                        }

                        if (Utils.getUserSess() == data.sess) {
                            ivSinglePostDelete.visibility = View.VISIBLE
                            ivSinglePostDelete.setOnClickListener {
                                val dialod = AlertDialog.Builder(this@SinglePostActivity)
                                dialod.setTitle("Alert!")
                                dialod.setMessage("Are you sure you want to delete this post?")
                                dialod.setCancelable(false)
                                dialod.setNegativeButton("No") { _, _ ->

                                }
                                dialod.setPositiveButton("Yes") { _, _ ->
                                    deletePost(id)
                                    dialod.create().dismiss()
                                }
                                dialod.create().show()

                            }
                        }
                    } else {
                        Utils.showToast(this@SinglePostActivity, data.message)
                    }
                } else {
                    Utils.showToast(
                        this@SinglePostActivity,
                        resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<SinglePostModel>?, t: Throwable?) {
                pbSinglePost.visibility = View.GONE
                Utils.showToast(this@SinglePostActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    fun likePost(id: String?, like: String) {
        val call = ApiClient().getClient().likePost(id, Utils.getUserSess(), like)
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

    fun deletePost(id: String?) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().deletePost(id, Utils.getUserSess())
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()
                    if (data?.status!!) {
                        Utils.showToast(
                            this@SinglePostActivity,
                            data.message
                        )
                        onBackPressed()
                    } else {
                        Utils.showToast(
                            this@SinglePostActivity,
                            data.message
                        )
                    }
                } else {
                    Utils.showToast(
                        this@SinglePostActivity,
                        resources.getString(R.string.no_response)
                    )
                }

            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@SinglePostActivity, resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onBackPressed() {
        if (isIntented!!) {
            startActivity(
                Intent(this@SinglePostActivity, HomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        } else {
            finish()
        }
    }

    override fun onStop() {
        if (videoPlayer != null) {
            vvSinglePost.setPlayer(null)
            videoPlayer?.release()
            videoPlayer = null
        }
        super.onStop()
    }
}
