package com.glumes.openglbasicshape.base

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.ACTIVITY_TITLE
import com.glumes.openglbasicshape.utils.ActivityUtils

abstract class BaseFragmentToolbarActivity : BaseToolbarActivity() {


    val mFragSparseArray: SparseArray<Fragment> = SparseArray()

    override fun updateToolbar() {
        setToolbarTitle(intent.getStringExtra(ACTIVITY_TITLE))
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

    protected fun updateFragment(itemId: Int) {
        ActivityUtils.replaceFragment(
                mFragSparseArray.get(itemId),
                supportFragmentManager,
                R.id.baseContainer
        )
    }

    abstract fun initFragments()
}
