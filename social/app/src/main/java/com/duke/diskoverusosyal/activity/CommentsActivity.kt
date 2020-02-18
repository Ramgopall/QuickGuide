package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.diskoverusosyal.MyEditText
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.adapter.RecyclerView_Commnets_Adapter
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.comments.CommentsModel
import com.duke.diskoverusosyal.model.comments.DataItem
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_comments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.duke.diskoverusosyal.activity.HomeActivity.Companion.homeAdapter
import com.duke.diskoverusosyal.model.addComment.AddCommentModel


class CommentsActivity : AppCompatActivity() {
    var postId = ""
    var postUserSess = ""
    var listData = arrayListOf<DataItem>()
    var adapter: RecyclerView_Commnets_Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        postId = intent?.getStringExtra("id")!!
        postUserSess = intent?.getStringExtra("sess")!!
        val linearLayoutManager = LinearLayoutManager(this@CommentsActivity)
        linearLayoutManager.stackFromEnd = true
        rvComments?.layoutManager = linearLayoutManager
        rvComments?.itemAnimator = DefaultItemAnimator()
        getPosts()

        etCommentsMsg.setKeyBoardInputCallbackListener(MyEditText.KeyBoardInputCallbackListener { inputContentInfo, flags, opts ->
            //you will get your gif/png/jpg here in opts bundle
            Log.d("aaaaaaaaaaa", "yaha call hua")
        })
        btnCommentsSend?.setOnClickListener {
            val message = etCommentsMsg?.text.toString().trim { it <= ' ' }
            if (message == "") {
                etCommentsMsg.requestFocus()
                etCommentsMsg.error = "Can't send empty text"
                return@setOnClickListener
            }
            hideKeyboard()
            submitComment(message)
        }
        ivCommentsBack.setOnClickListener { finish() }
    }

    private fun getPosts() {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().getComments(postId)
        call.enqueue(object : Callback<CommentsModel>, RecyclerView_Commnets_Adapter.OnDeleteItemClick {

            override fun onDeleteClick(commentId: String?,position: Int) {
                val dialog = AlertDialog.Builder(this@CommentsActivity)
                dialog.setTitle("Delete Comment")
                dialog.setMessage("Are you sure you want to delete this comment?")
                dialog.setNegativeButton("No") { _, _ -> }
                dialog.setPositiveButton("Yes") { _, _ ->
                    deleteComment(commentId,position)
                    dialog.create().dismiss()
                }
                dialog.setCancelable(false)
                dialog.create().show()
            }

            override fun onResponse(
                call: Call<CommentsModel>?,
                response: Response<CommentsModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            listData = data.response?.data!!

                            val isUserViewingOwnPostComments = postUserSess == Utils.getUserSess()
                            adapter = RecyclerView_Commnets_Adapter(listData, this@CommentsActivity, isUserViewingOwnPostComments,this)
                            rvComments?.adapter = adapter

                        } else {
                            val isUserViewingOwnPostComments = postUserSess == Utils.getUserSess()
                            adapter = RecyclerView_Commnets_Adapter(listData, this@CommentsActivity, isUserViewingOwnPostComments,this)
                            rvComments?.adapter = adapter
                        }
                    } else {
                        Utils.showToast(
                            this@CommentsActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<CommentsModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@CommentsActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
    }

    private fun submitComment(message: String) {
        pbComments.visibility = View.VISIBLE
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        etCommentsMsg?.setText("")
        val call = ApiClient().getClient().commentOnPost(Utils.getUserSess(), message, postId)
        call.enqueue(object : Callback<AddCommentModel> {
            override fun onResponse(
                call: Call<AddCommentModel>?,
                response: Response<AddCommentModel>?
            ) {
                try {
                    pbComments.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            addMessage(
                                Utils.getCurrentTimeStamp(),
                                Utils.getUserPhoto(),
                                message,
                                Utils.getUserSess(),
                                Utils.getUserName(),
                                data.message!!
                            )
                            homeAdapter?.updateCommentCount(data.totalcoment,postId)

                        } else {
                            Utils.showToast(
                                this@CommentsActivity,
                                data.message
                            )
                        }
                    } else {
                        Utils.showToast(
                            this@CommentsActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<AddCommentModel>?, t: Throwable?) {
                try {
                    pbComments.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Utils.showToast(
                        this@CommentsActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
    }

    private fun deleteComment(commentId:String?,position: Int) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().deleteComment(commentId,postId)
        call.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                call: Call<CommonResponseModel>?,
                response: Response<CommonResponseModel>?
            ) {
                try {
                    Utils.cancelLoading()
                    if (response!!.body() != null && response.isSuccessful) {
                        val data = response.body()
                        if (data?.status!!) {
                            adapter?.removeAt(position)
                            homeAdapter?.updateCommentCount(data.message,postId)
                        } else {
                            Utils.showToast(
                                this@CommentsActivity,
                                data.message
                            )
                        }
                    } else {
                        Utils.showToast(
                            this@CommentsActivity,
                            resources.getString(R.string.no_response)
                        )
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }

            override fun onFailure(call: Call<CommonResponseModel>?, t: Throwable?) {
                try {
                    Utils.cancelLoading()
                    Utils.showToast(
                        this@CommentsActivity,
                        resources.getString(R.string.api_failure)
                    )
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })
    }

    private fun addMessage(
        currentTimeStamp: String,
        userPhoto: String?,
        message: String,
        userSess: String?,
        userName: String?,
        id: String
    ) {
        adapter?.addMoreDataToList(
            DataItem(currentTimeStamp, userPhoto, message, userSess, userName, id)
        )
        scrollToBottom()
    }

    private fun scrollToBottom() {
        rvComments.scrollToPosition(adapter?.itemCount?.minus(1)!!)
    }

    override fun onStop() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        finish()
        super.onStop()
    }

    fun hideKeyboard() {
        try {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(this@CommentsActivity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.d("error", "error")
        }
    }
}
