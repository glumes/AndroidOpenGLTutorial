package com.glumes.importobject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Paint;
import android.opengl.GLES30;
import android.os.Build;

import com.glumes.importobject.utils.BallTypeHelper;
import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.ShaderUtil;

//球
@SuppressLint("NewApi")
public class Ball {
    int mProgram;// 自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int muRHandle;//球的半径属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
    int vCount = 0;
    public float yAngle = 0;// 绕y轴旋转的角度
    public float xAngle = 0;// 绕x轴旋转的角度
    public float zAngle = 0;// 绕z轴旋转的角度
    float r = 0.8f;

    public final float UNIT_SIZE = 1f;

    public int mType = 0;

    public Ball(Resources resources, int type) {

        mType = type;

        // 初始化顶点数据的方法
        initVertexData();
        // 初始化着色器的方法
        initShader(resources);
    }

    // 初始化顶点数据的方法
    public void initVertexData() {
        // 顶点坐标数据的初始化================begin============================
        ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
        final int angleSpan = 10;// 将球进行单位切分的角度
        for (int vAngle = -90; vAngle < 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
        {
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan)// 水平方向angleSpan度一份
            {// 纵向横向各到一个角度后计算对应的此点在球面上的坐标
                float x0 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle)));
                float y0 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle)));
                float z0 = (float) (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle)));

                float x1 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle + angleSpan)));
                float y1 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle + angleSpan)));
                float z1 = (float) (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle)));

                float x2 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
                        .cos(Math.toRadians(hAngle + angleSpan)));
                float y2 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
                        .sin(Math.toRadians(hAngle + angleSpan)));
                float z2 = (float) (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle + angleSpan)));

                float x3 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
                        .cos(Math.toRadians(hAngle)));
                float y3 = (float) (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
                        .sin(Math.toRadians(hAngle)));
                float z3 = (float) (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle + angleSpan)));

                // 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
            }
        }
        vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标
        // 将alVertix中的坐标值转存到一个float数组中
        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }

        // 创建顶点坐标数据缓冲
        // vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();// 转换为float型缓冲
        mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);// 设置缓冲区起始位置
        // 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        // 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    }

    // 初始化着色器
    public void initShader(Resources res) {

        // 加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile(BallTypeHelper.mVertexMap.get(mType), res);
        // 加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile(BallTypeHelper.mFragMap.get(mType), res);
        // 基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        // 获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        // 获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        // 获取程序中球半径引用
        muRHandle = GLES30.glGetUniformLocation(mProgram, "uR");
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SuppressLint("NewApi")
    public void drawSelf() {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 1, 0);//绕Y轴转动
        MatrixState.rotate(zAngle, 0, 0, 1);//绕Z轴转动
        // 指定使用某套shader程序
        GLES30.glUseProgram(mProgram);
        // 将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
                MatrixState.getFinalMatrix(), 0);
        // 将半径尺寸传入渲染管线
        GLES30.glUniform1f(muRHandle, r * UNIT_SIZE);
        //将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        //启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //绘制球
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
