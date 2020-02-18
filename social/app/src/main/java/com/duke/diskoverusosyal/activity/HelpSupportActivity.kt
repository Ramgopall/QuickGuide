package com.duke.diskoverusosyal.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duke.diskoverusosyal.R
import kotlinx.android.synthetic.main.activity_help_support.*

class HelpSupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)
        ivHelpBack.setOnClickListener { onBackPressed() }
        tvHelpEmail.setOnClickListener { composeEmail(arrayOf("sosyal@diskoveruklub.com"),"") }
    }

    fun composeEmail(addresses: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
