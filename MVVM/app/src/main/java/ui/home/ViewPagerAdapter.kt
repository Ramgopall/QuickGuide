package com.triviallanguages.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.triviallanguages.R
import com.triviallanguages.databinding.ViewpagerChooseLevelBinding
import kotlinx.android.synthetic.main.viewpager_choose_level.view.*


class ViewPagerAdapter(
    view: Context,
    private val images: List<Int>,
    private val names: List<Int>,
    private val userLevel: Int?
) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private val contextt:Context = view.applicationContext

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = contextt.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val view = DataBindingUtil.inflate<ViewpagerChooseLevelBinding>(layoutInflater!!,R.layout.viewpager_choose_level, null,false)

        view.viewModelNames = contextt.getString(names[position])
        view.viewModelImages = images[position]
        if (position <= userLevel!!){
            view.root.ivVpChooseLevelLock.visibility = View.GONE
            view.root.ivVpChooseLevel.alpha = 1f
        }else{
            view.root.ivVpChooseLevelLock.visibility = View.VISIBLE
           // view.root.ivVpChooseLevel.alpha = 0.4f
            view.root.ivVpChooseLevel.alpha = 1f
        }
        view.root.ivVpChooseLevelDegree.visibility = if (position >= userLevel){View.GONE}else{View.VISIBLE}

        val vp = container as ViewPager
        vp.addView(view.root,0)
        return view.root

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as ViewpagerChooseLevelBinding
        vp.removeView(view.root)
    }
}

