package com.gluems.openglkotlinnative

import android.opengl.GLES30
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @Author  glumes
 */
class Triangle(view: KotlinGLView) {


    var mProgram: Int? = 0

    var muMVPMatrixHandle: Int? = 0
    var maPositionHandle: Int? = 0
    var maColorHandle: Int? = 0
    var mVertexShader: String? = ""
    var mFragmentShader: String? = ""

    var mVertexBuffer: FloatBuffer? = null
    var mColorBuffer: FloatBuffer? = null
    var vCount: Int? = 0
    var xAngle: Float? = 0f

    val UNIT_SIZE: Float = 0.2f

    init {
        initVertexData()
        initShader(view)
    }


    fun drawSelf() {
        GLES30.glUseProgram(mProgram!!)
        Matrix.setRotateM(mMMatrix, 0, 0f, 0f, 1f, 0f)
        Matrix.translateM(mMMatrix, 0, 0f, 0f, 1f)
        Matrix.rotateM(mMMatrix, 0, xAngle!!, 1f, 0f, 0f)

        GLES30.glUniformMatrix4fv(muMVPMatrixHandle!!, 1, false, Triangle.getFinalMatrix(mMMatrix), 0)

        GLES30.glVertexAttribPointer(
                maPositionHandle!!,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        )

        GLES30.glVertexAttribPointer(
                maColorHandle!!,
                4,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer
        )

        GLES30.glEnableVertexAttribArray(maPositionHandle!!)
        GLES30.glEnableVertexAttribArray(maColorHandle!!)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount!!)

    }

    private fun initShader(view: KotlinGLView) {
        mVertexShader = ShaderUtil.readTextFileFromResource(view.context, R.raw.vertex)
        mFragmentShader = ShaderUtil.readTextFileFromResource(view.context, R.raw.fragment)
        mProgram = ShaderUtil.createProgram(mVertexShader!!, mFragmentShader!!)

        maPositionHandle = GLES30.glGetAttribLocation(mProgram!!, "aPosition")
        maColorHandle = GLES30.glGetAttribLocation(mProgram!!, "aColor")
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram!!, "uMVPMatrix")

    }


    private fun initVertexData() {
        vCount = 3
        val vertices: FloatArray = floatArrayOf(
                -4 * UNIT_SIZE, 0f, 0f,
                0f, -4 * UNIT_SIZE, 0f,
                4 * UNIT_SIZE, 0f, 0f
        )

        val vbb: ByteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        mVertexBuffer = vbb.asFloatBuffer()
        mVertexBuffer!!.put(vertices)
        mVertexBuffer!!.position(0)


        val colors: FloatArray = floatArrayOf(
                1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f
        )

        val cbb: ByteBuffer = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mColorBuffer = cbb.asFloatBuffer()
        mColorBuffer!!.put(colors)
        mColorBuffer!!.position(0)

    }


    companion object {

        var mProjMatrix: FloatArray = kotlin.FloatArray(16)
        var mVMatrix: FloatArray = kotlin.FloatArray(16)
        var mMVPMatrix: FloatArray = kotlin.FloatArray(16)
        var mMMatrix: FloatArray = kotlin.FloatArray(16)

        fun getFinalMatrix(spec: FloatArray): FloatArray {
            Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0)
            Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0)
            return mMVPMatrix
        }
    }
}