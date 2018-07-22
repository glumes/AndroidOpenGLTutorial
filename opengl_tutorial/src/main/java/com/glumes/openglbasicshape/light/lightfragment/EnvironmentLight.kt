package com.glumes.openglbasicshape.light.lightfragment

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import com.glumes.importobject.Ball
import com.glumes.importobject.utils.BallTypeHelper
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */
class EnvironmentLight : Fragment() {

    lateinit var surfaceView: AmbientView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = AmbientView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true

        return inflater.inflate(R.layout.fragment_light, container, false).also {
            it.findViewById<LinearLayout>(R.id.main_liner).addView(surfaceView)
            it.findViewById<SeekBar>(R.id.SeekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    surfaceView.lightOffset = (seekBar.max / 2.0f - progress) / (seekBar.max / 2.0f * -4)
                }

            })
        }
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

class AmbientView(var mContext: Context) : GLSurfaceView(mContext) {


    var mRenderer: AmbientRenderer
    lateinit var mShape: Ball
    var mPreviousY = 0f
    var mPreviousX = 0f

    var TOUCH_SCALE_FACTOR = 180.0f / 320
    var lightOffset = -4f

    init {

        mRenderer = AmbientRenderer()
        setEGLContextClientVersion(3)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY
                val dx = x - mPreviousX
                mShape.yAngle += dx * TOUCH_SCALE_FACTOR
                mShape.xAngle += dy * TOUCH_SCALE_FACTOR
            }
        }
        mPreviousX = y
        mPreviousX = x
        return true
    }

    inner class AmbientRenderer : GLSurfaceView.Renderer {

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(-1.2f, 0f, 0f)
            mShape.drawSelf()
            MatrixState.popMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(1.2f, 0f, 0f)
            mShape.drawSelf()
            MatrixState.popMatrix()

            MatrixState.popMatrix()

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            MatrixState.setInitStack()
            MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0f)
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0f, 0f, 0f, 0f)
            mShape = Ball(mContext.resources, BallTypeHelper.AmbientBall)

        }

    }
}

