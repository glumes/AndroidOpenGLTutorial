package com.glumes.openglbasicshape.collision

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.collision.collisionfragment.AABBFragment

class CollisionActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.aabb_detect)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.collision_menu)
    }

    override fun initFragments() {
        mFragSparseArray.put(R.id.aabb_detect, AABBFragment())
    }
}
