package com.glumes.openglbasicshape.multitest

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.multitest.testfragment.ScissorTestFragment

class MultiTestActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.scissor_test)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.scissor_and_test_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.scissor_test, ScissorTestFragment())
    }

}
