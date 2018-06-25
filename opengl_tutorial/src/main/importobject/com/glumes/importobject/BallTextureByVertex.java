package com.glumes.importobject;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by glumes on 25/06/2018
 */

//用于绘制的篮球
public class BallTextureByVertex {
    int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本
    static float[] mMMatrix = new float[16];//具体物体的移动旋转矩阵

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount = 0;   //顶点数量

    final float UNIT_SIZE = 0.8f;//球单位尺寸
    public static final float ANGLE_SPAN = 11.25f;//将球进行单位切分的角度

    public BallTextureByVertex(Resources res, float scale) {
        //初始化顶点坐标与着色数据
        initVertexData(scale);
        //初始化shader
        intShader(res);
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float scale) {
        //获取切分整图的纹理数组
        float[] texCoorArray =
                generateTexCoor
                        (
                                (int) (360 / ANGLE_SPAN), //纹理图切分的列数
                                (int) (180 / ANGLE_SPAN)  //纹理图切分的行数
                        );
        int tc = 0;//纹理数组计数器
        int ts = texCoorArray.length;//纹理数组长度

        ArrayList<Float> alVertix = new ArrayList<Float>();//存放顶点坐标的ArrayList
        ArrayList<Float> alTexture = new ArrayList<Float>();//存放纹理坐标的ArrayList

        for (float vAngle = 90; vAngle > -90; vAngle = vAngle - ANGLE_SPAN)//垂直方向angleSpan度一份
        {
            for (float hAngle = 360; hAngle > 0; hAngle = hAngle - ANGLE_SPAN)//水平方向angleSpan度一份
            {
                //纵向横向各到一个角度后计算对应的此点在球面上的四边形顶点坐标
                //并构建两个组成四边形的三角形

                double xozLength = scale * UNIT_SIZE * Math.cos(Math.toRadians(vAngle));
                float x1 = (float) (xozLength * Math.cos(Math.toRadians(hAngle)));
                float z1 = (float) (xozLength * Math.sin(Math.toRadians(hAngle)));
                float y1 = (float) (scale * UNIT_SIZE * Math.sin(Math.toRadians(vAngle)));

                xozLength = scale * UNIT_SIZE * Math.cos(Math.toRadians(vAngle - ANGLE_SPAN));
                float x2 = (float) (xozLength * Math.cos(Math.toRadians(hAngle)));
                float z2 = (float) (xozLength * Math.sin(Math.toRadians(hAngle)));
                float y2 = (float) (scale * UNIT_SIZE * Math.sin(Math.toRadians(vAngle - ANGLE_SPAN)));

                xozLength = scale * UNIT_SIZE * Math.cos(Math.toRadians(vAngle - ANGLE_SPAN));
                float x3 = (float) (xozLength * Math.cos(Math.toRadians(hAngle - ANGLE_SPAN)));
                float z3 = (float) (xozLength * Math.sin(Math.toRadians(hAngle - ANGLE_SPAN)));
                float y3 = (float) (scale * UNIT_SIZE * Math.sin(Math.toRadians(vAngle - ANGLE_SPAN)));

                xozLength = scale * UNIT_SIZE * Math.cos(Math.toRadians(vAngle));
                float x4 = (float) (xozLength * Math.cos(Math.toRadians(hAngle - ANGLE_SPAN)));
                float z4 = (float) (xozLength * Math.sin(Math.toRadians(hAngle - ANGLE_SPAN)));
                float y4 = (float) (scale * UNIT_SIZE * Math.sin(Math.toRadians(vAngle)));

                //构建第一三角形
                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x4);
                alVertix.add(y4);
                alVertix.add(z4);
                //构建第二三角形
                alVertix.add(x4);
                alVertix.add(y4);
                alVertix.add(z4);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

                //第一三角形3个顶点的6个纹理坐标
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                //第二三角形3个顶点的6个纹理坐标
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
                alTexture.add(texCoorArray[tc++ % ts]);
            }
        }

        vCount = alVertix.size() / 3;//顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

        //将alVertix中的坐标值转存到一个int数组中
        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }

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

        //顶点纹理坐标数据的初始化================begin============================
        float texCoor[] = new float[alTexture.size()];//顶点纹理值数组
        for (int i = 0; i < alTexture.size(); i++) {
            texCoor[i] = alTexture.get(i);
        }
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================

    }

    //初始化shader
    public void intShader(Resources res) {
        //加载顶点着色器的脚本内容
        mVertexShader = TextResourceReader.readTextFileFromAsset(res, "vertex_tex.glsl");
        //加载片元着色器的脚本内容
        mFragmentShader = TextResourceReader.readTextFileFromAsset(res, "frag_tex.glsl");
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderHelper.buildProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(int texId) {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //将纹理坐标数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        //启用顶点位置、纹理坐标数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        //绘制纹理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw, int bh) {
        float[] result = new float[bw * bh * 6 * 2];
        float sizew = 1.0f / bw;//列数
        float sizeh = 1.0f / bh;//行数
        int c = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {
                //每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
                float s = j * sizew;
                float t = i * sizeh;

                result[c++] = s;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t;


                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t + sizeh;
            }
        }
        return result;
    }
}
