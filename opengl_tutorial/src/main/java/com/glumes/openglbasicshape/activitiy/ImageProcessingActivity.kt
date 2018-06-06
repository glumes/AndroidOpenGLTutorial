package com.glumes.openglbasicshape.activitiy

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseToolbarActivity
import com.glumes.openglbasicshape.imageprocessing.ImageProcessingView

class ImageProcessingActivity : BaseToolbarActivity() {


    private val mImageProcessingView by lazy {
        findViewById<ImageProcessingView>(R.id.imageprocessing)
    }


    override fun updateToolbar() {
        setToolbarTitle(R.string.glsl_image_process)
        setToolbarMenu(R.menu.image_processing_menu)
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            mImageProcessingView.changeFilter()
            true
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_handle)
    }

    override fun onResume() {
        super.onResume()
        mImageProcessingView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mImageProcessingView.onPause()
    }
}


