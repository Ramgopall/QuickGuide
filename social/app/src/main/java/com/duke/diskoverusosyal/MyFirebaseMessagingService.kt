package com.duke.diskoverusosyal

import android.content.Intent
import android.os.Build
import android.util.Log
import com.duke.diskoverusosyal.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("NEW_TOKEN", s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
            try {
                val json = JSONObject(remoteMessage.data.toString())
                sendNotification(json)

            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }

    private fun sendNotification(json: JSONObject) {

        val data: JSONObject?
        try {
            data = json.getJSONObject("data")
            //parsing json data
            val title = data!!.getString("title")
            val message = data.getString("message")
            val click_action = data.getString("click_action")

            //creating MyNotificationManager object
            val myNotificationManager = MyNotificationManager(applicationContext)

            val intent = Intent(click_action)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                myNotificationManager.showNotificationOreo(title, message, intent)
            } else {
                myNotificationManager.showNotification(title, message, intent)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}

