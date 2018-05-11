package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.draw.BaseShape
import com.glumes.openglbasicshape.utils.ShaderHelper
import com.glumes.openglbasicshape.utils.TextureHelper
import io.reactivex.Observable
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 09/05/2018
 */
class CubeTexture(context: Context) : BaseShape(context) {

    private val U_VIEW_MATRIX = "u_ViewMatrix"
    private val U_MODEL_MATRIX = "u_ModelMatrix"
    private val U_PROJECTION_MATRIX = "u_ProjectionMatrix"
    private val A_POSITION = "a_Position"
    private val A_TEXTURE_COORDINATE = "a_TextureCoordinates"
    private val U_TEXTURE_UNIT = "u_TextureUnit"


    private var uModelMatrixAttr: Int = 0
    private var uViewMatrixAttr: Int = 0
    private var uProjectionMatrixAttr: Int = 0
    private var aPositionAttr: Int = 0
    private var aTextureCoordinateAttr: Int = 0
    private var uTextureUnitAttr: Int = 0

    private var mTextureId: Int = 0


    var vertexFloatBuffer = ByteBuffer
            .allocateDirect(8 * 6 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

    var textureFloagBuffer = ByteBuffer
            .allocateDirect(8 * 6 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

    val CubeSize = 1.0f

    val HalfCubeSize = CubeSize / 2

    var eyeX = 0.0f
    val eyeY = 0.0f
    var eyeZ = 2.0f


    val eyeDistance = 2.0f

    val lookX = 0.0f
    val lookY = 0.0f
    val lookZ = 0.0f

    val upX = 0.0f
    val upY = 1.0f
    val upZ = 0.0f

    init {

        LogUtil.d("cube texture")
        mProgram = ShaderHelper.buildProgram(mContext, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader)

        GLES20.glUseProgram(mProgram)

        initVertexData()

        initTextureData()

//        mVertexArray = VertexArray(vertexArrayData)
//        mTextureArray = VertexArray(textureArrayData)

        POSITION_COMPONENT_COUNT = 2

    }

    private fun initVertexData() {
        val faceLeft = -CubeSize / 2
        val faceRight = -faceLeft
        val faceTop = CubeSize / 2
        val faceBottom = -faceTop

        val vertices = floatArrayOf(
                faceLeft, faceBottom,
                faceRight, faceBottom,
                faceLeft, faceTop,
                faceRight, faceTop
        )
        for (it in 0..5) {
            vertexFloatBuffer.put(vertices)
        }
        vertexFloatBuffer.position(0)
    }


    private fun initTextureData() {
        val texCoords = floatArrayOf(
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        )
        for (it in 0..5) {
            textureFloagBuffer.put(texCoords)
        }
        textureFloagBuffer.position(0)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        aPositionAttr = GLES20.glGetAttribLocation(mProgram, A_POSITION)
        uModelMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_MODEL_MATRIX)
        uViewMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_VIEW_MATRIX)
        uProjectionMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_PROJECTION_MATRIX)

        aTextureCoordinateAttr = GLES20.glGetAttribLocation(mProgram, A_TEXTURE_COORDINATE)
        uTextureUnitAttr = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT)

        mTextureId = TextureHelper.loadTexture(mContext, R.drawable.texture)

        GLES20.glUniform1i(uTextureUnitAttr, 0)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)

        Matrix.rotateM(modelMatrix, 0, 200.0f, 0f, 1.0f, 0f)


        // Position the eye behind the origin.


        // We are looking toward the distance


        // Set our up vector. This is where our head would be pointing were we holding the camera.


        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(projectionMatrix, 0, left, ratio, bottom, top, near, far)


        Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe {
                    eyeX = eyeDistance * Math.cos((radian * num).toDouble()).toFloat()
                    eyeZ = eyeDistance * Math.sin((radian * num).toDouble()).toFloat()
                    num++
                    if (num > 360) {
                        num = 0
                    }
                    LogUtil.d("eyex is $eyeX eyez is $eyeZ num is $num")
                }


    }

    var num = 0
    val VERTEX_DATA_NUM = 360
    val radian = (2 * Math.PI / VERTEX_DATA_NUM).toFloat()


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        vertexFloatBuffer.position(0)
        GLES20.glVertexAttribPointer(aPositionAttr, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexFloatBuffer)
        GLES20.glEnableVertexAttribArray(aPositionAttr)

        textureFloagBuffer.position(0)
        GLES20.glVertexAttribPointer(aTextureCoordinateAttr, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, textureFloagBuffer)
        GLES20.glEnableVertexAttribArray(aTextureCoordinateAttr)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)

//        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.rotateM(modelMatrix, 0, 270f, 0f, 1f, 0f)


//        val time = SystemClock.uptimeMillis() % 10000L
//        val angleInDegrees = 360.0f / 10000.0f * time.toInt()

//        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 0.0f)

//
//        eyeX = (eyeDistance * Math.cos(angleInDegrees.toDouble())).toFloat()
//        eyeZ = (eyeDistance * Math.sin(angleInDegrees.toDouble())).toFloat()

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)




        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, modelMatrix, 0)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, viewMatrix, 0)
        GLES20.glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, projectionMatrix, 0)


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4)



        GLES20.glDisableVertexAttribArray(aPositionAttr)
        GLES20.glDisableVertexAttribArray(aTextureCoordinateAttr)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        GLES20.glDeleteProgram(mProgram)
    }
}