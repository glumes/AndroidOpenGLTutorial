package com.glumes.openglbasicshape

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.glumes.openglbasicshape.utils.FontUtil

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        val content = arrayOf("content")
        val bitmap = FontUtil.generateWLT(content, 512, 512)

        findViewById<ImageView>(R.id.imageView2).setImageBitmap(bitmap)
    }
}
