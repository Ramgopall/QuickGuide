package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.ChatActivity
import com.duke.diskoverusosyal.model.chatUsers.ResponseItem
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_chatusers.view.*

class RecyclerView_ChatUsers_Adapter(private val list: List<ResponseItem?>?, private val context: Activity) :
    RecyclerView.Adapter<RecyclerView_ChatUsers_Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chatusers, parent, false)
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
        if (data?.seen=="0") {
            holder.itemView.ivRvChatUsersUnread.visibility = View.VISIBLE
        }
        Utils.showRoundImage(
            context,
            data?.url,
            holder.itemView.ivRvChatUsersPic
        )
        holder.itemView.tvRvChatUsersName.text = data?.name
        holder.itemView.tvRvChatUsersDate.text = Utils.dateConvert(data?.updateddate)

        holder.itemView.llRvChatUsers.setOnClickListener {
            context.startActivity(
                Intent(context, ChatActivity::class.java)
                    .putExtra("id", data?.sess)
                    .putExtra("name", data?.name)
                    .putExtra("url", data?.url)
            )
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}