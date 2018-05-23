package com.glumes.openglbasicshape.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.glumes.openglbasicshape.R
import java.io.File
import java.io.FileOutputStream


/**
 * Created by glumes on 22/05/2018
 */
class PhotoEditor @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var mGLRootView: GLRootView

    init {
        LayoutInflater.from(context).inflate(R.layout.photo_editor, this, true)

        mGLRootView = findViewById(R.id.mGLSurfaceView)
    }

    fun onResume() {
        mGLRootView.onResume()
    }

    fun onPause() {
        mGLRootView.onPause()
    }

    fun addView() {
        val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        val text = TextView(context)

        text.text = "this is text"
        text.textSize = 30f
        text.setTextColor(Color.RED)

        text.rotationX = 40f

        addView(text, params)


    }

    @SuppressLint("StaticFieldLeak")
    fun getPhoto() {

        val imagePath = Environment.getExternalStorageDirectory().toString() + File.separator + "test.png"
        val TAG = "TEST"

        object : AsyncTask<String, String, Exception>() {

            override fun onPreExecute() {
                super.onPreExecute()
                setDrawingCacheEnabled(false)
            }

            @SuppressLint("MissingPermission")
            override fun doInBackground(vararg strings: String): Exception? {
                // Create a media file name

                Thread.sleep(1000)

                val file = File(imagePath)
                file.createNewFile()

                try {
                    val out = FileOutputStream(file, false)
                    setDrawingCacheEnabled(true)
                    val drawingCache = getDrawingCache()
                    drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                    out.close()
                    Log.d(TAG, "Filed Saved Successfully")
                    return null
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(TAG, "Failed to save File")
                    return e
                }

            }


            override fun onPostExecute(e: Exception?) {
                super.onPostExecute(e)
            }

        }.execute()
    }
}


