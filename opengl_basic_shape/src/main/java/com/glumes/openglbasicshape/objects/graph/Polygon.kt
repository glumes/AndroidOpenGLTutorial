package com.glumes.openglbasicshape.objects.graph

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
class Polygon(context: Context) : BaseShape(context) {


    val U_COLOR = "u_Color"
    val A_POSITION = "a_Position"
    var aColorLocation: Int? = null
    var aPositionLocation: Int? = null

    val VERTEX_DATA_NUM = 5
    val radian = (2 * Math.PI / VERTEX_DATA_NUM).toFloat()
    val radius = 0.8f

    var vertex: FloatArray = FloatArray(VERTEX_DATA_NUM * 2 + 4)

    init {
        mProgram = ShaderHelper.buildProgram(mContext, R.raw.circle_vertex_shader, R.raw.circle_fragment_shader)
        glUseProgram(mProgram)
        initVertexData()

        vertexArray = VertexArray(vertex)

        POSITION_COMPONENT_COUNT = 2

    }

    private fun initVertexData() {
        vertex[0] = 0f
        vertex[1] = 0f
        for (i in 0..VERTEX_DATA_NUM) {
            vertex[2 * i + 2] = (radius * Math.cos((radian * i).toDouble())).toFloat()
            vertex[2 * i + 1 + 2] = (radius * Math.sin((radian * i).toDouble())).toFloat()
        }
        vertex[VERTEX_DATA_NUM * 2 + 2] = (radius * Math.cos(radian.toDouble())).toFloat()
        vertex[VERTEX_DATA_NUM * 2 + 3] = (radius * Math.sin((radian.toDouble()))).toFloat()

    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        aColorLocation = glGetUniformLocation(mProgram,U_COLOR)
        aPositionLocation = glGetAttribLocation(mProgram,A_POSITION)

        vertexArray.setVertexAttribPointer(0,aPositionLocation!!,POSITION_COMPONENT_COUNT,0)

    }

    override fun onDrawFrame(gl: GL10) {
        super.onDrawFrame(gl)
        glUniform4f(aColorLocation!!, 0.0f, 1.0f, 0.0f, 0.0f)
        glDrawArrays(GL_LINE_LOOP, 1, VERTEX_DATA_NUM)
    }
}