package com.glumes.openglbasicshape.light

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.blend.blendfragment.BlendFragment
import com.glumes.openglbasicshape.blend.blendfragment.ETCFragment

class LightActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.no_light)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.light_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.no_light, BlendFragment())
        mFragSparseArray.put(R.id.environment_light, BlendFragment())
        mFragSparseArray.put(R.id.scatter_light, ETCFragment())
        mFragSparseArray.put(R.id.mirror_light, ETCFragment())
        mFragSparseArray.put(R.id.mix_light, ETCFragment())
        mFragSparseArray.put(R.id.direction_light, ETCFragment())
    }

}