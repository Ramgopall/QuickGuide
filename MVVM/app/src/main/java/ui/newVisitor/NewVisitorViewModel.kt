package com.duke.visitormanagement.ui.newVisitor

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duke.visitormanagement.Constant.Companion.LAST_INDEX_OF_URL_TRIM
import com.duke.visitormanagement.data.network.responses.spinnerData.CatItem
import com.duke.visitormanagement.data.repositories.NewVisitorRepository
import com.duke.visitormanagement.util.ApiException
import com.duke.visitormanagement.util.Coroutines
import com.duke.visitormanagement.util.NoInternetException
import com.theartofdev.edmodo.cropper.CropImage

class NewVisitorViewModel(
    private val repository: NewVisitorRepository
): ViewModel() {

    var newVisitorListener:NewVisitorListener? = null

    val imageUrl: MutableLiveData<String>? = MutableLiveData()
    val visitorName: MutableLiveData<String>? = MutableLiveData()
    val contactNumber: MutableLiveData<String>? = MutableLiveData()
    val vehicleNumber: MutableLiveData<String>? = MutableLiveData()
    val purpose: MutableLiveData<String>? = MutableLiveData()

    val firstSpinnerTitle: MutableLiveData<String>? = MutableLiveData("Select Category")
    val secondSpinnerTitle: MutableLiveData<String>? = MutableLiveData("Select Sub Category")
    val thirdSpinnerTitle: MutableLiveData<String>? = MutableLiveData("Select Person to whome you wanna meet")

    val firstSelectedItemPosition: MutableLiveData<Int>? = MutableLiveData()
    val secondSelectedItemPosition: MutableLiveData<Int>? = MutableLiveData()
    val thirdSelectedItemPosition: MutableLiveData<Int>? = MutableLiveData()

    private var responseSpinnerData = arrayListOf<CatItem?>()
    private val catList = arrayListOf<String>()
    private val catIdList = arrayListOf<String>()
    private val subCatList = arrayListOf<String>()
    private val subCatIdList = arrayListOf<String>()
    private val clientUserList = arrayListOf<String>()
    private val clientUserNumberList = arrayListOf<String>()
    private val clientUserIdList = arrayListOf<String>()

    private var catId=""
    private var subCatId=""
    private var clientId=""

    fun getSpinnerData(){
        try {
            newVisitorListener?.onStarted()
            Coroutines.main {
                try {
                    val authResponse = repository.getSpinnerData()
                    authResponse.cat?.let {
                        responseSpinnerData = it
                        for (i in 0 until responseSpinnerData.size){
                            catList.add(responseSpinnerData[i]?.name!!)
                            catIdList.add(responseSpinnerData[i]?.id!!)
                        }
                        newVisitorListener?.onSuccess(catList)
                        return@main
                    }
                    newVisitorListener?.onFailure(authResponse.message!!)
                }catch(e: ApiException){
                    newVisitorListener?.onFailure(e.message!!)
                }catch (e: NoInternetException){
                    newVisitorListener?.onFailure(e.message!!)
                }
            }
        } catch (e: ApiException) {
            e.message?.let { newVisitorListener?.onFailure(it) }
        }
    }

    fun onImageClick(view:View){
        newVisitorListener?.onImageClick()
    }

    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK ) {
                val photoPath = result.uri.path
                imageUrl?.value = photoPath
            }
        }
    }

    fun onFirstSpinnerItemSelect(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Coroutines.main {
            if (subCatList.size!=0){
                subCatList.clear()
                subCatIdList.clear()
            }
            val tempList = responseSpinnerData[pos]?.scate
            for (i in 0 until tempList?.size!!){
                subCatList.add(tempList[i]?.name!!)
                subCatIdList.add(tempList[i]?.id!!)
            }
            newVisitorListener?.onSpinner1Change(subCatList)
            catId = if (parent?.selectedItem.toString()==""){
                ""
            } else{
                catIdList[pos]
            }
            return@main
        }
    }

    fun onSecondSpinnerItemSelect(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Coroutines.main {
            if (clientUserList.size!=0){
                clientUserNumberList.clear()
                clientUserList.clear()
                clientUserIdList.clear()
            }
            val tempList = responseSpinnerData[firstSelectedItemPosition?.value!!]?.scate?.get(pos)?.client
            for (i in 0 until tempList?.size!!){
                clientUserNumberList.add(tempList[i]?.number!!)
                clientUserList.add(tempList[i]?.name!!)
                clientUserIdList.add(tempList[i]?.id!!)
            }
            newVisitorListener?.onSpinner2Change(clientUserList)
            subCatId = if (parent?.selectedItem.toString()==""){
                ""
            } else{
                subCatIdList[pos]
            }
            return@main
        }
    }

    fun onThirdSpinnerItemSelect(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        clientId = if (parent?.selectedItem.toString()==""){
            ""
        } else{
            clientUserIdList[pos]
        }
    }

    fun onSubmitClick(view:View){
        var tempOldImageUrl = ""
        var tempImageUrl = ""
        val imagePath = imageUrl?.value
        val tempName     = visitorName?.value
        val tempPhone    = contactNumber?.value
        var tempVehicle  = vehicleNumber?.value
        val tempPurpose  = purpose?.value
        if (imagePath==null){
            newVisitorListener?.onFailure("Image is required")
            return
        }
        if (tempName==null){
            newVisitorListener?.onFailure("Visitor Name is required")
            return
        }
        if (tempPhone==null){
            newVisitorListener?.onFailure("Phone number is required")
            return
        }
        if (tempPhone.length<10){
            newVisitorListener?.onFailure("Enter a valid phone number")
            return
        }
        if (tempPurpose==null){
            newVisitorListener?.onFailure("Purpose to meet is required")
            return
        }
        if (catId==""){
            newVisitorListener?.onFailure("Select Category")
            return
        }
        if (subCatId==""){
            newVisitorListener?.onFailure("Select Sub Category")
            return
        }
        if (clientId==""){
            newVisitorListener?.onFailure("Select Client Name")
            return
        }
        if (tempVehicle==null){
            tempVehicle=""
        }
        if(imagePath.startsWith("http")){
            tempOldImageUrl =imagePath.substring(LAST_INDEX_OF_URL_TRIM)
        }
        else{
            tempImageUrl = imagePath
        }
        try {
            newVisitorListener?.onStarted()
            Coroutines.main {
                try {
                    val authResponse = repository.submitNewVisitor(tempImageUrl,tempName,tempPhone,tempVehicle,tempPurpose,catId,subCatId,clientId,tempOldImageUrl)
                    authResponse.status?.let {
                        if (it){
                            newVisitorListener?.onSubmitSuccess(
                                clientUserList[thirdSelectedItemPosition?.value!!],
                                clientUserNumberList[thirdSelectedItemPosition.value!!]
                            )
                            return@main
                        }
                    }
                    newVisitorListener?.onFailure(authResponse.message!!)
                }catch(e: ApiException){
                    newVisitorListener?.onFailure(e.message!!)
                }catch (e: NoInternetException){
                    newVisitorListener?.onFailure(e.message!!)
                }
            }
        } catch (e: ApiException) {
            e.message?.let { newVisitorListener?.onFailure(it) }
        }
    }

    fun onBackClick(view:View){
        newVisitorListener?.onBackClick()
    }
}