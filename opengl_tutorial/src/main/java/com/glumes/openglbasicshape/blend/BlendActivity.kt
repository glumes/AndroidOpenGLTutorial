package com.glumes.openglbasicshape.blend

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.blend.blendfragment.BlendFragment
import com.glumes.openglbasicshape.blend.blendfragment.ETCFragment

class BlendActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.blend)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.blend_and_etc_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.blend, BlendFragment())
        mFragSparseArray.put(R.id.etc_texture, ETCFragment())
    }

}
