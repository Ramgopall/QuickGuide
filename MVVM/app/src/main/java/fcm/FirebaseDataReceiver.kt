package com.duke.visitormanagement

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.duke.visitormanagement.data.network.responses.invitationReplies.EnqueryDataItem
import com.duke.visitormanagement.ui.newVisitor.ClientResponseFragmentDialog
import com.google.gson.Gson

class FirebaseDataReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        for (key in intent?.extras!!.keySet()) {
//            val asdassd = intent.extras?.getString(key)
//            Log.d("aaaaaaaaaaa", "$key::: $asdassd")
//        }

        val title = intent?.extras?.getString("title")!!
        val message = intent.extras?.getString("body")!!
        val clickAction = intent.extras?.getString("click_action")!!
        val fcmData = intent.extras?.getString("fcmData")!!

        val myNotificationManager = MyNotificationManager(context!!)
        val intentt = Intent(clickAction)

        if (clickAction=="com.duke.visitormanagement.invitationReply"){
            val cInstace = ClientResponseFragmentDialog.getInstace()
            if (cInstace!=null){
                val gson = Gson()
                val tempGson = gson.fromJson(fcmData, EnqueryDataItem::class.java)
                if (tempGson.req_status=="Accepted"){
                    cInstace.getAccepted(tempGson.client_msg!!)
                }else{
                    cInstace.getRejected()
                }
            }
            else{
                showNotification(myNotificationManager,title,message,intentt)
            }
        }
        else{
            showNotification(myNotificationManager,title,message,intentt)
        }
    }

    private fun showNotification(myNotificationManager:MyNotificationManager,title:String,message:String,intent: Intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myNotificationManager.showNotificationOreo(title, message, intent)
        } else {
            myNotificationManager.showNotification(title, message, intent)
        }
    }
}