package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import com.glumes.openglbasicshape.draw.BaseShape
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 28/04/2018
 */

/**
 * 同时绘制多个纹理
 */
class MultiTexture(context: Context) : BaseShape(context) {


    private val mTriangleTexture = TriangleTexture(mContext)
    private val mRectangleTexture = RectangleTexture(mContext)
    private val mCircleTexture = CircleTexture(mContext)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)

        mTriangleTexture.onSurfaceCreated(gl, config)
        mRectangleTexture.onSurfaceCreated(gl, config)
        mCircleTexture.onSurfaceCreated(gl,config)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        mTriangleTexture.onSurfaceChanged(gl, width, height)
        mRectangleTexture.onSurfaceChanged(gl, width, height)
        mCircleTexture.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)

        mTriangleTexture.onDrawFrame(gl)
        mRectangleTexture.onDrawFrame(gl)
        mCircleTexture.onDrawFrame(gl)
    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        mTriangleTexture.onSurfaceDestroyed()
        mRectangleTexture.onSurfaceDestroyed()
        mCircleTexture.onSurfaceDestroyed()
    }
}