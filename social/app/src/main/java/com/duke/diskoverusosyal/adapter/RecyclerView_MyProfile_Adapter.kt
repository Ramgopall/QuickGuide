package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.SinglePostActivity
import com.duke.diskoverusosyal.model.userPosts.DataItem
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_myprofile.view.*


class RecyclerView_MyProfile_Adapter(private val list: List<DataItem?>?, private val context: Activity) :
    RecyclerView.Adapter<RecyclerView_MyProfile_Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_myprofile, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            list?.size!!
        } catch (e: Exception) {
            0
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list?.get(position)
        if (data?.type=="1"){
            holder.itemView.ivRvMyProfileVideoIcon.visibility = View.GONE
            Utils.showCenterCropImage(context, data.url, holder.itemView.ivRvMyProfile)
        }
        else{
            holder.itemView.ivRvMyProfileVideoIcon.visibility = View.VISIBLE
            Utils.showThumbnailFromUrl(context, data?.url, holder.itemView.ivRvMyProfile)
        }
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, SinglePostActivity::class.java)
                .putExtra("id",data?.id)
            )
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}