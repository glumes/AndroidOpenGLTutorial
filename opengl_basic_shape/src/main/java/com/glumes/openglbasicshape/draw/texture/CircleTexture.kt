package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.Matrix
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.draw.BaseShape
import com.glumes.openglbasicshape.utils.ShaderHelper
import com.glumes.openglbasicshape.utils.TextureHelper
import com.glumes.openglbasicshape.utils.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 28/04/2018
 * 球形纹理图
 */
class CircleTexture(context: Context) : BaseShape(context) {


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

    val VERTEX_DATA_NUM = 360


    private var vertexArrayData = FloatArray(VERTEX_DATA_NUM * 2 + 4)

    private var textureArrayData = FloatArray(VERTEX_DATA_NUM * 2 + 4)

    var mVertexArray: VertexArray

    var mTextureArray: VertexArray

    // 分成 360 份，每一份的弧度
    val radian = (2 * Math.PI / VERTEX_DATA_NUM).toFloat()

    // 绘制的半径
    val radius = 0.8f

    val textureRadius = 0.4f

    init {

        mProgram = ShaderHelper.buildProgram(mContext, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader)

        GLES20.glUseProgram(mProgram)

        initVertexData()

        initTextureData()

        mVertexArray = VertexArray(vertexArrayData)
        mTextureArray = VertexArray(textureArrayData)

        POSITION_COMPONENT_COUNT = 2

    }

    private fun initVertexData() {
        vertexArrayData[0] = 0f
        vertexArrayData[1] = 0f

        for (it in 0..VERTEX_DATA_NUM) {
            vertexArrayData[2 * it + 2] = (radius * Math.cos((radian * it).toDouble())).toFloat()
            vertexArrayData[2 * it + 2 + 1] = (radius * Math.sin((radian * it).toDouble())).toFloat()
        }

        vertexArrayData[VERTEX_DATA_NUM * 2 + 2] = (radius * Math.cos(radian.toDouble())).toFloat()
        vertexArrayData[VERTEX_DATA_NUM * 2 + 2 + 1] = (radius * Math.sin(radian.toDouble())).toFloat()

    }


    private fun initTextureData() {
        textureArrayData[0] = 0.5f
        textureArrayData[1] = 0.5f
        for (it in 0..VERTEX_DATA_NUM) {
            textureArrayData[2 * it + 2] = (textureRadius * Math.cos((radian * it).toDouble())).toFloat() + 0.5f
            textureArrayData[2 * it + 2 + 1] = (textureRadius * Math.sin((radian * it).toDouble())).toFloat() + 0.5f
        }
        textureArrayData[VERTEX_DATA_NUM * 2 + 2] = (textureRadius * Math.cos(radian.toDouble())).toFloat() + 0.5f
        textureArrayData[VERTEX_DATA_NUM * 2 + 2 + 1] = (textureRadius * Math.sin(radian.toDouble())).toFloat() + 0.5f
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
        Matrix.rotateM(modelMatrix,0,180f,1f,0f,0f)
        Matrix.scaleM(modelMatrix,0,0.6f,0.6f,0f)
        Matrix.translateM(modelMatrix,0,1.0f,1.0f,0f)

        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        GLES20.glViewport(0, 0, width, height)

        val aspectRatio = if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()

        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 10f)
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, modelMatrix, 0)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, viewMatrix, 0)
        GLES20.glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, projectionMatrix, 0)


        mVertexArray.setVertexAttribPointer(0, aPositionAttr, POSITION_COMPONENT_COUNT, 0)
        mTextureArray.setVertexAttribPointer(0, aTextureCoordinateAttr, POSITION_COMPONENT_COUNT, 0)


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDrawArrays(GL_TRIANGLE_FAN, 0, VERTEX_DATA_NUM + 2);

        GLES20.glDisableVertexAttribArray(aPositionAttr)
        GLES20.glDisableVertexAttribArray(aTextureCoordinateAttr)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        GLES20.glDeleteProgram(mProgram)
    }
}