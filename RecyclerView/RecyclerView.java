												   MAIN CLASS
												
	val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setHasFixedSize(true)
        //recyclerView.setItemViewCacheSize(20);
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
	val adapter = RecyclerView_Adapter(list, this)
        recyclerView.adapter = adapter	
        
		// ClickListener call here
		recyclerView.addOnItemTouchListener( RecyclerViewListener(this,recyclerView, object : RecyclerViewListener.ClickListener
		{
            override fun onClick(view: View?, position: Int) {
               Toast.makeText(this@MoreFeedbacks,""+position,Toast.LENGTH_LONG).show()
            }

            override fun onLongClick(view: View?, position: Int) {

            }

        }
        ))
		
-------------------------------------------------------------------------------------------------------------------------------
													ADAPTER

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecyclerView_Adapter(list: List<TotalRating>, activity: Activity) : RecyclerView.Adapter<RecyclerView_Adapter.Myviewholder>() {

    var list1: List<TotalRating> = list
    var context2: Context = activity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Myviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_allstaff,parent,false)
        return  Myviewholder(view)
    }

    override fun getItemCount(): Int {
        return list1.size
    }

    
    override fun onBindViewHolder(holder: Myviewholder, position: Int) {

       var allReport_model: TotalRating = list1[position]
        holder.name.text = allReport_model.staffName
        
	}


    class Myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.textView_recyclerAdapter_name
     
    }


}