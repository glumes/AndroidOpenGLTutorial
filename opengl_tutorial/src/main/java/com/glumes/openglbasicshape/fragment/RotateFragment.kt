package com.glumes.openglbasicshape.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.draw.BaseShapeView
import com.glumes.openglbasicshape.draw.texture.BaseCube
import com.glumes.openglbasicshape.draw.texture.CubeTexture
import com.glumes.openglbasicshape.draw.texture.CubeTexture2
import com.glumes.openglbasicshape.renderers.BaseRenderer
import com.glumes.openglbasicshape.utils.MatrixState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */

class RotateFragment : Fragment() {

    lateinit var surfaceView: BaseShapeView
    lateinit var renderder: RotateRenderer
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        renderder = RotateRenderer(context)
        surfaceView = RotateSurface(context, renderder)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }
}


@SuppressLint("ViewConstructor")
class RotateSurface(context: Context, renderer: BaseRenderer) : BaseShapeView(context, renderer) {

    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    private var mPreviewX: Float = 0f
    private var mPreviewY: Float = 0f


    init {
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviewY
                val dx = x - mPreviewX
            }
        }
        mPreviewX = x
        mPreviewY = y
        return true
    }
}


class RotateRenderer(context: Context) : BaseRenderer(context) {

    lateinit var cube: RotateCube

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        cube = RotateCube(mContext)
        cube.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        cube.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        cube.onDrawFrame(gl)
    }

}

class RotateCube(context: Context) : BaseCube(context) {

    private var isPlus = true
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe {

                    if (eyeZ in 1.5f..5f) {
                        if (isPlus) {
                            LogUtil.d("is plus ")
                            eyeZ += 0.2f
                        } else {
                            LogUtil.d("is not plus ")
                            eyeZ -= 0.2f
                        }
                    }

                    if (eyeZ <= 1.5f) {
                        eyeZ = 1.5f
                        isPlus = true
                    }

                    if (eyeZ >= 5f) {
                        eyeZ = 5f
                        isPlus = false
                    }
                }
        MatrixState.rotate(-30f, 0f, 0f, 1f)
    }


    override fun onDrawCubePre() {
        super.onDrawCubePre()

        // 控制调整相机来观察不同的面
        MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, MatrixState.getVMatrix(), 0)
    }

}

