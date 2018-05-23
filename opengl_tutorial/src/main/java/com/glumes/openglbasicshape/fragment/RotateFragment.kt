package com.glumes.openglbasicshape.fragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.draw.BaseShapeView
import com.glumes.openglbasicshape.draw.texture.BaseCube
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        renderder = RotateRenderer(context!!)
        surfaceView = BaseShapeView(context, renderder)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
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


    val eyeDistance = 2.0f
    var num = 0
    var RotateNum = 180
    val radian = (2 * Math.PI / RotateNum).toFloat()

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        var isPlus = true
        var distance = eyeZ
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    distance = if (isPlus) distance + 0.1f else distance - 0.1f

                    if (distance < 2.0f) {
                        isPlus = true
                    }

                    if (distance > 5.0f) {
                        isPlus = false
                    }
//                    eyeZ = distance

                    eyeX = eyeDistance * Math.sin((radian * num).toDouble()).toFloat()
                    eyeZ = distance * Math.cos((radian * num).toDouble()).toFloat()
                    num++
                    if (num > 360) {
                        num = 0
                    }
                    updateCamera()
                }
        // 将物体调整一下，可以看到三个面
        MatrixState.rotate(-45f, 0f, 1f, 0f)
        MatrixState.rotate(45f, 1f, 0f, 0f)
    }

    private fun updateCamera() {
        MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, MatrixState.getVMatrix(), 0)
    }
}

