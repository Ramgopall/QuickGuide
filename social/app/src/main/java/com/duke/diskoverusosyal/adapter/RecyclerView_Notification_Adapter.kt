package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.activity.ChatUsersActivity
import com.duke.diskoverusosyal.activity.MyWalletActivity
import com.duke.diskoverusosyal.activity.OfferViewActivity
import com.duke.diskoverusosyal.activity.SinglePostActivity
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.notification.DataItem
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_notification.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerView_Notification_Adapter(private val list: List<DataItem?>?, private val context: Activity, private val onItemClicks: OnItemClicks)
    : RecyclerView.Adapter<RecyclerView_Notification_Adapter.MyViewHolder>() {

    interface OnItemClicks{
        fun refreshRecycler()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_notification,parent,false)
        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try{list?.size!!}catch (e:Exception){0}
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data  = list?.get(position)
        if (data?.seen=="0") {
            holder.itemView.ivRvNotificationUnread.visibility = View.VISIBLE
        }
        if (data?.url!="") {
            Utils.showRoundImage(context, data?.url, holder.itemView.ivRvNotificationPic)
        }
        else {
            if (data.type =="wallet"){
                Utils.showImage(context, R.drawable.ic_notification_wallet_color, holder.itemView.ivRvNotificationPic)
            }
            else{
                Utils.showImage(context, R.drawable.ic_reject, holder.itemView.ivRvNotificationPic)
            }
            //holder.itemView.ivRvNotificationPic.visibility = View.VISIBLE
        }

        holder.itemView.tvRvNotificationName.text = data?.notification
        holder.itemView.tvRvNotificationDate.text = Utils.dateConvert(data?.date)
        holder.itemView.llRvNotification.setOnClickListener {
            when {
                data?.type =="post" -> context.startActivity(
                    Intent(context, SinglePostActivity::class.java)
                        .putExtra("id",data.pid)
                )
                data?.type =="offer" -> context.startActivity(
                    Intent(context, OfferViewActivity::class.java)
                        .putExtra("id",data.pid)
                )
                data?.type =="wallet" -> context.startActivity(
                    Intent(context, MyWalletActivity::class.java)
                )
                data?.type =="chat" -> {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setCancelable(false)
                    dialog.setTitle("Invitation Request")
                    dialog.setMessage(data.notification)
                    dialog.setPositiveButton("Accept"){_,_ ->
                        sendInvitation(data.pid!!,data.nid!!,"accepted")
                        dialog.create().dismiss()
                    }
                    dialog.setNegativeButton("Reject"){_,_ ->
                        sendInvitation(data.pid!!,data.nid!!,"rejected")
                        dialog.create().dismiss()
                    }
                    dialog.create().show()

                }
                data?.type =="chatr" -> {
                    context.startActivity(
                        Intent(context, ChatUsersActivity::class.java)
                    )
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun sendInvitation(pid:String,nid:String,status:String) {
        Utils.showLoadingDialog(context)
        val call = ApiClient().getClient().inviteReply(Utils.getUserSess(),pid,nid,status)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            )
            {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    Utils.showToast(context, response.body()?.message)
                    if (response.body()?.status!!){
                        if (status=="accepted"){
                            context.startActivity(
                                Intent(context, ChatUsersActivity::class.java)
                            )
                        }
                        else{
                            onItemClicks.refreshRecycler()
                        }
                    }
                } else {
                    Utils.showToast(
                        context,
                        context.resources.getString(R.string.no_response)
                    )
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(context, context.resources.getString(R.string.api_failure))
            }
        })
    }

}