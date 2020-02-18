package com.duke.diskoverusosyal.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.trimmer.interfaces.OnK4LVideoListener
import com.duke.diskoverusosyal.trimmer.interfaces.OnTrimVideoListener
import kotlinx.android.synthetic.main.activity_trimmer.*

@Suppress("DEPRECATION")
class TrimmerActivity : AppCompatActivity(), OnTrimVideoListener, OnK4LVideoListener {

    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trimmer)

        val extraIntent = intent
        var path = ""
        if (extraIntent != null) {
            path = extraIntent.getStringExtra(AddNewPostActivity.EXTRA_VIDEO_PATH)!!
        }

        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.setMessage("Trimming your video")
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(45)
            mVideoTrimmer.setOnTrimVideoListener(this)
            mVideoTrimmer.setOnK4LVideoListener(this)
            mVideoTrimmer.setDestinationPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/")
            mVideoTrimmer.setVideoURI(Uri.parse(path))
            mVideoTrimmer.setVideoInformationVisibility(true)
        }
    }

    override fun onTrimStarted() {
        mProgressDialog?.show()
    }

    override fun getResult(uri: Uri) {
        mProgressDialog?.cancel()
        val returnIntent = Intent()
        returnIntent.putExtra("path", uri.path)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun cancelAction() {
        mProgressDialog?.cancel()
        mVideoTrimmer.destroy()
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }

    override fun onError(message:String) {
        mProgressDialog?.cancel()
        runOnUiThread {
            run {
                Toast.makeText(this@TrimmerActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onVideoPrepared() {
        runOnUiThread {
            run {
                Toast.makeText(this@TrimmerActivity, "onVideoPrepared", Toast.LENGTH_SHORT).show()
            }
        }
    }
}