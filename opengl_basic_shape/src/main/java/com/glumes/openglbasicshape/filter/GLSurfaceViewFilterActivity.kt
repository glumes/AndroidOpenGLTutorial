package com.glumes.openglbasicshape.filter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.glumes.camera.Camera2
import com.glumes.openglbasicshape.R

class GLSurfaceViewFilterActivity : AppCompatActivity() {


    var mView: FilterSurfaceView? = null

    lateinit var mCamera2: Camera2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_glsurface_view_filter)


        mCamera2 = Camera2(this)

        if (mCamera2.openCamera()) {
            mView = FilterSurfaceView(this)

            mView!!.setCamera(mCamera2)
        }


        if (mView == null) {
            setContentView(R.layout.activity_main)
        } else {
            setContentView(mView)
        }


    }

    override fun onResume() {
        super.onResume()
        mView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView?.onPause()
    }


}
