package com.triviallanguages.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.triviallanguages.R
import com.triviallanguages.ui.home.HomeActivity
import com.triviallanguages.ui.login.LoginActivity
import com.triviallanguages.ui.winlose.WinLoseActivity

private var progressDialog: Dialog? = null

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun Context.showLoadingDialog() {
    if (!(progressDialog != null && progressDialog!!.isShowing)) {
        progressDialog = Dialog(this)
        progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.progress_dialog)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }
}

fun Context.cancelLoading() {
    if (progressDialog != null && progressDialog!!.isShowing)
        progressDialog!!.cancel()
}

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()

}

fun Context.setImage(url: Any,view:ImageView) {
    Glide.with(this).load(url).into(view)
}

fun getHash(): String {
    val temp = StringBuilder(first+second+third).reverse().toString()

    var m: MessageDigest? = null
    try {
        m = MessageDigest.getInstance("MD5")
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    m!!.update(temp.toByteArray(), 0, temp.length)
    return BigInteger(1, m.digest()).toString(16)
}

fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun rand() : String {
    val rnds1 = (1000..9999).random()
    return "$rnds1"
}

fun dateConvert(firstDate:String?): String{
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val date = formatter.parse(firstDate!!)!!
    val tempDay = SimpleDateFormat("dd", Locale.US).format(date)
    val tempMonth = SimpleDateFormat("MMM", Locale.US).format(date)
    val tempYear = SimpleDateFormat("yyyy", Locale.US).format(date)
    val tempTime = SimpleDateFormat("KK:mm a",Locale.US).format(date)
    return "$tempDay $tempMonth,$tempYear\n$tempTime"
}

fun timeConvert(firstDate:String?): String{
    val formatter = SimpleDateFormat("hh:mm:ss", Locale.US)
    val date = formatter.parse(firstDate!!)!!
    return SimpleDateFormat("KK:mm a",Locale.US).format(date)
}

fun getTimeMilli(timee:String?): Date{
    val time1: Date = SimpleDateFormat("hh:mm:ss", Locale.US).parse(timee!!)!!
    val calendar1: Calendar = Calendar.getInstance()
    calendar1.time = time1
    calendar1.add(Calendar.DATE, 1)
    return calendar1.time
}

fun Context.loadJSONFromAsset(fileName:String): String? {
    //example:-   filename = "countries.json"
    val json: String?
    try {
        val iss = assets?.open(fileName)
        val size = iss?.available()!!
        val buffer = ByteArray(size)
        iss.read(buffer)
        iss.close()
        val charset: Charset = Charsets.UTF_8
        json = String(buffer, charset)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}

@Suppress("DEPRECATION")
fun Context.getImagePath(imageUri: Uri): String? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver?.query(imageUri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val picturePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return picturePath
}