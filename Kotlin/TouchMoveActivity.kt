package com.example.ramgopal.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_touch_move.*


class TouchMoveActivity : AppCompatActivity(), View.OnTouchListener {

    private var dX: Float = 0f
    private var locc: Float = 0f
    private var dY: Float = 0f

    var first: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_move)

        btnn.viewTreeObserver.addOnScrollChangedListener {
            // Do what you want to do when the view has been scrolled
            Toast.makeText(this@TouchMoveActivity, "scroll", Toast.LENGTH_SHORT).show()
        }

        btnn.setOnTouchListener(this)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {

                dX = view!!.x - event.rawX
                dY = view.y - event.rawY

                if (!first) {
                    locc = event.rawX
                    first = true
                }

            }

            MotionEvent.ACTION_UP -> {
                val temp =  event.rawX - locc
                if (temp>=150){
                    Toast.makeText(this@TouchMoveActivity, "Greater: "+temp, Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this@TouchMoveActivity, "Smaller: "+temp, Toast.LENGTH_SHORT).show()

                }
                view!!.animate()
                    .translationX(0f)
                    .setDuration(500)
                    .start()
            }

            MotionEvent.ACTION_MOVE -> {
                if (locc < event.rawX) {
                    view!!.animate()
                        .x(event.rawX + dX)
                        .setDuration(0)
                        .start()
                }
            }
            else -> return false
        }
        return true

    }


}
