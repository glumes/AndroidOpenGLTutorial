package com.glumes.opengl_tutorial_practice

import android.os.Bundle
import com.glumes.opengl_tutorial_practice.base.BaseFragmentToolbarActivity
import com.glumes.opengl_tutorial_practice.chapter5.ContentFragment
import com.glumes.opengl_tutorial_practice.utils.ActivityUtils

class MainActivity : BaseFragmentToolbarActivity() {


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
    }

    override fun initFragments() {
        mFragSparseArray.put(R.id.chapter_1, ContentFragment())
        mFragSparseArray.put(R.id.chapter_2, ContentFragment())
        mFragSparseArray.put(R.id.chapter_3, ContentFragment())
    }


}
