package com.duke.diskoverusosyal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.packageList.DataItem
import kotlinx.android.synthetic.main.recyclerview_package.view.*

class RecyclerView_Package_Adapter(private val list: List<DataItem?>?, private val onItemClickListener:OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView_Package_Adapter.MyViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(id:String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_package,parent,false)
        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try{list?.size!!}catch (e:Exception){0}
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data  = list?.get(position)
        holder.itemView.tvRvPackageTitle.text = data?.name
        holder.itemView.tvRvPackagePrice.text = "$ "+data?.price
        if (data?.vid=="0"){
            holder.itemView.tv_recyclerViewPackages_desc.text = "Unlimited Video posting"
        }else{
            holder.itemView.tv_recyclerViewPackages_desc.text = "Enjoy ${data?.vid} Video posting"
        }

        holder.itemView.tv_recyclerViewPackages_subDesc.text = "For ${data?.days} days"

        holder.itemView.btn_recyclerViewPackages_buy.setOnClickListener {
            onItemClickListener.onItemClick(data?.id)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}