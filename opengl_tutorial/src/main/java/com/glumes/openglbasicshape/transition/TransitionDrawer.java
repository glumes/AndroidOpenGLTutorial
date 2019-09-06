package com.glumes.openglbasicshape.transition;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

public class TransitionDrawer {


    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maCameraHandle; //摄像机位置属性引用
    int maPositionHandle; //顶点位置属性引用
    int maNormalHandle; //顶点法向量属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int maSunLightLocationHandle;//光源位置属性引用

    int muDirectionHandle;
    int muProgressHandle;

    int mTextureSample1;
    int mTextureSample2;

    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer mTextureBuffer;//顶点着色数据缓冲
    int vCount;//顶点数量
    int texId;//纹理Id

    float width = 2.0f;
    float height = 2.0f;


    float mProgress;
    float[] mDirection = new float[2];

//    public TransitionDrawer(Resources res,
//                       float width, float height    //纹理矩形的宽高
//    ) {
//
//        this.width = width;
//        this.height = height;
//
//        initVertexData();
//        initShader(res);
//
//    }

    public TransitionDrawer(Resources res) {
        initVertexData();
        initShader(res);
    }

    //初始化顶点数据
    public void initVertexData() {
        //顶点坐标数据的初始化================begin============================
        vCount = 6;//每个格子两个三角形，每个三角形3个顶点
        float vertices[] =
                {
                        -width / 2, height / 2, 0,
                        -width / 2, -height / 2, 0,
                        width / 2, height / 2, 0,

                        -width / 2, -height / 2, 0,
                        width / 2, -height / 2, 0,
                        width / 2, height / 2, 0
                };
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        float textures[] =
                {
                        0f, 0f, 0f, 1, 1, 0f,
                        0f, 1, 1, 1, 1, 0f
                };
        //创建顶点纹理数据缓冲
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTextureBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTextureBuffer.put(textures);//向缓冲区中放入顶点着色数据
        mTextureBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理数据的初始化================end============================
    }

    public void initShader(Resources res) {
        //加载顶点着色器的脚本内容
        mVertexShader = TextResourceReader.readTextFileFromAsset(res, "transition_vertex.glsl");
        //加载片元着色器的脚本内容
        mFragmentShader = TextResourceReader.readTextFileFromAsset(res, "transition_fragment.glsl");
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderHelper.buildProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        muDirectionHandle = GLES20.glGetUniformLocation(mProgram, "direction");
        muProgressHandle = GLES20.glGetUniformLocation(mProgram, "progress");

        mTextureSample1 = GLES20.glGetUniformLocation(mProgram,"sTexture1");

        mTextureSample2 = GLES20.glGetUniformLocation(mProgram,"sTexture2");

    }


    public void setProgress(float progress) {
        mProgress = progress;
    }

    public void setDirection(float x, float y) {
        mDirection[0] = x;
        mDirection[1] = y;
    }

    public void drawSelf(int texId, int texId2,float[] mvpmatrix) {
        //指定使用某套着色器程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入渲染管线
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpmatrix, 0);
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
        //将纹理数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTextureBuffer
                );
        //启用顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点纹理坐标数据数组
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        GLES20.glUniform1f(muProgressHandle, mProgress);

        GLES20.glUniform2fv(muDirectionHandle,1,mDirection,0);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glUniform1i(mTextureSample1, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId2);
        GLES20.glUniform1i(mTextureSample2, 1);

//        GLES20.
        //绘制纹理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

}
