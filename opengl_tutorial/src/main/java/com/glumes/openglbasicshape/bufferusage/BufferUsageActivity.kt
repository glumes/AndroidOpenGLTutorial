package com.glumes.openglbasicshape.bufferusage

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity

class BufferUsageActivity : BaseFragmentToolbarActivity() {


    override fun setMenuId() {
        setToolbarMenu(R.menu.buffer_usage_menu)
    }

    override fun initFragments() {
        mFragSparseArray.put(R.id.vertex_buffer_usage, VBOFragment())
        mFragSparseArray.put(R.id.vertex_array_object_usage, VAOFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.vertex_buffer_usage)
    }
}
