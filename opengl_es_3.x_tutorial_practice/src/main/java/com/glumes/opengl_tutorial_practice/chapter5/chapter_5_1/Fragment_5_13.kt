package com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.glumes.opengl_tutorial_practice.shape.Cube
import com.glumes.opengl_tutorial_practice.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import com.glumes.opengl_tutorial_practice.utils.Constant.ratio


/**
 * Created by glumes on 11/05/2018
 */


class Fragment_5_13 : Fragment() {


    lateinit var surfaceView: RendererSurface_5_13

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = RendererSurface_5_13(context)
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


class RendererSurface_5_13(context: Context) : GLSurfaceView(context) {

    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    private var mPreviousX: Float = 0f
    private var mPreviousY: Float = 0f

    var mRenderer: SceneRenderer_5_13

    var yAngle = 0f//总场景绕y轴旋转的角度

    init {
        setEGLContextClientVersion(3)
        mRenderer = SceneRenderer_5_13(context)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mPreviousX//计算触控位置X位移
                yAngle += dx * TOUCH_SCALE_FACTOR//设置三角形对绕y轴旋转角度
                mRenderer.yAngle = yAngle
            }
        }
        mPreviousX = x
        return true
    }
}

class SceneRenderer_5_13(val mContext: Context) : GLSurfaceView.Renderer {

    lateinit var cube: Cube

    var yAngle = 0f//总场景绕y轴旋转的角度

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        cube = Cube(mContext.resources)
        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        //打开背面剪裁
        GLES30.glEnable(GLES30.GL_CULL_FACE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视口的大小及位置
        GLES30.glViewport(0, 0, width, height)
        //计算视口的宽高比
        ratio = width.toFloat() / height

        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio * 0.7f, ratio * 0.7f, -0.7f, 0.7f, 1f, 10f)
        //调用此方法产生摄像机矩阵
        MatrixState.setCamera(0f, 0.5f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        //初始化变换矩阵
        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        //清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        //保护现场,栈中保存了当前的变换矩阵
        MatrixState.pushMatrix()
        //绕Y轴旋转（实现总场景旋转）
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        //绘制左侧立方体，栈中保存了绘制左侧立方体前的变换矩阵

        // 调用了 pushMatrix 之后，这段操作，变换矩阵的起点是 总场景旋转，然后进行自己单独的操作
        // 操作结束之后，再进行出栈操作，操作之后的起点又是 总场景旋转了。
        MatrixState.pushMatrix()
        MatrixState.translate(-3f, 0f, 0f)
        MatrixState.rotate(60f, 0f, 1f, 0f)
        cube.drawSelf()
        MatrixState.popMatrix()

        //绘制右侧立方体，栈中保存了绘制右侧立方体前的变换矩阵
        MatrixState.pushMatrix()
        MatrixState.translate(3f, 0f, 0f)
        MatrixState.rotate(-60f, 0f, 1f, 0f)
        cube.drawSelf()
        MatrixState.popMatrix()

        //恢复现场
        MatrixState.popMatrix()
    }

}

