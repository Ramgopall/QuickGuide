package com.duke.diskoverusosyal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.Message
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.rv_chat.view.*


class ChatBoxAdapter(
    private val MessageList: ArrayList<Message>,
    private val toid: String?
) : RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun addMoreDataToList(result: Message) {
        MessageList.add(result)
        notifyItemInserted(MessageList.size - 1)
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_chat, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val m = MessageList[position]
            if (m.type=="0"){
                holder.itemView.my_message.visibility = View.VISIBLE
                holder.itemView.my_message_date.visibility = View.VISIBLE
                holder.itemView.messagee.visibility = View.GONE
                holder.itemView.messagee_date.visibility = View.GONE
                holder.itemView.my_message.text = m.message
                holder.itemView.my_message_date.text = Utils.dateConvert(m.dated)
            }
            else{
                if (m.myid.equals(toid) && m.toid.equals(Utils.getUserSess())) {
                    holder.itemView.messagee.visibility = View.VISIBLE
                    holder.itemView.messagee_date.visibility = View.VISIBLE
                    holder.itemView.my_message.visibility = View.GONE
                    holder.itemView.my_message_date.visibility = View.GONE
                    holder.itemView.messagee.text = m.message
                    holder.itemView.messagee_date.text = Utils.dateConvert(m.dated)
                }
            }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}