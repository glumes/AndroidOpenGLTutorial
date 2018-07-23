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
import com.glumes.importobject.light.DiffuseBall
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * @Author  glumes
 */
class ScatterLight : Fragment() {

    lateinit var surfaceView: DiffuseView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = DiffuseView(context!!)
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
                    surfaceView.lightOffset = (seekBar.max / 2.0f - progress) / (seekBar.max / 2.0f) * -4
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


class DiffuseView(var mContext: Context) : GLSurfaceView(mContext) {

    private val TOUCH_SCALE_FACTOR = 180.0f / 320//角度缩放比例
    private var mRenderer: DiffuseRenderer//场景渲染器
    lateinit var ball: DiffuseBall//球

    var lightOffset = -4f//灯光的位置或方向的偏移量

    private var mPreviousY: Float = 0.toFloat()//上次的触控位置Y坐标
    private var mPreviousX: Float = 0.toFloat()//上次的触控位置X坐标

    init {
        this.setEGLContextClientVersion(3) //设置使用OPENGL ES3.0
        mRenderer = DiffuseRenderer()    //创建场景渲染器
        setRenderer(mRenderer)                //设置渲染器
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY//设置渲染模式为主动渲染
    }


    override fun onTouchEvent(e: MotionEvent): Boolean {
        val y = e.y
        val x = e.x
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY//计算触控笔Y位移
                val dx = x - mPreviousX//计算触控笔X位移
                ball.yAngle += dx * TOUCH_SCALE_FACTOR//设置填充椭圆绕y轴旋转的角度
                ball.xAngle += dy * TOUCH_SCALE_FACTOR//设置填充椭圆绕x轴旋转的角度
            }
        }
        mPreviousY = y//记录触控笔位置
        mPreviousX = x//记录触控笔位置
        return true
    }

    private inner class DiffuseRenderer : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            //设置光源位置
            MatrixState.setLightLocation(lightOffset, 0f, 1.5f)
            //保护现场
            MatrixState.pushMatrix()
            //绘制球
            MatrixState.pushMatrix()
            MatrixState.translate(-1.2f, 0f, 0f)

            ball.drawSelf()
            MatrixState.popMatrix()
            //绘制球
            MatrixState.pushMatrix()
            MatrixState.translate(1.2f, 0f, 0f)
            ball.drawSelf()
            MatrixState.popMatrix()
            //恢复现场
            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            MatrixState.setInitStack()
            MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0f)
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0f, 0f, 0f, 1.0f)
            //创建球对象
            ball = DiffuseBall(mContext.resources)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE)
        }
    }

//    fun setLightOffset(lightOffset: Float) {
//        this.lightOffset = lightOffset
//    }
}

