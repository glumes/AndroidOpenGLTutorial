package com.glumes.opengl_tutorial_practice.shape

import android.content.res.Resources
import android.opengl.GLES30
import android.opengl.Matrix
import com.glumes.opengl_tutorial_practice.utils.MatrixState
import com.glumes.opengl_tutorial_practice.utils.ShaderUtil
import com.glumes.opengl_tutorial_practice.utils.ShaderUtil.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


/**
 * Created by glumes on 11/05/2018
 */
class SixPointedStar(resouerce: Resources, r: Float, R: Float, z: Float) {

    var mProgram: Int = 0//自定义渲染管线着色器程序id
    var muMVPMatrixHandle: Int = 0//总变换矩阵引用
    var maPositionHandle: Int = 0 //顶点位置属性引用
    var maColorHandle: Int = 0 //顶点颜色属性引用
    lateinit var mVertexShader: String    //顶点着色器代码脚本
    lateinit var mFragmentShader: String    //片元着色器代码脚本
    var mMMatrix = FloatArray(16)    //具体物体的3D变换矩阵，包括旋转、平移、缩放

    lateinit var mVertexBuffer: FloatBuffer//顶点坐标数据缓冲
    lateinit var mColorBuffer: FloatBuffer//顶点着色数据缓冲
    var vCount = 0
    var yAngle = 0f//绕y轴旋转的角度
    var xAngle = 0f//绕x轴旋转的角度
    val UNIT_SIZE = 1f

    init {
        //调用初始化顶点数据的initVertexData方法
        initVertexData(R, r, z)
        //调用初始化着色器的intShader方法
        initShader(resouerce)
    }

    //初始化顶点数据的initVertexData方法
    fun initVertexData(R: Float, r: Float, z: Float) {
        val flist = ArrayList<Float>()
        val tempAngle = (360 / 6).toFloat()
        var angle = 0f
        while (angle < 360)
        //循环生成构成六角形各三角形的顶点坐标
        {
            //第一个三角形
            //第一个点的x、y、z坐标
            flist.add(0f)
            flist.add(0f)
            flist.add(z)
            //第二个点的x、y、z坐标
            flist.add((R.toDouble() * UNIT_SIZE.toDouble() * Math.cos(Math.toRadians(angle.toDouble()))).toFloat())
            flist.add((R.toDouble() * UNIT_SIZE.toDouble() * Math.sin(Math.toRadians(angle.toDouble()))).toFloat())
            flist.add(z)
            //第三个点的x、y、z坐标
            flist.add((r.toDouble() * UNIT_SIZE.toDouble() * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            flist.add((r.toDouble() * UNIT_SIZE.toDouble() * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            flist.add(z)

            //第二个三角形
            //第一个中心点的x、y、z坐标
            flist.add(0f)
            flist.add(0f)
            flist.add(z)
            //第二个点的x、y、z坐标
            flist.add((r.toDouble() * UNIT_SIZE.toDouble() * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            flist.add((r.toDouble() * UNIT_SIZE.toDouble() * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            flist.add(z)
            //第三个点的x、y、z坐标
            flist.add((R.toDouble() * UNIT_SIZE.toDouble() * Math.cos(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            flist.add((R.toDouble() * UNIT_SIZE.toDouble() * Math.sin(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            flist.add(z)
            angle += tempAngle
        }
        vCount = flist.size / 3
        val vertexArray = FloatArray(flist.size)//顶点坐标数组
        for (i in 0 until vCount) {
            vertexArray[i * 3] = flist[i * 3]
            vertexArray[i * 3 + 1] = flist[i * 3 + 1]
            vertexArray[i * 3 + 2] = flist[i * 3 + 2]
        }
        val vbb = ByteBuffer.allocateDirect(vertexArray.size * 4)
        vbb.order(ByteOrder.nativeOrder())    //设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer()
        mVertexBuffer.put(vertexArray)//将顶点坐标数据放进缓冲
        mVertexBuffer.position(0)


        //顶点着色数据的初始化================begin============================
        val colorArray = FloatArray(vCount * 4)//顶点着色数据的初始化
        for (i in 0 until vCount) {
            if (i % 3 == 0) {//中心点为白色，RGBA 4个通道[1,1,1,0]
                colorArray[i * 4] = 1f
                colorArray[i * 4 + 1] = 1f
                colorArray[i * 4 + 2] = 1f
                colorArray[i * 4 + 3] = 0f
            } else {//边上的点为淡蓝色，RGBA 4个通道[0.45,0.75,0.75,0]
                colorArray[i * 4] = 0.45f
                colorArray[i * 4 + 1] = 0.75f
                colorArray[i * 4 + 2] = 0.75f
                colorArray[i * 4 + 3] = 0f
            }
        }
        val cbb = ByteBuffer.allocateDirect(colorArray.size * 4)
        cbb.order(ByteOrder.nativeOrder())    //设置字节顺序为本地操作系统顺序
        mColorBuffer = cbb.asFloatBuffer()
        mColorBuffer.put(colorArray)//将顶点颜色数据放进缓冲
        mColorBuffer.position(0)
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================

    }

    //初始化着色器的initShader方法
    fun initShader(resouerce: Resources) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_six_point.glsl", resouerce)
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_six_point.glsl", resouerce)
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader)
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition")
        //获取程序中顶点颜色属性引用id
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor")
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
    }

    //绘制图形的方法
    fun drawSelf() {
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram)
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0f, 0f, 1f, 0f)
        //设置沿Z轴正向位移1
        Matrix.translateM(mMMatrix, 0, 0f, 0f, 1f)
        //设置绕y轴旋转yAngle度
        Matrix.rotateM(mMMatrix, 0, yAngle, 0f, 1f, 0f)
        //设置绕x轴旋转xAngle度
        Matrix.rotateM(mMMatrix, 0, xAngle, 1f, 0f, 0f)
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0)
        //将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        )
        //将顶点颜色数据送入渲染管线
        GLES30.glVertexAttribPointer(
                maColorHandle,
                4,
                GLES30.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer
        )
        //启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle)
        //启用顶点颜色数据数组
        GLES30.glEnableVertexAttribArray(maColorHandle)
        //绘制六角星
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }
}