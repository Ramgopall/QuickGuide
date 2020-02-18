package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.MyProfileActivity
import com.duke.diskoverusosyal.model.likeList.DataItem
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_likes.view.*

class RecyclerView_Likes_Adapter(private val list: List<DataItem?>?, private val context: Activity) :
    RecyclerView.Adapter<RecyclerView_Likes_Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_likes, parent, false)
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
        Utils.showRoundImage(
            context,
            data?.image,
            holder.itemView.ivRvLikesProfile
        )
        holder.itemView.tvRvLikesName.text = data?.name
        holder.itemView.tvRvLikesDate.text = Utils.dateConvert(data?.date)

        holder.itemView.llRvLikesMain.setOnClickListener {
            context.startActivity(
                Intent(context, MyProfileActivity::class.java)
                    .putExtra("id", data?.sess)
            )
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}