package com.glumes.opengl_tutorial_practice.base

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.SparseArray

abstract class BaseFragmentToolbarActivity : BaseToolbarActivity() {


    val mFragSparseArray: SparseArray<Fragment> = SparseArray()

    override fun updateToolbar() {
        setTitleTextColor(Color.WHITE)
        setMenuId()
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            updateFragment(it.itemId)
            true
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()
    }


    abstract fun setMenuId()

    abstract fun updateFragment(itemId: Int)

    abstract fun initFragments()
}
