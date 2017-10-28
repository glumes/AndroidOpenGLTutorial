package com.gluems.openglkotlinnative

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    lateinit var glview: KotlinGLView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        glview = KotlinGLView(this)
        glview.requestFocus()
        glview.isFocusableInTouchMode = true
        setContentView(glview)
    }

    override fun onResume() {
        super.onResume()
        glview.onResume()
    }

    override fun onPause() {
        super.onPause()
        glview.onPause()
    }
}
