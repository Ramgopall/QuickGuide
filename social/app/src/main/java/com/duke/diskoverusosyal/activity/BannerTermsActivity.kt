package com.duke.diskoverusosyal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.duke.diskoverusosyal.R
import kotlinx.android.synthetic.main.activity_banner_terms.*

class BannerTermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_terms)
        ivBannerTermsBack.setOnClickListener { onBackPressed() }
        val terms = intent.getStringExtra("Desc")
        tvBannerTerms.text = Html.fromHtml(terms)
    }

    override fun onBackPressed() {
        finish()
    }
}
