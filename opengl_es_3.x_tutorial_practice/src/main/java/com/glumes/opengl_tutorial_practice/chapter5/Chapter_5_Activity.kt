package com.glumes.opengl_tutorial_practice.chapter5

import android.os.Bundle
import com.glumes.opengl_tutorial_practice.R
import com.glumes.opengl_tutorial_practice.base.BaseFragmentToolbarActivity
import com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1.AttributeMatFragment
import com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1.Fragment_5_1
import com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1.Fragment_5_13
import com.glumes.opengl_tutorial_practice.utils.ActivityUtils

class Chapter_5_Activity : BaseFragmentToolbarActivity() {


    override fun updateFragment(itemId: Int) {
        ActivityUtils.replaceFragment(
                mFragSparseArray.get(itemId),
                supportFragmentManager,
                R.id.baseContainer
        )
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.chapter_5_menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(R.string.chapter_5)
        ActivityUtils.replaceFragment(
                mFragSparseArray.get(R.id.chapter_1),
                supportFragmentManager,
                R.id.baseContainer
        )
    }

    override fun initFragments() {
        mFragSparseArray.put(R.id.chapter_1, Fragment_5_1())
        mFragSparseArray.put(R.id.chapter_2, Fragment_5_13())
        mFragSparseArray.put(R.id.chapter_3, AttributeMatFragment())
    }
}
