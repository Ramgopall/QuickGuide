package com.duke.diskoverusosyal.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duke.diskoverusosyal.MyEditText.KeyBoardInputCallbackListener
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.ChatBoxAdapter
import com.duke.diskoverusosyal.model.Message
import com.duke.diskoverusosyal.model.chatHistory.ChatHistoryModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_chat.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI
import java.net.URISyntaxException


class ChatActivity : AppCompatActivity() {

    var toid:String? = ""
    var isConnected = false
    var MessageList = arrayListOf<Message>()
    var chatBoxAdapter: ChatBoxAdapter? = null
    var mWebSocketClient: WebSocketClient? = null

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toid = intent?.getStringExtra("id")
        val name = intent?.getStringExtra("name")
        val url = intent?.getStringExtra("url")
        Utils.showRoundImage(this,url,ivChatPhoto)
        tvChatName.text = name
        ivChatBack.setOnClickListener { finish() }

        val linearLayoutManager = LinearLayoutManager(this@ChatActivity)
        linearLayoutManager.stackFromEnd = true
        myRecylerView?.layoutManager = linearLayoutManager
        myRecylerView?.itemAnimator = DefaultItemAnimator()


        etChatMsg.setKeyBoardInputCallbackListener(KeyBoardInputCallbackListener { inputContentInfo, flags, opts ->
            //you will get your gif/png/jpg here in opts bundle
            Log.d("aaaaaaaaaaa","yaha call hua")
        })
        btnChatSend?.setOnClickListener {
            val message = etChatMsg?.text.toString().trim { it <= ' ' }
            if(message==""){
                etChatMsg.requestFocus()
                etChatMsg.error = "Can't send empty text"
                return@setOnClickListener
            }
            sendMessage(message)
        }
        getPosts()
    }

    private fun getPosts() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getChatHistory(Utils.getUserSess(),toid)
        call.enqueue(object : Callback<ChatHistoryModel> {
            override fun onResponse(
                call: Call<ChatHistoryModel>?,
                response: Response<ChatHistoryModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            MessageList = data.response!!
                            chatBoxAdapter = ChatBoxAdapter(MessageList, toid)
                            myRecylerView?.adapter = chatBoxAdapter

                        } else {
                            chatBoxAdapter = ChatBoxAdapter(MessageList, toid)
                            myRecylerView?.adapter = chatBoxAdapter
                        }
                    } else {
                        Utils.showToast(
                            this@ChatActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                    connectWebSocket("")
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<ChatHistoryModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@ChatActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
    }

    private fun connectWebSocket(tempString :String) {
        val uri: URI
        try {
            uri = URI("ws://192.105.321.112:0002/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }

        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.d("Websocket", "Opened")
                if (tempString=="") {
                    mWebSocketClient?.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL)
                }
                else{
                    mWebSocketClient?.send(tempString)
                    addMessage(
                        Utils.getUserSess()!!,
                        toid!!,
                        tempString,
                        "0",
                        "text",
                        Utils.getCurrentTimeStamp(),
                        "0",
                        "connected"
                    )
                    etChatMsg.setText("")
                }
                isConnected = true
            }

            override fun onMessage(message: String) {
                isConnected = true
                Log.d("Websocket", message)
                val sendText = JSONObject(message)
                val tempConnectionType = sendText.getString("connection_type")
                if (tempConnectionType == "connected") {
                    val tempMyId = sendText.getString("myid")
                    val tempToId = sendText.getString("toid")
                    val tempMessageType = sendText.getString("message_type")
                    val tempDated = sendText.getString("dated")
                    val tempSeen = sendText.getString("seen")
                    val tempMessage = sendText.getString("message")
                    if (tempMyId!="null" && tempMyId == toid && tempToId == Utils.getUserSess()) {
                        runOnUiThread {
                            addMessage(tempMyId,tempToId, tempMessage, "1", tempMessageType,tempDated,tempSeen,tempConnectionType)
                        }
                    }
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.d("Websocket", "Closed $s")
                isConnected = false
            }

            override fun onError(e: Exception) {
                Log.d("Websocket", "Error " + e.message)
                isConnected = false
            }
        }
        mWebSocketClient?.connect()
    }

    private fun sendMessage(message:String) {
        val sendText = JSONObject()
        sendText.put("myid", Utils.getUserSess()!!)
        sendText.put("toid", toid)
        sendText.put("message_type", "text")
        sendText.put("dated", Utils.getCurrentTimeStamp())
        sendText.put("seen", "0")
        sendText.put("message", message)
        sendText.put("connection_type", "connected")

        if (isConnected) {
            mWebSocketClient?.send(sendText.toString())
            addMessage(
                Utils.getUserSess()!!,
                toid!!,
                message,
                "0",
                "text",
                Utils.getCurrentTimeStamp(),
                "0",
                "connected"
            )
            etChatMsg.setText("")
        } else {
            try {
                connectWebSocket(sendText.toString())
            } catch (e: Exception) {
                Toast.makeText(this@ChatActivity, "Please Check your Connection", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun addMessage(
        tempMyId: String,
        tempToId: String,
        tempMessage: String,
        type: String,
        tempMessageType: String,
        tempDated: String,
        tempSeen: String,
        tempConnectionType: String
    ) {
        chatBoxAdapter?.addMoreDataToList(Message(
            tempMyId,
            tempToId,
            tempMessage,
            type,
            tempMessageType,
            tempDated,
            tempSeen,
            tempConnectionType
        ))
        scrollToBottom()
    }

    private fun scrollToBottom() {
        myRecylerView.scrollToPosition(chatBoxAdapter?.itemCount?.minus(1)!!)
    }

    override fun onDestroy() {
        mWebSocketClient?.close()
        super.onDestroy()
    }
}