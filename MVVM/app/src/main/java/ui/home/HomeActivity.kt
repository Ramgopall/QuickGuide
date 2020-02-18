package com.triviallanguages.ui.home

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.triviallanguages.Constant.Companion.imageList
import com.triviallanguages.Constant.Companion.nameList
import com.triviallanguages.R
import com.triviallanguages.databinding.ActivityHomeBinding
import com.triviallanguages.ui.question.QuestionActivity
import com.triviallanguages.util.viewPagerTransformers.ZoomOutTranformer
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeActivity : AppCompatActivity(), HomeListener, KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private var binding: ActivityHomeBinding? = null
    var level = 1
    var mpButtonClick:MediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        binding?.viewModel = viewModel
        viewModel.homeListener = this
        viewModel.getUserLevel()
    }

    override fun onLevelGet(level: Int) {
        this.level = level
        binding?.vpHome?.also {
            it.offscreenPageLimit = imageList.size
            it.beginFakeDrag()
            it.setPageTransformer(false, ZoomOutTranformer())
            it.adapter = ViewPagerAdapter(this, imageList, nameList,level.minus(1))
            it.currentItem = level.minus(1)
        }
    }

    override fun onPreviousClick() {
        val position = binding?.vpHome?.currentItem
        if (position!! > 0) {
            binding?.vpHome?.currentItem = position.minus(1)
            if(position.minus(1) == 0){
                binding?.ivHomePrevious?.visibility = View.INVISIBLE
            }
            else{
                binding?.ivHomePrevious?.visibility = View.VISIBLE
                binding?.ivHomeNext?.visibility = View.VISIBLE
            }

            if(position.minus(1) <= (level-1)){
                binding?.ivHomePlay?.setImageResource(R.drawable.choose_level_play_button)
            }
            else{
                binding?.ivHomePlay?.setImageResource(R.drawable.choose_level_learn_button)
            }
        }
    }

    override fun onNextClick() {
        val position = binding?.vpHome?.currentItem
        if (position!! < 64) {
            binding?.vpHome?.currentItem = position.plus(1)
            if(position.plus(1) == 64){
                binding?.ivHomeNext?.visibility = View.INVISIBLE
            }
            else{
                binding?.ivHomePrevious?.visibility = View.VISIBLE
                binding?.ivHomeNext?.visibility = View.VISIBLE
            }

            if(position.plus(1) <= (level-1)){
                binding?.ivHomePlay?.setImageResource(R.drawable.choose_level_play_button)
            }
            else{
                binding?.ivHomePlay?.setImageResource(R.drawable.choose_level_learn_button)
            }
        }
    }

    override fun onPlayClick() {
        mpButtonClick = MediaPlayer.create(this@HomeActivity, R.raw.button_click)
        mpButtonClick?.start()
        val tempLevel = binding?.vpHome?.currentItem?.plus(1)!!
        if (tempLevel <= level) {
            Intent(this, QuestionActivity::class.java).also {
                it.putExtra("level", tempLevel)
                startActivity(it)
            }
        }
    }

    override fun onStop() {
        mpButtonClick?.stop()
        mpButtonClick?.release()
        super.onStop()
    }

}