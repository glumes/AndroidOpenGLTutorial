package com.glumes.openglbasicshape.blend

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.blend.blendfragment.BlendFragment

class BlendActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.scissor_test)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.blend_and_etc_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.scissor_test, BlendFragment())
    }

}
