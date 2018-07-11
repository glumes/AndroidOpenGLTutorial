package com.glumes.video

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.glumes.openglbasicshape.R

class FrameReplaceActivity : AppCompatActivity() {


    private val mResultImage by lazy {
        findViewById<ImageView>(R.id.resultImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_replace)

        findViewById<Button>(R.id.glslChange).setOnClickListener { glslChange() }

        findViewById<Button>(R.id.blendChange).setOnClickListener { blendChange() }

    }

    private fun glslChange() {

    }

    private fun blendChange() {

    }

}
