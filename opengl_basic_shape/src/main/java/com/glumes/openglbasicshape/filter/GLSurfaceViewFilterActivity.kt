package com.glumes.openglbasicshape.filter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.glumes.camera.Camera2
import com.glumes.openglbasicshape.R

class GLSurfaceViewFilterActivity : AppCompatActivity() {


    var mView: FilterSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        mView = FilterSurfaceView(this)

        setContentView(mView)

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
