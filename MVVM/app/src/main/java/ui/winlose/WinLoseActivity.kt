package com.triviallanguages.ui.winlose

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.triviallanguages.R
import com.triviallanguages.util.setImage
import com.triviallanguages.util.startHomeActivity
import kotlinx.android.synthetic.main.activity_graduated.*
import kotlinx.android.synthetic.main.activity_lose.NextLevel

class WinLoseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        when (intent?.getStringExtra("result")) {
            "win" -> setContentView(R.layout.activity_win)
            "lose" -> setContentView(R.layout.activity_lose)
            else -> {
                setContentView(R.layout.activity_graduated)
                val tempArray = arrayListOf(R.drawable.graduated_kids, R.drawable.graduated_kids, R.drawable.graduated_kids)
                tempArray.shuffle()
                setImage(tempArray[0], img_graduate!!)
            }
        }
        NextLevel.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
       startHomeActivity()
    }
}
