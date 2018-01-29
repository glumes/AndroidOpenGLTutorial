package com.glumes.openglbasicshape.objects.texture

import android.content.Context
import android.opengl.GLES20.*
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.data.VertexArray
import com.glumes.openglbasicshape.objects.BaseShape
import com.glumes.openglbasicshape.utils.ShaderHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author glumes
 */
class TextureTriangle(context: Context) : BaseShape(context) {


    private val U_VIEW_MATRIX = "u_ViewMatrix"
    private val U_MODEL_MATRIX = "u_ModelMatrix"
    private val U_PROJECTION_MATRIX = "u_ProjectionMatrix"
    private val A_POSITION = "a_Position"
    private val A_TEXTURE_COORDINATE = "a_TextureCoordinates"


    private var uModelMatrixAttr: Int = 0
    private var uViewMatrixAttr: Int = 0
    private var uProjectionMatrixAttr: Int = 0
    private var aPositionAttr: Int = 0
    private var aTextureCoordinateAttr: Int = 0


    var vertexArrayData = floatArrayOf(
            0.0f, 1.0f
            - 1.0f, 0.0f,
            1.0f, 0.0f
    )

    var textureArrayData = floatArrayOf(

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

        aPositionAttr = glGetAttribLocation(mProgram, A_POSITION)
        uModelMatrixAttr = glGetUniformLocation(mProgram, U_MODEL_MATRIX)
        uViewMatrixAttr = glGetUniformLocation(mProgram, U_VIEW_MATRIX)
        uProjectionMatrixAttr = glGetUniformLocation(mProgram, U_PROJECTION_MATRIX)


    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
    }
}