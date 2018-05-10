package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.Matrix
import com.glumes.comlib.LogUtil
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.draw.BaseShape
import com.glumes.openglbasicshape.utils.ShaderHelper
import com.glumes.openglbasicshape.utils.TextureHelper
import com.glumes.openglbasicshape.utils.VertexArray
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author glumes
 */
class TriangleTexture(context: Context) : BaseShape(context) {


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


    private var vertexArrayData = floatArrayOf(

            0.5f, 0f,
            0f, 1.0f,
            1.0f, 1.0f
    )

    private var textureArrayData = floatArrayOf(
            0.5f, 0f,
            0f, 1.0f,
            1.0f, 1.0f
    )

    var mVertexArray: VertexArray
    var mTextureArray: VertexArray

    init {
        mProgram = ShaderHelper.buildProgram(mContext, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader)

        glUseProgram(mProgram)

        mVertexArray = VertexArray(vertexArrayData)
        mTextureArray = VertexArray(textureArrayData)


        POSITION_COMPONENT_COUNT = 2
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)

        glClearColor(0f, 0f, 0f, 1.0f)
        aPositionAttr = glGetAttribLocation(mProgram, A_POSITION)
        uModelMatrixAttr = glGetUniformLocation(mProgram, U_MODEL_MATRIX)
        uViewMatrixAttr = glGetUniformLocation(mProgram, U_VIEW_MATRIX)
        uProjectionMatrixAttr = glGetUniformLocation(mProgram, U_PROJECTION_MATRIX)


        aTextureCoordinateAttr = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATE)
        uTextureUnitAttr = glGetUniformLocation(mProgram, U_TEXTURE_UNIT)


        mTextureId = TextureHelper.loadTexture(mContext, R.drawable.texture)

        glUniform1i(uTextureUnitAttr, 0)


        val intBuffer: IntBuffer = IntBuffer.allocate(1)

        glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, intBuffer)

        LogUtil.d("max combined texture image units " + intBuffer[0])

        Matrix.setIdentityM(modelMatrix, 0)

//        Matrix.rotateM(modelMatrix,0,180f,1f,0f,0f)
        Matrix.translateM(modelMatrix,0,0f,0.5f,0f)

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

        glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        glUniformMatrix4fv(uModelMatrixAttr, 1, false, modelMatrix, 0)
        glUniformMatrix4fv(uViewMatrixAttr, 1, false, viewMatrix, 0)
        glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, projectionMatrix, 0)

        mVertexArray.setVertexAttribPointer(0, aPositionAttr, POSITION_COMPONENT_COUNT, 0)
        mTextureArray.setVertexAttribPointer(0, aTextureCoordinateAttr, POSITION_COMPONENT_COUNT, 0)

        glActiveTexture(GL_TEXTURE0)

        glBindTexture(GL_TEXTURE_2D, mTextureId)

        glDrawArrays(GL_TRIANGLES, 0, 3)

        GLES20.glDisableVertexAttribArray(aPositionAttr)
        GLES20.glDisableVertexAttribArray(aTextureCoordinateAttr)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        LogUtil.d("draw triangle")

    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        glDeleteProgram(mProgram)
    }
}