package com.duke.visitormanagement.ui.newRequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.duke.visitormanagement.R
import com.duke.visitormanagement.data.network.responses.newRequests.EnqueryDataItem
import com.duke.visitormanagement.databinding.RvNewRequestsBinding

class NewRequestAdapter(private val list: ArrayList<EnqueryDataItem?>,private val newRequestsListener:NewRequestsListener) : RecyclerView.Adapter<NewRequestAdapter.NewRequestViewHolder>(){

    override fun getItemCount() = try{list.size}catch (e:Exception){0}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewRequestViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rv_new_requests, parent, false)
        )

    override fun onBindViewHolder(holder: NewRequestViewHolder, position: Int) {
        holder.recyclerviewMovieBinding.viewmodel = list[position]
        holder.recyclerviewMovieBinding.btnRvNewRequestsAccept.setOnClickListener {
            newRequestsListener.onRecyclerViewButtonClick(position,"accept",list[position])
        }
        holder.recyclerviewMovieBinding.btnRvNewRequestsReject.setOnClickListener {
            newRequestsListener.onRecyclerViewButtonClick(position,"reject",list[position])
        }
    }

    fun removeItem(position: Int) {
        if (list.size==1){
            newRequestsListener.onListGetEmpty()
        }
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    inner class NewRequestViewHolder(val recyclerviewMovieBinding: RvNewRequestsBinding)
        : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

}