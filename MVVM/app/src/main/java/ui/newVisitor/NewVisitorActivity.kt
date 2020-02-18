package com.duke.visitormanagement.ui.newVisitor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.duke.visitormanagement.R
import com.duke.visitormanagement.data.network.responses.completedMeetings.EnqueryDataItem
import com.duke.visitormanagement.databinding.ActivityNewVisitorBinding
import com.duke.visitormanagement.util.cancelLoading
import com.duke.visitormanagement.util.showLoadingDialog
import com.duke.visitormanagement.util.toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class NewVisitorActivity : AppCompatActivity(), KodeinAware, NewVisitorListener {

    override val kodein by kodein()
    var binding:ActivityNewVisitorBinding? = null
    private var viewModel: NewVisitorViewModel? = null
    private val factory: NewVisitorViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_visitor)
        viewModel = ViewModelProviders.of(this,factory).get(NewVisitorViewModel::class.java)
        viewModel?.newVisitorListener = this
        binding?.lifecycleOwner = this
        binding?.viewmodel = viewModel
        viewModel?.getSpinnerData()
        if (intent.extras != null){
            val oldData = intent.getSerializableExtra("oldEntry") as EnqueryDataItem
            viewModel?.visitorName?.value = oldData.name
            viewModel?.contactNumber?.value = oldData.phone
            viewModel?.vehicleNumber?.value = oldData.vehicle
            viewModel?.imageUrl?.value = oldData.image
        }
    }

    override fun onStarted() {
        showLoadingDialog()
    }

    override fun onBackClick() {
        onBackPressed()
    }

    override fun onSuccess(catList: ArrayList<String>) {
        binding?.spinAdapter1 = ArrayAdapter(this,android.R.layout.simple_list_item_1,catList)
        cancelLoading()
    }

    override fun onSubmitSuccess(name: String,number:String) {
        cancelLoading()
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("ClientResponseFragmentDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val dialogFragment = ClientResponseFragmentDialog(this,name,number)
        dialogFragment.isCancelable = false
        dialogFragment.show(ft, "ClientResponseFragmentDialog")
    }

    override fun onFailure(message: String) {
        cancelLoading()
        toast(message)
    }

    override fun onImageClick() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this@NewVisitorActivity)
    }

    override fun onDialogCrossClick() {
        finish()
    }

    override fun onSpinner1Change(subCatList: ArrayList<String>) {
        binding?.spinAdapter2 = ArrayAdapter(this,android.R.layout.simple_list_item_1,subCatList)
        binding?.spinAdapter2?.notifyDataSetChanged()
    }

    override fun onSpinner2Change(clientUserList: ArrayList<String>) {
        binding?.spinAdapter3 = ArrayAdapter(this,android.R.layout.simple_list_item_1,clientUserList)
        binding?.spinAdapter3?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel?.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
