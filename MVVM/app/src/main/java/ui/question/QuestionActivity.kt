package com.triviallanguages.ui.question

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.triviallanguages.Constant.Companion.imageList
import com.triviallanguages.Constant.Companion.roundNumberList
import com.triviallanguages.R
import com.triviallanguages.databinding.ActivityQuestionBinding
import com.triviallanguages.ui.winlose.WinLoseActivity
import com.triviallanguages.util.cancelLoading
import com.triviallanguages.util.setImage
import com.triviallanguages.util.showLoadingDialog
import com.triviallanguages.util.toast
import kotlinx.android.synthetic.main.popup_dialog.*
import kotlinx.android.synthetic.main.popup_dialog.ivPopUpGotIt
import kotlinx.android.synthetic.main.round_win_popup_dialog.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class QuestionActivity : AppCompatActivity(), KodeinAware, QuestionListener {

    var binding: ActivityQuestionBinding? = null
    override val kodein by kodein()
    private val factory: QuestionViewModelFactory by instance()
    var viewModel: QuestionViewModel? = null

    var round = 1
    var question_no = 1
    var index = 0
    lateinit var countDownTimer: CountDownTimer
    val timer: Long = 31000
    var reTime: Long = 0
    var correctAnswer: String? = ""
    var questionId: String? = ""

    var wrongAttempt = 5
    var helpAttempts = 5

    var option1: String? = ""
    var option2: String? = ""
    var option3: String? = ""
    var option4: String? = ""
    var quest: String? = ""

    var isHelpTaken = false
    var level = 0

    var mpDialog : MediaPlayer?=null
    var mpTeacherDialog : MediaPlayer?=null
    var mpFlag : MediaPlayer?=null
    var mpClock : MediaPlayer?=null
    var mpCategorySound : MediaPlayer?=null
    var mpJokerButtonClick : MediaPlayer?=null
    var mpCloud : MediaPlayer?=null
    var mpButtonClick : MediaPlayer?=null
    var mpWrongAns    : MediaPlayer?=null
    var mpCorrectAns  : MediaPlayer?=null
    var mpSlideDown   : MediaPlayer?=null
    var mpQuestionsAppear  : MediaPlayer?=null

    private var tts: TtsManager? = null
    var suffelAnimation : AnimationDrawable? = null

    var isListeningTypeQstn = false
    var isClockSoundPlaying = false
    var isStoppedd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question)
        viewModel = ViewModelProviders.of(this, factory).get(QuestionViewModel::class.java)
        binding?.viewModel = viewModel
        viewModel?.questionListener = this
        level = intent?.getIntExtra("level", 0)!!
        setImage(imageList[level - 1], binding?.ivQuestionCircle!!)
        tts = TtsManager(this,"")

        mpTeacherDialog     = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
        mpDialog            = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
        mpFlag              = MediaPlayer.create(this@QuestionActivity, R.raw.ready_go)
        mpClock             = MediaPlayer.create(this@QuestionActivity, R.raw.clock)
        mpCategorySound     = MediaPlayer.create(this@QuestionActivity, R.raw.category_sound)
        mpJokerButtonClick  = MediaPlayer.create(this@QuestionActivity, R.raw.joker)
        mpCloud             = MediaPlayer.create(this@QuestionActivity, R.raw.cloud)
        mpButtonClick       = MediaPlayer.create(this@QuestionActivity, R.raw.button_click)
        mpWrongAns          = MediaPlayer.create(this@QuestionActivity, R.raw.wrong_answer)
        mpCorrectAns        = MediaPlayer.create(this@QuestionActivity, R.raw.correct_answer)
        mpSlideDown         = MediaPlayer.create(this@QuestionActivity, R.raw.answers_go_down_from_the_top)
        mpQuestionsAppear   = MediaPlayer.create(this@QuestionActivity, R.raw.questions_appear)

        //popUpDialog(7)
        val round = roundNumberList[level - 1].split(",")
        viewModel?.getQuestionFromServer(round[0],round[1],round[2])
    }

    private fun timerStart() {
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onFinish() {
                isClockSoundPlaying = false
                checkUserAnswer("","1")
            }

            override fun onTick(millisUntilFinished: Long) {
                reTime = millisUntilFinished
                try {
                    val timeRemain = millisUntilFinished / 1000
                    binding?.tvQuestionTimer?.text = timeRemain.toString()
                } catch (e: Exception) {
                    binding?.tvQuestionTimer?.text = "0"
                }
                Log.d("mmmmm", millisUntilFinished.toString())
            }
        }
    }

    private fun resumeTimmer(reTimeee:Long) {
        countDownTimer = object : CountDownTimer(reTimeee, 1000) {
            override fun onFinish() {
                isClockSoundPlaying = false
                checkUserAnswer("","1")
            }

            override fun onTick(millisUntilFinished: Long) {
                reTime = millisUntilFinished
                try {
                    val timeRemain = millisUntilFinished / 1000
                    binding?.tvQuestionTimer?.text = timeRemain.toString()
                } catch (e: Exception) {
                    binding?.tvQuestionTimer?.text = "0"
                }
                Log.d("mmmmm", millisUntilFinished.toString())
            }
        }
        countDownTimer.start()
    }

    override fun onStarted() {
        showLoadingDialog()
    }

    override fun onSuccess(message: String?) {
        cancelLoading()
        setImage(viewModel?.largAd!!, binding?.ivQuestionLargeAd!!)
        startGame()
    }

    private fun startGame(){
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding?.llQuestionOptions?.visibility = View.GONE
        binding?.ivQuestionCategory?.visibility = View.GONE
        binding?.ivQuestionSuffel?.visibility = View.GONE
        binding?.llQuestionQst?.visibility = View.INVISIBLE
        binding?.rlQuestionSpeak?.visibility = View.GONE
        binding?.rlQuestionOptions?.visibility = View.GONE
        binding?.tvQuestionOption1?.visibility = View.INVISIBLE
        binding?.tvQuestionOption2?.visibility = View.INVISIBLE
        binding?.tvQuestionOption3?.visibility = View.INVISIBLE
        binding?.tvQuestionOption4?.visibility = View.INVISIBLE
        binding?.llQuestionClock?.visibility = View.INVISIBLE
        binding?.ivQuestionCorrectAns?.visibility = View.GONE
        binding?.ivQuestionWrongAns?.visibility = View.GONE
       // binding?.tvQuestionNo?.text = ""
        binding?.tvQuestionText?.text = ""

        val flagIn = AnimationUtils.loadAnimation(this,R.anim.zoomout)
        val flagOut = AnimationUtils.loadAnimation(this,R.anim.zoomin)
        binding?.ivQuestionReadyFlag?.visibility = View.VISIBLE
        binding?.ivQuestionReadyFlag?.startAnimation(flagOut)
        mpFlag?.start()
        Handler().postDelayed({
            binding?.ivQuestionReadyFlag?.startAnimation(flagIn)
            Handler().postDelayed({
                if (mpFlag?.isPlaying!!){
                    mpFlag?.pause()
                    mpFlag?.seekTo(0)
                }
                binding?.ivQuestionReadyFlag?.visibility = View.GONE
                suffelImages()
            },1000)
        }, 1500)
    }

    private fun suffelImages(){
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding?.llQuestionOptions?.visibility = View.GONE
        binding?.ivQuestionCategory?.visibility = View.GONE
        binding?.ivQuestionSuffel?.visibility = View.GONE
        binding?.llQuestionQst?.visibility = View.INVISIBLE
        binding?.rlQuestionSpeak?.visibility = View.GONE
        binding?.rlQuestionOptions?.visibility = View.GONE
        binding?.tvQuestionOption1?.text = ""
        binding?.tvQuestionOption2?.text = ""
        binding?.tvQuestionOption3?.text = ""
        binding?.tvQuestionOption4?.text = ""
        binding?.tvQuestionOption1?.visibility = View.INVISIBLE
        binding?.tvQuestionOption2?.visibility = View.INVISIBLE
        binding?.tvQuestionOption3?.visibility = View.INVISIBLE
        binding?.tvQuestionOption4?.visibility = View.INVISIBLE
        binding?.llQuestionClock?.visibility = View.INVISIBLE
        binding?.ivQuestionCorrectAns?.visibility = View.GONE
        binding?.ivQuestionWrongAns?.visibility = View.GONE
        binding?.tvQuestionText?.text = ""

        showQuestion()
    }

    override fun onFailure(message: String) {
        cancelLoading()
        toast(message)
    }

    override fun onGetCounts(getCountIds: Int) {
        if (getCountIds==1){
            changeQuestion()
        }
        else{
            popUpDialog(getCountIds)
        }
    }

    override fun onOptionSelect(answer: String,optionButtonNo:String) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        checkUserAnswer(answer,optionButtonNo)
    }

    override fun onSpickerClick() {
        tts?.say(quest, processSelection(getSystemLanguage().toString()))
    }

    override fun onHelpClick() {
        if (!isHelpTaken && helpAttempts > 0) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

//            val scaleDownX = ObjectAnimator.ofFloat(binding?.ivQuestionJoker!!, "scaleX", 0.9f)
//            val scaleDownY = ObjectAnimator.ofFloat(binding?.ivQuestionJoker!!, "scaleY", 0.9f)
//            scaleDownX.duration = 10
//            scaleDownY.duration = 10
//            val scaleDown = AnimatorSet()
//            scaleDown.play(scaleDownX).with(scaleDownY)
//            scaleDown.start()

            Handler().postDelayed({
                binding?.ivQuestionCircle?.setImageDrawable(null)
                binding?.ivQuestionCircle?.setImageDrawable(getDrawable(R.drawable.joker_collection))
                val jokerAnimation = binding?.ivQuestionCircle?.drawable as AnimationDrawable

                jokerAnimation.start()
                Handler().postDelayed({
                    mpJokerButtonClick?.start()
                    isHelpTaken = true
                    helpAttempts -= 1
                    binding?.tvQuestionHelp?.text = helpAttempts.toString()
                    val tempArray = arrayListOf(option2, option3, option4)
                    tempArray.shuffle()
                    val tempWrongOption = tempArray[0]
                    val tempWrongOption2 = tempArray[1]
                    Handler().postDelayed({
                        val cloudAnimation:AnimationDrawable?
                        when (tempWrongOption) {
                            binding?.tvQuestionOption1?.text -> {
                                binding?.ivQuestionOption1?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                cloudAnimation = binding?.ivQuestionOption1?.drawable as AnimationDrawable
                                cloudAnimation.start()
                                mpCloud?.start()
                                binding?.tvQuestionOption1?.text = ""
                                binding?.llQuestionOption1?.isEnabled = false
                            }
                            binding?.tvQuestionOption2?.text -> {
                                binding?.ivQuestionOption2?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                cloudAnimation = binding?.ivQuestionOption2?.drawable as AnimationDrawable
                                cloudAnimation.start()
                                mpCloud?.start()
                                binding?.tvQuestionOption2?.text = ""
                                binding?.llQuestionOption2?.isEnabled = false
                            }
                            binding?.tvQuestionOption3?.text -> {
                                binding?.ivQuestionOption3?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                cloudAnimation = binding?.ivQuestionOption3?.drawable as AnimationDrawable
                                cloudAnimation.start()
                                mpCloud?.start()
                                binding?.tvQuestionOption3?.text = ""
                                binding?.llQuestionOption3?.isEnabled = false
                            }
                            else -> {
                                binding?.ivQuestionOption4?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                cloudAnimation = binding?.ivQuestionOption4?.drawable as AnimationDrawable
                                cloudAnimation.start()
                                mpCloud?.start()
                                binding?.tvQuestionOption4?.text = ""
                                binding?.llQuestionOption4?.isEnabled = false
                            }
                        }
                        Handler().postDelayed({
                            if (mpCloud!=null){
                                if (mpCloud?.isPlaying!!){
                                    mpCloud?.pause()
                                    mpCloud?.seekTo(0)
                                }
                            }
                            if (mpJokerButtonClick!=null){
                                if (mpJokerButtonClick?.isPlaying!!){
                                    mpJokerButtonClick?.pause()
                                    mpJokerButtonClick?.seekTo(0)
                                }
                            }

                            binding?.ivQuestionOption1?.setImageDrawable(null)
                            binding?.ivQuestionOption2?.setImageDrawable(null)
                            binding?.ivQuestionOption3?.setImageDrawable(null)
                            binding?.ivQuestionOption4?.setImageDrawable(null)
                            cloudAnimation.stop()

                            jokerAnimation.stop()
                            jokerAnimation.start()
                            Handler().postDelayed({
                                mpJokerButtonClick?.start()
                                Handler().postDelayed({
                                    val cloudSecondAnimation:AnimationDrawable?
                                    when (tempWrongOption2) {
                                        binding?.tvQuestionOption1?.text -> {
                                            binding?.ivQuestionOption1?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                            cloudSecondAnimation = binding?.ivQuestionOption1?.drawable as AnimationDrawable
                                            cloudSecondAnimation.start()
                                            mpCloud?.start()
                                            binding?.tvQuestionOption1?.text = ""
                                            binding?.llQuestionOption1?.isEnabled = false
                                        }
                                        binding?.tvQuestionOption2?.text -> {
                                            binding?.ivQuestionOption2?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                            cloudSecondAnimation = binding?.ivQuestionOption2?.drawable as AnimationDrawable
                                            cloudSecondAnimation.start()
                                            mpCloud?.start()
                                            binding?.tvQuestionOption2?.text = ""
                                            binding?.llQuestionOption2?.isEnabled = false
                                        }
                                        binding?.tvQuestionOption3?.text -> {
                                            binding?.ivQuestionOption3?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                            cloudSecondAnimation = binding?.ivQuestionOption3?.drawable as AnimationDrawable
                                            cloudSecondAnimation.start()
                                            mpCloud?.start()
                                            binding?.tvQuestionOption3?.text = ""
                                            binding?.llQuestionOption3?.isEnabled = false
                                        }
                                        else -> {
                                            binding?.ivQuestionOption4?.setImageDrawable(getDrawable(R.drawable.cloud_collection))
                                            cloudSecondAnimation = binding?.ivQuestionOption4?.drawable as AnimationDrawable
                                            cloudSecondAnimation.start()
                                            mpCloud?.start()
                                            binding?.tvQuestionOption4?.text = ""
                                            binding?.llQuestionOption4?.isEnabled = false
                                        }
                                    }
                                    Handler().postDelayed({
                                        if (mpCloud!=null){
                                            if (mpCloud?.isPlaying!!){
                                                mpCloud?.pause()
                                                mpCloud?.seekTo(0)
                                            }
                                        }
                                        if (mpJokerButtonClick!=null){
                                            if (mpJokerButtonClick?.isPlaying!!){
                                                mpJokerButtonClick?.pause()
                                                mpJokerButtonClick?.seekTo(0)
                                            }
                                        }

                                        binding?.ivQuestionOption1?.setImageDrawable(null)
                                        binding?.ivQuestionOption2?.setImageDrawable(null)
                                        binding?.ivQuestionOption3?.setImageDrawable(null)
                                        binding?.ivQuestionOption4?.setImageDrawable(null)
                                        binding?.ivQuestionCircle?.setImageDrawable(null)

                                        cloudSecondAnimation.stop()
                                        jokerAnimation.stop()
                                        setImage(imageList[level - 1], binding?.ivQuestionCircle!!)

//                                        val scaleUpX = ObjectAnimator.ofFloat(binding?.ivQuestionJoker!!, "scaleX", 1f)
//                                        val scaleUpY = ObjectAnimator.ofFloat(binding?.ivQuestionJoker!!, "scaleY", 1f)
//                                        scaleUpX.duration = 10
//                                        scaleUpY.duration = 10
//                                        val scaleUp = AnimatorSet()
//                                        scaleUp.play(scaleUpX).with(scaleUpY)
//                                        scaleUp.start()


                                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    },2000)
                                },200)
                            },200)
                        },2000)
                    },200)
                },200)
            },100)

        }
    }

    private fun checkUserAnswer(answer: String,optionButtonNo:String) {
        if (mpClock!=null){
            if (mpClock?.isPlaying!!){
                mpClock?.pause()
                mpClock?.seekTo(0)
            }
        }
        if (countDownTimer != null) {
            countDownTimer.cancel()
        }
        if (answer!=""){
            mpButtonClick?.start()
        }

        when (correctAnswer) {
            binding?.tvQuestionOption1?.text -> {
                binding?.tvQuestionOption2?.text = ""
                binding?.tvQuestionOption3?.text = ""
                binding?.tvQuestionOption4?.text = ""

                binding?.tvQuestionOption2?.visibility = View.INVISIBLE
                binding?.tvQuestionOption3?.visibility = View.INVISIBLE
                binding?.tvQuestionOption4?.visibility = View.INVISIBLE
            }
            binding?.tvQuestionOption2?.text -> {
                binding?.tvQuestionOption1?.text = ""
                binding?.tvQuestionOption3?.text = ""
                binding?.tvQuestionOption4?.text = ""
                binding?.tvQuestionOption1?.visibility = View.INVISIBLE
                binding?.tvQuestionOption3?.visibility = View.INVISIBLE
                binding?.tvQuestionOption4?.visibility = View.INVISIBLE

            }
            binding?.tvQuestionOption3?.text -> {
                binding?.tvQuestionOption1?.text = ""
                binding?.tvQuestionOption2?.text = ""
                binding?.tvQuestionOption4?.text = ""
                binding?.tvQuestionOption1?.visibility = View.INVISIBLE
                binding?.tvQuestionOption2?.visibility = View.INVISIBLE
                binding?.tvQuestionOption4?.visibility = View.INVISIBLE
            }
            else -> {
                binding?.tvQuestionOption1?.text = ""
                binding?.tvQuestionOption2?.text = ""
                binding?.tvQuestionOption3?.text = ""
                binding?.tvQuestionOption1?.visibility = View.INVISIBLE
                binding?.tvQuestionOption2?.visibility = View.INVISIBLE
                binding?.tvQuestionOption3?.visibility = View.INVISIBLE
            }
        }

        binding?.llQuestionClock?.visibility = View.INVISIBLE
        binding?.tvQuestionTimer?.text = ""

        binding?.llQuestionQst?.visibility = View.INVISIBLE
        if (answer != correctAnswer) {
            viewModel?.updateWrongAnswer(round, answer, "0", questionId)
            val flagOut = AnimationUtils.loadAnimation(this,R.anim.zoomin_wrong)
            binding?.ivQuestionWrongAns?.visibility = View.VISIBLE
            binding?.ivQuestionWrongAns?.startAnimation(flagOut)
            if (mpWrongAns!=null){
                mpWrongAns?.start()
            }
            Handler().postDelayed({
                binding?.ivQuestionWrongAns?.visibility = View.GONE
                if (mpWrongAns!=null){
                    if (mpWrongAns?.isPlaying!!){
                        mpWrongAns?.pause()
                        mpWrongAns?.seekTo(0)
                    }
                }
                if (mpButtonClick!=null){
                    if (mpButtonClick?.isPlaying!!){
                        mpButtonClick?.pause()
                        mpButtonClick?.seekTo(0)
                    }
                }
                if (wrongAttempt == 0) {
                    //popUpDialog(true)
                    viewModel?.uploadHalfQuestionToServer()
                    Intent(this, WinLoseActivity::class.java).also {
                        it.putExtra("result", "lose")
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                    finish()
                    return@postDelayed
                }
                else {
                    wrongAttempt -= 1
                    viewModel?.getQuestionIdCount(questionId!!)
                    //
                }
            },3300)
        }
        else {
            val flagOut = AnimationUtils.loadAnimation(this,R.anim.zoomin)
            binding?.ivQuestionCorrectAns?.visibility = View.VISIBLE
            binding?.ivQuestionCorrectAns?.startAnimation(flagOut)
            if (mpCorrectAns!=null){
                mpCorrectAns?.start()
            }

            Handler().postDelayed({
                viewModel?.updateRightAnswer(round, answer, "1", questionId)
                if (mpCorrectAns!=null){
                    if (mpCorrectAns?.isPlaying!!){
                        mpCorrectAns?.pause()
                        mpCorrectAns?.seekTo(0)
                    }
                }
                if (mpButtonClick!=null){
                    if (mpButtonClick?.isPlaying!!){
                        mpButtonClick?.pause()
                        mpButtonClick?.seekTo(0)
                    }
                }
                binding?.ivQuestionCorrectAns?.visibility = View.GONE
                changeQuestion()
            },1200)
        }
    }

    private fun popUpDialog(countIds: Int?) {
        mpTeacherDialog?.start()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding?.llQuestionOptions?.visibility = View.GONE
        binding?.rlQuestionOptions?.visibility = View.INVISIBLE

        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setView(R.layout.popup_dialog)
        val alertDialog = dialog.create()
        alertDialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!this@QuestionActivity.isFinishing) {
            alertDialog.show()
            alertDialog.tvPopUpAnswer.text = correctAnswer
            alertDialog.ivPopUpGotIt.setOnClickListener {
                if (mpTeacherDialog!=null){
                    if (mpTeacherDialog?.isPlaying!!){
                        mpTeacherDialog?.pause()
                        mpTeacherDialog?.seekTo(0)
                    }
                }
                alertDialog.dismiss()
                if (mpButtonClick!=null){
                    mpButtonClick?.start()
                }
                Handler().postDelayed({
                    if (mpCorrectAns!=null){
                        if (mpCorrectAns?.isPlaying!!){
                            mpButtonClick?.pause()
                            mpButtonClick?.seekTo(0)
                        }
                    }
                    changeQuestion()
                },1000)
            }

            if (countIds!! > 6) {
                alertDialog.tvPopUpExpressionWord.text =
                    "Never mind....\nYou'll never learn this.\nIt's Impossible."
                setImage(R.drawable.expression6, alertDialog.ivPopUpExpression)
            }
            else {
                when (countIds) {
                    2 -> {
                        alertDialog.tvPopUpExpressionWord.text =
                            "My dear friend,\nThis is the second time you have done mistake in the same question.\nKEEP ATTENTION!"
                        setImage(R.drawable.expression1, alertDialog.ivPopUpExpression)
                    }
                    3 -> {
                        alertDialog.tvPopUpExpressionWord.text =
                            "You Did it again.\nThis is the third!\nPut more attention please!"
                        setImage(R.drawable.expression2, alertDialog.ivPopUpExpression)
                    }
                    4 -> {
                        alertDialog.tvPopUpExpressionWord.text =
                            "What the hell!\nThis is forth time.\nMaybe you are not interested to learn it."
                        setImage(R.drawable.expression3, alertDialog.ivPopUpExpression)
                    }
                    5 -> {
                        alertDialog.tvPopUpExpressionWord.text =
                            "OMG!!!\nYou don't understand nothing!\nIt's the fifth time!"
                        setImage(R.drawable.expression4, alertDialog.ivPopUpExpression)
                    }
                    6 -> {
                        alertDialog.tvPopUpExpressionWord.text =
                            "What are you doing.\nThe is Sixth Time!!!!!!!!!!"
                        setImage(R.drawable.expression5, alertDialog.ivPopUpExpression)
                    }
                }
            }

        }
    }

    private fun changeQuestion() {
        if (question_no < 10) {
            question_no += 1
            index += 1
            binding?.rlQuestionOptions?.visibility = View.INVISIBLE
            suffelImages()
        } else {
            question_no = 1
            index = 0
            if (round < 3) {
                round += 1
                popUpRoundDialog()
            } else {
                viewModel?.uploadQuestionToServer(level)
                if (level == 64) {
                    Intent(this, WinLoseActivity::class.java).also {
                        it.putExtra("result", "win")
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    Intent(this, WinLoseActivity::class.java).also {
                        it.putExtra("result", "graduated")
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }
                finish()
            }
        }
    }

    private fun popUpRoundDialog() {
        mpDialog?.start()
        binding?.cvQustionNumber1?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber2?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber3?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber4?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber5?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber6?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber7?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber8?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber9?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))
        binding?.cvQustionNumber10?.setCardBackgroundColor(Color.parseColor("#e1e1e1"))

        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setView(R.layout.round_win_popup_dialog)
        val alertDialog = dialog.create()
        alertDialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!this@QuestionActivity.isFinishing) {
            alertDialog.show()
            Handler().postDelayed(Runnable {
                alertDialog.nextRound.visibility = View.VISIBLE
            },3500)
        }
        alertDialog.nextRound.setOnClickListener {
            if (mpDialog!=null){
                if (mpDialog?.isPlaying!!){
                    mpDialog?.pause()
                    mpDialog?.seekTo(0)
                }
            }
            alertDialog.dismiss()
            if (mpButtonClick!=null){
                mpButtonClick?.start()
            }
            Handler().postDelayed({
                if (mpButtonClick!=null){
                    if (mpButtonClick?.isPlaying!!){
                        mpButtonClick?.pause()
                        mpButtonClick?.seekTo(0)
                    }
                }
                binding?.rlQuestionOptions?.visibility = View.INVISIBLE
                startGame()
            },1000)
        }

    }

    private fun showQuestion() {
        //binding?.ivQuestionCategory?.visibility = View.VISIBLE
        //binding?.llQuestionOptions?.visibility = View.VISIBLE
        var selectedCategory = ""
        timerStart()
        reTime = 31000
        isHelpTaken = false
        val roundText = "$round/3"
        //val questionNoText = "Question-$question_no/10"
        binding?.tvQuestionRound?.text = roundText

        when (question_no) {
            1 -> {
                binding?.cvQustionNumber1?.setCardBackgroundColor(Color.parseColor("#FF0000"))
            }
            2 -> {
                binding?.cvQustionNumber2?.setCardBackgroundColor(Color.parseColor("#FF3300"))
            }
            3 -> {
                binding?.cvQustionNumber3?.setCardBackgroundColor(Color.parseColor("#ff6600"))
            }
            4 -> {
                binding?.cvQustionNumber4?.setCardBackgroundColor(Color.parseColor("#ff9900"))
            }
            5 -> {
                binding?.cvQustionNumber5?.setCardBackgroundColor(Color.parseColor("#FFCC00"))
            }
            6 -> {
                binding?.cvQustionNumber6?.setCardBackgroundColor(Color.parseColor("#FFFF00"))
            }
            7 -> {
                binding?.cvQustionNumber7?.setCardBackgroundColor(Color.parseColor("#ccff00"))
            }
            8 -> {
                binding?.cvQustionNumber8?.setCardBackgroundColor(Color.parseColor("#99ff00"))
            }
            9 -> {
                binding?.cvQustionNumber9?.setCardBackgroundColor(Color.parseColor("#33ff00"))
            }
            else -> {
                binding?.cvQustionNumber10?.setCardBackgroundColor(Color.parseColor("#00FF00"))
            }
        }
        binding?.tvQuestionWrongAttemps?.text = wrongAttempt.toString()
        binding?.tvQuestionText?.visibility = View.GONE
        binding?.rlQuestionSpeak?.visibility = View.GONE

        binding?.llQuestionOption1?.isEnabled = true
        binding?.llQuestionOption2?.isEnabled = true
        binding?.llQuestionOption3?.isEnabled = true
        binding?.llQuestionOption4?.isEnabled = true

        viewModel?.getQuestion(round)?.observe(this, Observer { question ->
            if (question != null) {
                try {
                    binding?.tvQuestionLevel?.text = question[index].level
                    correctAnswer = question[index].correct
                    questionId = question[index]._id

                    option1 = question[index].correct
                    option2 = question[index].wrong1
                    option3 = question[index].wrong2
                    option4 = question[index].wrong3
                    quest = question[index].question

                    val tempArray = arrayListOf(option1, option2, option3, option4)
                    tempArray.shuffle()


                    binding?.tvQuestionOption1?.text = tempArray[0]
                    binding?.tvQuestionOption2?.text = tempArray[1]
                    binding?.tvQuestionOption3?.text = tempArray[2]
                    binding?.tvQuestionOption4?.text = tempArray[3]
//                    binding?.tvQuestionOption1?.text = option1
//                    binding?.tvQuestionOption2?.text = option2
//                    binding?.tvQuestionOption3?.text = option3
//                    binding?.tvQuestionOption4?.text = option4
                    binding?.tvQuestionText?.text = question[index].question
                    selectedCategory = question[index].category!!
                    when {
                        question[index].category == "Verbs" -> {
                            binding?.viewModelImages = R.drawable.questoin_category_verbs
                            binding?.tvQuestionText?.visibility = View.VISIBLE
                            isListeningTypeQstn = false
                        }
                        question[index].category == "Grammar" -> {
                            binding?.viewModelImages = R.drawable.questoin_category_grammar
                            binding?.tvQuestionText?.visibility = View.VISIBLE
                            isListeningTypeQstn = false
                        }
                        question[index].category == "Correction" -> {
                            binding?.viewModelImages = R.drawable.questoin_category_correction
                            binding?.tvQuestionText?.visibility = View.VISIBLE
                            isListeningTypeQstn = false
                        }
                        question[index].category == "Listening" -> {
                            binding?.viewModelImages = R.drawable.questoin_category_listening
                            binding?.rlQuestionSpeak?.visibility = View.VISIBLE
                            isListeningTypeQstn = true
                        }
                        else -> {
                            binding?.viewModelImages = R.drawable.questoin_category_vocablary
                            binding?.tvQuestionText?.visibility = View.VISIBLE
                            isListeningTypeQstn = false
                        }
                    }
                } catch (e: Exception) {
                    Log.d("error", "error")
                }
            }
        })


        when {
            selectedCategory == "Verbs" -> {
                binding?.ivQuestionSuffel?.setBackgroundResource(R.drawable.image_suffel_animation_verb)
                suffelAnimation = binding?.ivQuestionSuffel?.background as AnimationDrawable
            }
            selectedCategory == "Grammar" -> {
                binding?.ivQuestionSuffel?.setBackgroundResource(R.drawable.image_suffel_animation_grammar)
                suffelAnimation = binding?.ivQuestionSuffel?.background as AnimationDrawable
            }
            selectedCategory == "Correction" -> {
                binding?.ivQuestionSuffel?.setBackgroundResource(R.drawable.image_suffel_animation_collection)
                suffelAnimation = binding?.ivQuestionSuffel?.background as AnimationDrawable
            }
            selectedCategory == "Listening" -> {
                binding?.ivQuestionSuffel?.setBackgroundResource(R.drawable.image_suffel_animation_listening)
                suffelAnimation = binding?.ivQuestionSuffel?.background as AnimationDrawable
            }
            else -> {
                binding?.ivQuestionSuffel?.setBackgroundResource(R.drawable.image_suffel_animation_vocab)
                suffelAnimation = binding?.ivQuestionSuffel?.background as AnimationDrawable
            }
        }

        binding?.ivQuestionSuffel?.visibility = View.VISIBLE
        suffelAnimation?.start()
        binding?.ivQuestionLargeAd?.visibility = View.VISIBLE
        if (mpCategorySound!=null && !isStoppedd){
            mpCategorySound?.start()
        }
        Handler().postDelayed({
            suffelAnimation?.stop()
            if (mpCategorySound!=null){
                if (mpCategorySound?.isPlaying!!){
                    mpCategorySound?.pause()
                    mpCategorySound?.seekTo(0)
                }
            }
            binding?.ivQuestionSuffel?.visibility = View.GONE
            binding?.ivQuestionLargeAd?.visibility = View.GONE

            val blinkk = AnimationUtils.loadAnimation(this,R.anim.blink)
            binding?.ivQuestionCategory?.visibility = View.VISIBLE
            binding?.ivQuestionCategory?.startAnimation(blinkk)
            Handler().postDelayed({
                binding?.ivQuestionCategory?.clearAnimation()

                val slideDown = AnimationUtils.loadAnimation(this,R.anim.slidedown)
                val fadeinnQuestionsAppear = AnimationUtils.loadAnimation(this,R.anim.fadeinn_question_appear)
                val fadeinnClock = AnimationUtils.loadAnimation(this,R.anim.clock_animation)
                val fadeinn = AnimationUtils.loadAnimation(this,R.anim.fadeinn)
                binding?.rlQuestionOptions?.visibility = View.VISIBLE
                binding?.rlQuestionOptions?.startAnimation(slideDown)
                binding?.llQuestionOptions?.visibility = View.VISIBLE
                if (mpSlideDown!=null && !isStoppedd){
                    mpSlideDown?.start()
                }
                Handler().postDelayed({
                    if (mpSlideDown!=null){
                        if (mpSlideDown?.isPlaying!!){
                            mpSlideDown?.pause()
                            mpSlideDown?.seekTo(0)
                        }
                    }

                    binding?.llQuestionQst?.visibility = View.VISIBLE
                    binding?.llQuestionQst?.startAnimation(fadeinn)
                    Handler().postDelayed({

                        binding?.tvQuestionOption1?.visibility = View.VISIBLE
                        binding?.tvQuestionOption2?.visibility = View.VISIBLE
                        binding?.tvQuestionOption3?.visibility = View.VISIBLE
                        binding?.tvQuestionOption4?.visibility = View.VISIBLE
                        binding?.tvQuestionOption1?.startAnimation(fadeinnQuestionsAppear)
                        binding?.tvQuestionOption2?.startAnimation(fadeinnQuestionsAppear)
                        binding?.tvQuestionOption3?.startAnimation(fadeinnQuestionsAppear)
                        binding?.tvQuestionOption4?.startAnimation(fadeinnQuestionsAppear)
                        if (mpQuestionsAppear!=null && !isStoppedd){
                            mpQuestionsAppear?.start()
                        }
                        Handler().postDelayed({
                            if (mpQuestionsAppear!=null){
                                if (mpQuestionsAppear?.isPlaying!!){
                                    mpQuestionsAppear?.pause()
                                    mpQuestionsAppear?.seekTo(0)
                                }
                            }

                            binding?.llQuestionClock?.visibility = View.VISIBLE
                            binding?.llQuestionClock?.startAnimation(fadeinnClock)
                            Handler().postDelayed({
                                binding?.tvQuestionTimer?.visibility = View.VISIBLE
                                binding?.tvQuestionTimer?.startAnimation(fadeinnQuestionsAppear)
                                Handler().postDelayed({
                                    if(isListeningTypeQstn && !isStoppedd){
                                        tts?.say(quest, processSelection(getSystemLanguage().toString()))
                                    }
                                    if (mpClock!=null && !isStoppedd){
                                        mpClock?.start()
                                    }
                                    if (!isStoppedd){
                                        countDownTimer.start()
                                    }
                                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    setImage(viewModel?.smallAd!!, binding?.ivQuestionSmallAd!!)
                                },500)
                            },400)
                        },500)
                    },400)

                },500)
            },500)
        }, 1500)
    }

    private fun getSystemLanguage(): String? {
        val language = Locale.getDefault().displayName
        val bcp47 = Locale.getDefault().toString()
        return String.format("%s (%s)", language, bcp47)
    }

    private fun processSelection(text: String): Locale? {
        val first = text.lastIndexOf("(")
        return Locale.US
//        if (first != -1) {
//            val loc = text.substring(first + 1, text.length)
//            Locale(loc)
//        } else {
//            Locale.US
//        }
    }

    override fun onBackPressed() {
        viewModel?.uploadHalfQuestionToServer()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        finish()
    }

    override fun onResume() {
        if (isClockSoundPlaying){
            mpTeacherDialog     = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
            mpDialog            = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
            mpFlag              = MediaPlayer.create(this@QuestionActivity, R.raw.ready_go)
            mpClock             = MediaPlayer.create(this@QuestionActivity, R.raw.clock)
            mpCategorySound     = MediaPlayer.create(this@QuestionActivity, R.raw.category_sound)
            mpJokerButtonClick  = MediaPlayer.create(this@QuestionActivity, R.raw.joker)
            mpCloud             = MediaPlayer.create(this@QuestionActivity, R.raw.cloud)
            mpButtonClick       = MediaPlayer.create(this@QuestionActivity, R.raw.button_click)
            mpWrongAns          = MediaPlayer.create(this@QuestionActivity, R.raw.wrong_answer)
            mpCorrectAns        = MediaPlayer.create(this@QuestionActivity, R.raw.correct_answer)
            mpSlideDown         = MediaPlayer.create(this@QuestionActivity, R.raw.answers_go_down_from_the_top)
            mpQuestionsAppear   = MediaPlayer.create(this@QuestionActivity, R.raw.questions_appear)
            mpClock?.start()
            resumeTimmer(reTime)
        }
        else{
            if (isStoppedd) {
                mpTeacherDialog     = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
                mpDialog            = MediaPlayer.create(this@QuestionActivity, R.raw.new_round)
                mpFlag              = MediaPlayer.create(this@QuestionActivity, R.raw.ready_go)
                mpClock             = MediaPlayer.create(this@QuestionActivity, R.raw.clock)
                mpCategorySound     = MediaPlayer.create(this@QuestionActivity, R.raw.category_sound)
                mpJokerButtonClick  = MediaPlayer.create(this@QuestionActivity, R.raw.joker)
                mpCloud             = MediaPlayer.create(this@QuestionActivity, R.raw.cloud)
                mpButtonClick       = MediaPlayer.create(this@QuestionActivity, R.raw.button_click)
                mpWrongAns          = MediaPlayer.create(this@QuestionActivity, R.raw.wrong_answer)
                mpCorrectAns        = MediaPlayer.create(this@QuestionActivity, R.raw.correct_answer)
                mpSlideDown         = MediaPlayer.create(this@QuestionActivity, R.raw.answers_go_down_from_the_top)
                mpQuestionsAppear   = MediaPlayer.create(this@QuestionActivity, R.raw.questions_appear)
                mpClock?.start()
                resumeTimmer(timer)
            }
        }
        isStoppedd = false
        isClockSoundPlaying = false
        super.onResume()
    }

    override fun onStop() {
        if (mpClock?.isPlaying!!){
            mpClock?.pause()
            mpClock?.seekTo(0)
            isClockSoundPlaying = true
            if (countDownTimer != null) {
                countDownTimer.cancel()
            }
        }
        mpTeacherDialog?.pause()
        mpDialog?.pause()
        mpFlag?.pause()
        mpClock?.pause()
        mpCategorySound?.pause()
        mpJokerButtonClick?.pause()
        mpCloud?.pause()
        mpButtonClick?.pause()
        mpWrongAns?.pause()
        mpCorrectAns?.pause()
        mpSlideDown?.pause()
        mpQuestionsAppear?.pause()

        mpTeacherDialog?.seekTo(0)
        mpDialog?.seekTo(0)
        mpFlag?.seekTo(0)
        mpClock?.seekTo(0)
        mpCategorySound?.seekTo(0)
        mpJokerButtonClick?.seekTo(0)
        mpCloud?.seekTo(0)
        mpButtonClick?.seekTo(0)
        mpWrongAns?.seekTo(0)
        mpCorrectAns?.seekTo(0)
        mpSlideDown?.seekTo(0)
        mpQuestionsAppear?.seekTo(0)

        mpClock?.release()
        mpClock = null

        isStoppedd = true
        super.onStop()
    }

    override fun onDestroy() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        tts?.shutDown()
        if (countDownTimer != null) {
            countDownTimer.cancel()
        }
        try {
            mpTeacherDialog?.release()
            mpDialog?.release()
            mpFlag?.release()
            mpClock?.release()
            mpCategorySound?.release()
            mpJokerButtonClick?.release()
            mpCloud?.release()
            mpButtonClick?.release()
            mpWrongAns?.release()
            mpCorrectAns?.release()
            mpSlideDown?.release()
            mpQuestionsAppear?.release()

            mpTeacherDialog = null
            mpDialog = null
            mpFlag = null
            mpClock = null
            mpCategorySound = null
            mpJokerButtonClick = null
            mpCloud = null
            mpButtonClick = null
            mpWrongAns    = null
            mpCorrectAns  = null
            mpSlideDown   = null
            mpQuestionsAppear = null
        }catch (e:Exception){Log.d("TAG","sound release catch")}
        super.onDestroy()
    }

}
