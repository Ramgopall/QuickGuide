package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.MyProfileActivity
import com.duke.diskoverusosyal.model.comments.DataItem
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_comments.view.*
import java.util.ArrayList

class RecyclerView_Commnets_Adapter(
    private val list: ArrayList<DataItem>?,
    private val context: Activity,
    private val userViewingOwnPostComments: Boolean,
    private val onDeleteItemClick: OnDeleteItemClick
) :
    RecyclerView.Adapter<RecyclerView_Commnets_Adapter.MyViewHolder>() {

    interface OnDeleteItemClick{
        fun onDeleteClick(commentId:String?,position: Int)
    }

    fun addMoreDataToList(result: DataItem) {
        list?.add(result)
        notifyItemInserted(list?.size?.minus(1)!!)
    }

    fun removeAt(position: Int) {
        list?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list?.size!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_comments, parent, false)
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
        if (userViewingOwnPostComments){
            holder.itemView.ivRvCommentsDelete.visibility = View.VISIBLE
        }
        if (data?.sess == Utils.getUserSess()){
            holder.itemView.ivRvCommentsDelete.visibility = View.VISIBLE
        }
        Utils.showRoundImage(
            context,
            data?.image,
            holder.itemView.ivRvCommentsProfile
        )
        holder.itemView.tvRvCommentsName.text = data?.name
        holder.itemView.tvRvCommentsMsg.text = data?.coment
        holder.itemView.tvRvCommentsDate.text = Utils.dateConvert(data?.date)

        holder.itemView.ivRvCommentsProfile.setOnClickListener {
            context.startActivity(
                Intent(context, MyProfileActivity::class.java)
                    .putExtra("id", data?.sess)
            )
        }
        holder.itemView.ivRvCommentsDelete.setOnClickListener {
            onDeleteItemClick.onDeleteClick(data?.id,position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}