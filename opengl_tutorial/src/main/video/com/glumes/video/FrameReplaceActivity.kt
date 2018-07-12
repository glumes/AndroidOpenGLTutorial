package com.glumes.video

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.Matrix
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.glumes.importobject.AlphaTextureRect
import com.glumes.importobject.TextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10.GL_RGBA
import javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE


fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

class FrameReplaceActivity : AppCompatActivity() {


    private val width = 229
    private val height = 314

    private val mResultImage by lazy {
        findViewById<ImageView>(R.id.resultImage)
    }

    private var mOriginImage: TextureRect? = null
    private var mReplaceImage: TextureRect? = null
    private var mAlphaTextureRect: AlphaTextureRect? = null
    private var mOriginTextureId: Int = 0
    private var mReplaceTextureId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_replace)

        findViewById<Button>(R.id.glslChange).setOnClickListener { glslChange() }

        findViewById<Button>(R.id.blendChange).setOnClickListener { blendChange() }

    }

    private fun glslChange() {

        mResultImage.setImageResource(R.drawable.waiting)
        replace(false)
    }


    private fun blendChange() {
        mResultImage.setImageResource(R.drawable.waiting)

        replace(true)
    }


    private fun replace(isBlend: Boolean) {
        Observable.fromCallable {
            return@fromCallable initEgl()
        }.map {
            prepare(width, height)
            return@map it
        }.map {
            replaceContent(isBlend)
            return@map it
        }.map {
            val result = readPixel(width, height)
            it.destroy()
            return@map result
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mResultImage.setImageBitmap(it)
                }, {
                    showToast("replace failed")
                })
    }

    private fun prepare(w: Int, h: Int) {

        mOriginImage = TextureRect(resources, 2.0f, 2.0f)
        mReplaceImage = TextureRect(resources, 2.0f, 2.0f)
        mAlphaTextureRect = AlphaTextureRect(resources, 2.0f, 2.0f)

        mOriginTextureId = TextureHelper.loadTexture(this, R.drawable.origin)
        mReplaceTextureId = TextureHelper.loadTexture(this, R.drawable.replace)

        glViewport(0, 0, w, h)

        MatrixState.setInitStack()

        Matrix.setIdentityM(MatrixState.getVMatrix(), 0)

        Matrix.setIdentityM(MatrixState.getProMatrix(), 0)

    }

    private fun initEgl(): EGLRenderer {
        val renderer = EGLRenderer()
        renderer.create(width, height)
        return renderer
    }

    private fun replaceContent(isBlend: Boolean) {
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        mOriginImage?.drawSelf(mOriginTextureId)

        if (isBlend) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            mReplaceImage?.drawSelf(mReplaceTextureId)
            glDisable(GL_BLEND)
        } else {
            mAlphaTextureRect?.drawSelf(mReplaceTextureId)

        }
    }

    private fun readPixel(width: Int, height: Int): Bitmap {

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val pixels = IntBuffer.allocate(width * height)

        pixels.clear()

        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels)

        val pixelMirroredArray = IntArray(width * height)

        pixels.position(0)

        val pixelArray = pixels.array()

        // read 之后的 pixel 上下是反过来的,
        for (i in 0 until height) {
            for (j in 0 until width) {
                pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i * width + j]
            }
        }
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray))
        return bitmap
    }

}
