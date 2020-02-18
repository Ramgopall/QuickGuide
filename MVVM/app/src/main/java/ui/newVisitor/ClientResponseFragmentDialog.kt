package com.duke.visitormanagement.ui.newVisitor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duke.visitormanagement.R
import kotlinx.android.synthetic.main.client_response_fragment_dialog.*


class ClientResponseFragmentDialog() : DialogFragment() {

    var newVisitorListener: NewVisitorListener? = null
    var countDownTimer: CountDownTimer? = null
    var name = ""
    var number = ""

    constructor(newVisitorListener: NewVisitorListener,name:String,number:String) : this() {
        this.newVisitorListener = newVisitorListener
        this.name = name
        this.number = number
    }

    companion object {
        private var clientResponseInstance: ClientResponseFragmentDialog? = null

        fun getInstace(): ClientResponseFragmentDialog? {
            return clientResponseInstance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.client_response_fragment_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        clientResponseInstance = this
        val tempTitle = "Waiting for $name Response"
        tvCRDWaitTitle.text = tempTitle
        countDownTimer = object : CountDownTimer(31000, 1000) {
            override fun onFinish() {
                llCRD1.visibility = View.GONE
                llCRD3.visibility = View.VISIBLE
                tvCRD3.text = "There has been no reply from $name"
            }

            override fun onTick(millisUntilFinished: Long) {
                val tempSecond = millisUntilFinished / 1000
                tvCRD.text = tempSecond.toString()
                pbCRD.progress = tempSecond.toInt()
            }
        }
        countDownTimer?.start()

        ivCRDClose2.setOnClickListener {
            dismiss()
            newVisitorListener?.onDialogCrossClick()
        }
        ivCRDClose3.setOnClickListener {
            dismiss()
            newVisitorListener?.onDialogCrossClick()
        }
        btnCRD3.setOnClickListener {
            Intent(Intent.ACTION_DIAL).also {
                it.data = Uri.parse("tel:$number")
                startActivity(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            // dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun getAccepted(message: String) {
        if (activity != null) {
            activity?.runOnUiThread {
                countDownTimer?.cancel()
                llCRD1.visibility = View.GONE
                llCRD2.visibility = View.VISIBLE
                val tempMsg = "Request Accepted\n$message"
                tvCRDReply2.text = tempMsg
                ivCRDReply2.setImageResource(R.drawable.ic_correct)
            }
        }
    }

    fun getRejected() {
        if (activity != null) {
            activity?.runOnUiThread {
                countDownTimer?.cancel()
                llCRD1.visibility = View.GONE
                llCRD2.visibility = View.VISIBLE
                val tempMsg = "Request Rejected"
                tvCRDReply2.text = tempMsg
                ivCRDReply2.setImageResource(R.drawable.ic_quit)
            }
        }
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        clientResponseInstance=null
        super.onDestroy()
    }

}