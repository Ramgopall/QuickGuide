package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.CommentsActivity
import com.duke.diskoverusosyal.activity.LikesActivity
import com.duke.diskoverusosyal.activity.MyProfileActivity
import com.duke.diskoverusosyal.activity.ZoomActivity
import com.duke.diskoverusosyal.custom.ExoPlayerRecyclerView
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.home.DataItem
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_home_video.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerView_Home_Adapter(
    private val list: ArrayList<DataItem?>?,
    private val requestManager: RequestManager,
    private val rvHome: ExoPlayerRecyclerView
) : RecyclerView.Adapter<RecyclerView_Home_Adapter.PlayerViewHolder>() {

    public class PlayerViewHolder(public val parent: View) : RecyclerView.ViewHolder(parent) {

        public var mediaContainer: FrameLayout
        var mediaCoverImage: ImageView
        var volumeControl: ImageView
        var ivPlayVideo: ImageView
        var progressBar: ProgressBar
        var requestManager: RequestManager? = null

        init {
            mediaContainer = parent.findViewById(R.id.mediaContainer)
            mediaCoverImage = parent.findViewById(R.id.ivMediaCoverImage)
            progressBar = parent.findViewById(R.id.progressBar)
            volumeControl = parent.findViewById(R.id.ivVolumeControl)
            ivPlayVideo = parent.findViewById(R.id.ivPlayVideo)
        }

        fun onBind(mediaObject: DataItem, requestManager: RequestManager) {
            this.requestManager = requestManager
            parent.tag = this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_home_video, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            list?.size!!
        } catch (e: Exception) {
            0
        }
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val data = list?.get(position)
        holder.onBind(data!!, requestManager)
        holder.itemView.tvRvHomeDesc.text = data.desc

        if (data.type == "1") {
            holder.itemView.ivVolumeControl.visibility = View.INVISIBLE
            holder.itemView.ivPlayVideo.visibility = View.GONE
            Utils.showImage(holder.itemView.context, data.url, holder.itemView.ivMediaCoverImage)
        } else {
            holder.itemView.ivVolumeControl.visibility = View.VISIBLE
            Utils.showThumbnailFromUrl(holder.itemView.context, data.url, holder.itemView.ivMediaCoverImage)
        }

        Utils.showRoundImage(holder.itemView.context, data.image, holder.itemView.ivRvHomeProfile)
        holder.itemView.tvRvHomeLikes.text = data.totallike + " Likes"
        holder.itemView.tvRvHomeComments.text = data.totalcoment + " Comments"
        holder.itemView.tvRvHomeName.text = data.name
        holder.itemView.tvRvHomeDate.text = if (data.date != "") {
            Utils.dateConvert(data.date)
        } else {
            ""
        }

        holder.itemView.mediaContainer.setOnClickListener {
            if (data.type == "2") {
                rvHome.onRestartPlayer()
            }
        }
        holder.itemView.ivPlayVideo.setOnClickListener {
            if (data.type == "2") {
                rvHome.onRestartPlayer()
            }
        }
        holder.itemView.ivVolumeControl.setOnClickListener {
            if (data.type == "2") {
                rvHome.toggleVolume()
            }
        }
        holder.itemView.ivMediaCoverImage.setOnClickListener {
            if (data.type == "1") {
                holder.itemView.context.startActivity(
                    Intent(holder.itemView.context, ZoomActivity::class.java)
                        .putExtra("image", data.url)
                )
            }
            else{
                rvHome.onRestartPlayer()
            }
        }
        holder.itemView.tvRvHomeName.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, MyProfileActivity::class.java)
                    .putExtra("id", data.sess)
            )
        }
        holder.itemView.ivRvHomeProfile.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, MyProfileActivity::class.java)
                    .putExtra("id", data.sess)
            )
        }
        holder.itemView.tvRvHomeLikes.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, LikesActivity::class.java)
                    .putExtra("id", data.id)
            )
        }
        holder.itemView.ivRvHomeComment.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, CommentsActivity::class.java)
                    .putExtra("id", data.id)
                    .putExtra("sess", data.sess)
            )
        }
        holder.itemView.tvRvHomeComments.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, CommentsActivity::class.java)
                    .putExtra("id", data.id)
                    .putExtra("sess", data.sess)
            )
        }

        holder.itemView.cbRvHomeLike.isChecked = data.ownlike == "1"

        holder.itemView.cbRvHomeLike.setOnClickListener {
            if (holder.itemView.cbRvHomeLike.isChecked) {
                val likee = data.totallike?.toInt()?.plus(1)
                data.totallike = likee.toString()
                data.ownlike = "1"
                holder.itemView.tvRvHomeLikes.text = likee.toString() + " Likes"
                likePost(data.id, "1")
            } else {
                val likee = data.totallike?.toInt()?.minus(1)
                data.ownlike = "0"
                data.totallike = likee.toString()
                holder.itemView.tvRvHomeLikes.text = likee.toString() + " Likes"
                likePost(data.id, "2")
            }
        }

        holder.itemView.ivRvHomeShare.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "DiskoverU Sosyal")
            i.putExtra(Intent.EXTRA_TEXT, data.share_url)
            holder.itemView.context.startActivity(Intent.createChooser(i, "choose one"))
        }

    }

    fun addMoreDataToList(result: ArrayList<DataItem?>?) {
        for (i in 0 until result?.size!!) {
            list?.add(result[i])
        }
        notifyDataSetChanged()
    }

    fun updateCommentCount(count:String?,postId:String) {
//        Thread(Runnable {
            for (i in 0 until list?.size!!) {
                if (list[i]?.id == postId){
                    list[i]?.totalcoment = count
                }
            }
//            context.runOnUiThread {
                notifyDataSetChanged()
//            }
//        }).start()
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return list?.get(position)?.type?.toInt()!!
    }
}