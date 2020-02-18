package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.utils.Utils
import kotlinx.android.synthetic.main.activity_zoom.*

class ZoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom)
        val url = intent?.getStringExtra("image")
        ivZoomBack.setOnClickListener { onBackPressed() }
        Utils.showImage(this,url,ivZoom)
    }

    override fun onBackPressed() {
        finish()
    }
}
