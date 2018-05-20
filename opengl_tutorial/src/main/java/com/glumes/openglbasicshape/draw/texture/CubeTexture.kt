package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.os.SystemClock
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.draw.BaseShape
import com.glumes.openglbasicshape.utils.MatrixState
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

    private var mTextureId: IntArray? = null


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

    // 六个面的顶点，都是一样的坐标，通过变换矩阵来转换位置进行绘制。
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

    // 六个面的纹理坐标，都是一样的坐标，通过变换矩阵来转换位置进行绘制。
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

        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        //打开背面剪裁，面剔除，优化显示速度
        GLES30.glEnable(GLES30.GL_CULL_FACE)

        aPositionAttr = GLES20.glGetAttribLocation(mProgram, A_POSITION)
        uModelMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_MODEL_MATRIX)
        uViewMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_VIEW_MATRIX)
        uProjectionMatrixAttr = GLES20.glGetUniformLocation(mProgram, U_PROJECTION_MATRIX)

        aTextureCoordinateAttr = GLES20.glGetAttribLocation(mProgram, A_TEXTURE_COORDINATE)
        uTextureUnitAttr = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT)

        mTextureId = TextureHelper.loadCubeTexture(mContext)

        GLES20.glUniform1i(uTextureUnitAttr, 0)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 6.0f

        Observable.interval(30, TimeUnit.MILLISECONDS)
                .subscribe {
                    eyeX = eyeDistance * Math.sin((radian * num).toDouble()).toFloat()
                    eyeZ = eyeDistance * Math.cos((radian * num).toDouble()).toFloat()
                    num++
                    if (num > 360) {
                        num = 0
                    }
                }


        MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        MatrixState.setProjectFrustum(left, ratio, bottom, top, near, far)

        MatrixState.setInitStack()

        MatrixState.rotate(-30f, 0f, 0f, 1f)
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

        // 控制调整相机来观察不同的面
        MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)


        GLES20.glUniformMatrix4fv(uProjectionMatrixAttr, 1, false, MatrixState.getProMatrix(), 0)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, MatrixState.getVMatrix(), 0)


        MatrixState.pushMatrix()

//        val time = SystemClock.uptimeMillis() % 10000L
//        val angleInDegrees = 360.0f / 10000.0f * time.toInt()

        // 通过改变旋转矩阵来观察不同的面
//        MatrixState.rotate(angleInDegrees, 0f, 1.0f, 0f)


        // 开始绘制立方体的每个面
        // 前面
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, HalfCubeSize)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![0])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()

        // 后面
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -HalfCubeSize)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![1])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()

        // 上面
        MatrixState.pushMatrix()
        MatrixState.translate(0f, HalfCubeSize, 0f)
        MatrixState.rotate(-90f, 1f, 0f, 0f)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![2])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()


        //下面
        MatrixState.pushMatrix()
        MatrixState.translate(0f, -HalfCubeSize, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![3])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()


        // 左面
        MatrixState.pushMatrix()
        MatrixState.translate(HalfCubeSize, 0f, 0f)
        MatrixState.rotate(-90f, 1f, 0f, 0f)
        MatrixState.rotate(90f, 0f, 1f, 0f)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![4])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()

        // 右面
        MatrixState.pushMatrix()
        MatrixState.translate(-HalfCubeSize, 0f, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        MatrixState.rotate(-90f, 0f, 1f, 0f)
        GLES20.glUniformMatrix4fv(uModelMatrixAttr, 1, false, MatrixState.getMMatrix(), 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId!![5])
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        MatrixState.popMatrix()


        MatrixState.popMatrix()

        GLES20.glDisableVertexAttribArray(aPositionAttr)
        GLES20.glDisableVertexAttribArray(aTextureCoordinateAttr)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        GLES20.glDeleteProgram(mProgram)
    }
}