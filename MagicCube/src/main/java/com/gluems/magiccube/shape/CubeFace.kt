package com.gluems.magiccube.shape

import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLUtils
import com.gluems.magiccube.CubeApplication
import com.gluems.magiccube.R
import com.gluems.magiccube.util.BufferFactory
import com.gluems.magiccube.util.MatrixState
import com.gluems.magiccube.util.ShaderUtil
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * @Author  glumes
 */
class CubeFace(v1: CubeVertex, v2: CubeVertex, v3: CubeVertex, v4: CubeVertex) {

    lateinit var indicesBuffer: FloatBuffer
    lateinit var textureBuffer: FloatBuffer
    var vertextList: ArrayList<CubeVertex> = ArrayList()

    lateinit var mBitmap: Bitmap

    var isInitTexture = false

    var mTextureId = -1

    var mProgram: Int? = 0
    var uMVPMatrixHandle: Int? = 0
    var aPositionHandle: Int? = 0
    var aTexCoorHandle: Int? = 0

    var mVertexShader: String? = ""
    var mFragmentShader: String? = ""


    init {
        vertextList.add(v1)
        vertextList.add(v2)
        vertextList.add(v3)
        vertextList.add(v4)

        initShader()

    }

    fun loadBitmap(bitmap: Bitmap) {
        this.mBitmap = bitmap
        isInitTexture = true
    }


    fun initShader() {
        mVertexShader = ShaderUtil.readTextFileFromResource(R.raw.cube_vertex_shader)
        mFragmentShader = ShaderUtil.readTextFileFromResource(R.raw.cube_fragment_shader)
        mProgram = ShaderUtil.createProgram(mVertexShader!!, mFragmentShader!!)
        uMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram!!, "uMVPMatrix")
        aPositionHandle = GLES30.glGetAttribLocation(mProgram!!, "aPosition")
        aTexCoorHandle = GLES30.glGetAttribLocation(mProgram!!, "aTexColor")
    }


    fun loadTexture() {

        var textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        mTextureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId)

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_SWIZZLE_R, GLES30.GL_GREEN.toFloat())

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, mBitmap, 0)

    }

    fun draw() {
        if (isInitTexture) {
            loadTexture()
            isInitTexture = false
        }

        GLES30.glUseProgram(mProgram!!)
        MatrixState.setInitStack()
        GLES30.glUniformMatrix4fv(uMVPMatrixHandle!!, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPositionHandle!!, 3, GLES30.GL_FLOAT, false, 3 * 4, indicesBuffer)
        GLES30.glVertexAttribPointer(aTexCoorHandle!!, 2, GLES30.GL_FLOAT, false, 2 * 4, textureBuffer)
        GLES30.glEnableVertexAttribArray(aPositionHandle!!)
        GLES30.glEnableVertexAttribArray(aTexCoorHandle!!)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6)


    }

    fun setIndices(floatArray: FloatArray) {
        indicesBuffer = BufferFactory.newFloatBuffer(floatArray)
    }

    fun setTextureCoordinates(floatArray: FloatArray) {
        textureBuffer = BufferFactory.newFloatBuffer(floatArray)
    }
}