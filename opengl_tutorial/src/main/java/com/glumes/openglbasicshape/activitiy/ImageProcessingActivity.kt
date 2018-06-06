package com.glumes.openglbasicshape.activitiy

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseToolbarActivity
import com.glumes.openglbasicshape.imageprocessing.ImageProcessingView
import com.glumes.openglbasicshape.imageprocessing.processor.*

class ImageProcessingActivity : BaseToolbarActivity() {


    private val mImageProcessingView by lazy {
        findViewById<ImageProcessingView>(R.id.imageprocessing)
    }


    override fun updateToolbar() {
        setToolbarTitle(R.string.glsl_image_process)
        setToolbarMenu(R.menu.image_processing_menu)
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            mImageProcessingView.changeFilter(getProcessor(it.itemId))
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

    private fun getProcessor(id: Int): ImageProcess {
        var processor: ImageProcess = ImageProcess()
        when (id) {
            R.id.filter_origin -> {
                processor = ImageProcess()
            }
            R.id.filter_haha -> {
                processor = ImageHaHaProcess()
            }
            R.id.filter_smooth_filter -> {
                processor = ImageSmoothFilterProcess()
            }
            R.id.edge_detect -> {
                processor = ImageEdgeDetectProcess()
            }
            R.id.sharpen_handle -> {
                processor = ImageSharpenProcess()
            }
            R.id.relief_effect -> {
                processor = ImageReliefEffectProcess()
            }
        }
        return processor
    }
}


