package com.glumes.openglbasicshape.light

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.light.lightfragment.*

class LightActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.no_light)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.light_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.no_light, NoLight())
        mFragSparseArray.put(R.id.environment_light, EnvironmentLight())
        mFragSparseArray.put(R.id.scatter_light, ScatterLight())
        mFragSparseArray.put(R.id.mirror_light, MirrorLight())
        mFragSparseArray.put(R.id.mix_light, MixLight())
//        mFragSparseArray.put(R.id.direction_light, DirectionLight())
    }

}