package com.glumes.openglbasicshape.imageprocessing.processor

import android.graphics.PointF
import android.opengl.GLES20
import com.glumes.gpuimage.utils.OpenGlUtils
import com.glumes.openglbasicshape.utils.ShaderHelper
import java.nio.FloatBuffer
import java.util.*

/**
 * Created by glumes on 06/06/2018
 */

/**
 * 如果所有参赛都有默认值，则会生成一个额外的无参数构造函数，它将使用默认值
 */
open class ImageProcess(vertexShader: String, textureShader: String) {


    companion object {
        val NO_FILTER_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                " \n" +
                "varying vec2 textureCoordinate;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = position;\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "}"
        val NO_FILTER_FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                "uniform sampler2D inputImageTexture;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "}"
    }

    private val mRunOnDraw: LinkedList<Runnable> = LinkedList()
    private var isInitialized: Boolean = false
    private var mProgram: Int = 0

    // GLSL 中对应的默认属性
    private var attribPosition: Int = 0
    private var uniformTexture: Int = 0
    private var attribTextureCoordinate: Int = 0
    private var mCenter: Int = 0

    private var mVertexShader: String = ""
    private var mFragmentShader: String = ""

    init {
        mVertexShader = vertexShader
        mFragmentShader = textureShader
    }

    constructor() : this(vertexShader = NO_FILTER_VERTEX_SHADER, textureShader = NO_FILTER_FRAGMENT_SHADER)

    fun init() {
        onInit()
        isInitialized = true
        onInitialized()
    }

    /**
     * override 该方法来绑定其他属性
     */
    open fun onInit() {
        mProgram = ShaderHelper.buildProgram(mVertexShader, mFragmentShader)
        attribPosition = GLES20.glGetAttribLocation(mProgram, "position")
        uniformTexture = GLES20.glGetUniformLocation(mProgram, "inputImageTexture")
        attribTextureCoordinate = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate")

    }

    /**
     * 给属性设置，添加到 mRunOnDraw 链表中，绘制时再具体执行
     */
    open fun onInitialized() {
    }

    open fun onDraw(textureId: Int, vertexBuffer: FloatBuffer, textureBuffer: FloatBuffer) {
        GLES20.glUseProgram(mProgram)

        runPendingOnDrawTasks()
        if (!isInitialized) {
            return
        }
        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(attribPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glEnableVertexAttribArray(attribPosition)
        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(attribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(attribTextureCoordinate)

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glUniform1i(uniformTexture, 0)
        }
        onDrawArraysPre()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribTextureCoordinate)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    }


    /**
     * 在执行绘制具体内容之前的操作
     */
    protected open fun onDrawArraysPre() {}

    /**
     * 在绘制时设置其他属性的值
     */
    private fun runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            mRunOnDraw.removeFirst().run()
        }
    }

    fun destroy() {
        isInitialized = false
        GLES20.glDeleteProgram(mProgram)
        onDestroy()
    }

    open fun onDestroy() {

    }

    /**
     * 往 mRunOnDraw 添加 Runnable，在绘制之前设置其他的属性内容
     */
    protected fun addRunOnDraw(runnable: Runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.addLast(runnable)
        }
    }

    fun getProgram(): Int {
        return mProgram
    }

    protected fun setInteger(location: Int, intValue: Int) {
        addRunOnDraw(Runnable { GLES20.glUniform1i(location, intValue) })
    }

    fun setFloat(location: Int, floatValue: Float) {
        addRunOnDraw(Runnable { GLES20.glUniform1f(location, floatValue) })
    }

    protected fun setFloatVec2(location: Int, arrayValue: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue)) })
    }

    protected fun setFloatVec3(location: Int, arrayValue: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue)) })
    }

    protected fun setFloatVec4(location: Int, arrayValue: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue)) })
    }

    protected fun setFloatArray(location: Int, arrayValue: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniform1fv(location, arrayValue.size, FloatBuffer.wrap(arrayValue)) })
    }

    protected fun setPoint(location: Int, point: PointF) {
        addRunOnDraw(Runnable {
            val vec2 = FloatArray(2)
            vec2[0] = point.x
            vec2[1] = point.y
            GLES20.glUniform2fv(location, 1, vec2, 0)
        })
    }

    protected fun setUniformMatrix3f(location: Int, matrix: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0) })
    }

    protected fun setUniformMatrix4f(location: Int, matrix: FloatArray) {
        addRunOnDraw(Runnable { GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0) })
    }
}