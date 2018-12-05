package com.glumes.openglbasicshape.texturemix

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.glumes.importobject.TextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureMixActivity : AppCompatActivity() {


    lateinit var mGLSurfaceView: GLSurfaceView
    lateinit var mGLSurfaceView2: GLSurfaceView
    lateinit var mRenderer: MixRenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_mix)


        mGLSurfaceView = findViewById(R.id.texturemix)

        mRenderer = MixRenderer(this)

        mGLSurfaceView.setEGLContextClientVersion(2)

        mGLSurfaceView.setRenderer(mRenderer)
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        mGLSurfaceView = findViewById(R.id.texturemix2)

//        mRenderer = MixRenderer(this)


        mGLSurfaceView.setEGLContextClientVersion(2)

        mGLSurfaceView.setRenderer(MixRenderer(this))
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

    }

    inner class MixRenderer(val mContext: Context) : GLSurfaceView.Renderer {

        lateinit var mTextureRect: MixTexture
        lateinit var mvpMatrix: FloatArray
        var mTextureId: Int = 0
        var mTextureId2: Int = 0

        override fun onDrawFrame(gl: GL10?) {

            mTextureRect.drawSelf(mTextureId, mTextureId2, mvpMatrix)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            mTextureRect = MixTexture(mContext.resources, 2f, 2f)

            mTextureId = TextureHelper.loadTexture(mContext, R.drawable.texture)

            mTextureId2 = TextureHelper.loadTexture(mContext, R.drawable.tree)

            MatrixState.setInitStack()

            mvpMatrix = FloatArray(16)

            Matrix.setIdentityM(mvpMatrix, 0)

//            Matrix.scaleM(mvpMatrix, 0, 0.5f, 0f, 0f)

//            Matrix.rotateM(mvpMatrix, 0, 40f, 1f, 0f, 0f)
        }

    }
}
