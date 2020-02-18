package com.duke.diskoverusosyal.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.wallet.DataItem
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.recyclerview_mywallet.view.*
import java.text.SimpleDateFormat

class RecyclerView_MyWallet_Adapter(private val list: List<DataItem?>?, private val context: Activity)
    : RecyclerView.Adapter<RecyclerView_MyWallet_Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_mywallet,parent,false)
        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try{list?.size!!}catch (e:Exception){0}
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data  = list?.get(position)

        val firstDate = data?.createdDate!!

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(firstDate)!!
        val tempDay = SimpleDateFormat("dd").format(date)
        val tempMonth = SimpleDateFormat("MMM").format(date)
        val tempYear = SimpleDateFormat("yyyy").format(date)
        val finalDate = "$tempMonth $tempDay, $tempYear"

        val amount = "$ "+data.amount

        holder.itemView.tvRvMyWalletDate.text = finalDate
        holder.itemView.tvRvMyWalletPrice.text = amount
        holder.itemView.tvRvMyWalletReason.text = data.reason
        if (data.cOrD=="1"){
            Utils.showImage(context,R.drawable.ic_down_arrow,holder.itemView.ivRvMyWalletArrow)
            holder.itemView.tvRvMyWalletPriceSymbol.text = "+"
            holder.itemView.tvRvMyWalletPriceSymbol.setTextColor(Color.parseColor("#56C75B"))
        }else{
            Utils.showImage(context,R.drawable.ic_up_arrow,holder.itemView.ivRvMyWalletArrow)
            holder.itemView.tvRvMyWalletPriceSymbol.text = "-"
            holder.itemView.tvRvMyWalletPriceSymbol.setTextColor(Color.parseColor("#EC3237"))
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