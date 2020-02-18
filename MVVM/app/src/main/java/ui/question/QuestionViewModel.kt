package com.triviallanguages.ui.question

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.triviallanguages.BuildConfig
import com.triviallanguages.data.repositories.QuestionRepository
import com.triviallanguages.util.Coroutines
import com.triviallanguages.util.NoInternetException
import kotlinx.android.synthetic.main.activity_question.view.*


class QuestionViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    var questionRightIds = arrayListOf<String?>()
    var questionWrongIds = arrayListOf<String?>()
    var questionIds = arrayListOf<String?>()
    var questionListener: QuestionListener? = null

    var smallAd = ""
    var largAd = ""

    fun getQuestionFromServer(round1:String?,round2:String?,round3:String?) {
        questionListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getQuestions(round1,round2,round3)
                if (authResponse.small_ad?.size!! != 0){
                    smallAd = "https://triviallanguages.herokuapp.com/"+ authResponse.small_ad[0].file_path
                }
                if (authResponse.large_ad?.size!! != 0){
                    largAd = "https://triviallanguages.herokuapp.com/"+ authResponse.large_ad[0].file_path
                }
                authResponse.round1?.let {
                    repository.saveQuestions1(it)
                }
                authResponse.round2?.let {
                    repository.saveQuestions2(it)
                }
                authResponse.round3?.let {
                    questionListener?.onSuccess(authResponse.message)
                    repository.saveQuestions3(it)
                    return@main
                }
                questionListener?.onFailure(authResponse.message!!)
            } catch (e: ApiException) {
                questionListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                questionListener?.onFailure(e.message!!)
            }
        }
    }

    fun uploadHalfQuestionToServer() {
        Coroutines.main {
            try {
                repository.uploadQuestions(questionIds)
                repository.updateAdId(smallAd)
                repository.updateAdId(largAd)
            } catch (e: ApiException) {
                // questionListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                // questionListener?.onFailure(e.message!!)
            }
        }
    }

    fun uploadQuestionToServer(level:Int) {
        Coroutines.main {
            try {
                repository.uploadQuestions(questionIds)
                repository.updateLevel(level)
                repository.updateAdId(smallAd)
                repository.updateAdId(largAd)
            } catch (e: ApiException) {
                // questionListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                // questionListener?.onFailure(e.message!!)
            }
        }
    }

    fun getQuestion(round: Int) = repository.getQuestion(round)

    fun onOption1Click(view: View) {
        questionListener?.onOptionSelect(view.tvQuestionOption1?.text.toString(),"1")
    }

    fun onOption2Click(view: View) {
        questionListener?.onOptionSelect(view.tvQuestionOption2?.text.toString(),"2")
    }

    fun onOption3Click(view: View) {
        questionListener?.onOptionSelect(view.tvQuestionOption3?.text.toString(),"3")
    }

    fun onOption4Click(view: View) {
        questionListener?.onOptionSelect(view.tvQuestionOption4?.text.toString(),"4")
    }

    fun onClockClick(view: View) {

    }

    fun onSpickerClick(view: View) {
        questionListener?.onSpickerClick()
    }

    fun onHelpClick(view: View) {
        questionListener?.onHelpClick()
    }

    fun getQuestionIdCount(id:String) {
     Coroutines.main {
         val countt = repository.getQuestionIdCount(id)
         questionListener?.onGetCounts(countt)
     }
    }

    fun updateWrongAnswer(round: Int, ans: String, ans_corr: String, id: String?) {
        questionWrongIds.add(id)
        questionIds.add(id)
        Coroutines.main {
            repository.insertWrongWuestions(id)
        }
    }

    fun updateRightAnswer(round: Int, ans: String, ans_corr: String, id: String?) {
        questionRightIds.add(id)
        questionIds.add(id)
    }

}