package com.duke.diskoverusosyal.utils

import android.view.View
import androidx.viewpager.widget.ViewPager

class ViewPagerTransformation(private val mPager: ViewPager): ViewPager.PageTransformer {
    override fun transformPage(page: View, p1: Float) {
        val pageWidth = mPager.measuredWidth -
                mPager.paddingLeft - mPager.paddingRight
        val paddingLeft = mPager.paddingLeft
        val transformPos = (page.left - (mPager.scrollX + paddingLeft)).toFloat() / pageWidth
        when {
            transformPos < -1 -> {
                page.scaleY = 0.9f
                page.translationY = 0f
            }
            transformPos <= 1 -> {
                page.scaleY = 1f
            }
            else -> {
                page.scaleY = 0.9f
                page.translationY = 0f
            }
        }
    }
}