package com.glumes.importobject;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by glumes on 25/06/2018
 */

//加载后的物体――携带顶点信息，自动计算面法向量
public class LoadedObjectVertexNormalFace {
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maPositionHandle; //顶点位置属性引用
    int maNormalHandle; //顶点法向量属性引用
    int maLightLocationHandle;//光源位置属性引用
    int maCameraHandle; //摄像机位置属性引用

    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mNormalBuffer;//顶点法向量数据缓冲
    int vCount = 0;

    public LoadedObjectVertexNormalFace(Resources res, float[] vertices, float[] normals) {
        //初始化顶点数据
        initVertexData(vertices, normals);
        //初始化着色器
        initShader(res);
    }

    //初始化顶点数据的方法
    public void initVertexData(float[] vertices, float[] normals) {
        //顶点坐标数据的初始化================begin============================
        vCount = vertices.length / 3;

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点法向量数据的初始化================begin============================
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================
    }

    //初始化着色器
    public void initShader(Resources res) {
        //加载顶点着色器的脚本内容
        mVertexShader = TextResourceReader.readTextFileFromAsset(res, "vertex_light.glsl");
        //加载片元着色器的脚本内容
        mFragmentShader = TextResourceReader.readTextFileFromAsset(res, "frag_light.glsl");
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderHelper.buildProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中光源位置引用
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中摄像机位置引用
        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
    }

    public void drawSelf() {
        //指定使用某套着色器程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入渲染管线
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入渲染管线
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //将光源位置传入渲染管线
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //将摄像机位置传入渲染管线
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);

        // 将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maNormalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );
        //启用顶点位置、法向量数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        //绘制被加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
