package com.glumes.importobject;

import android.content.res.Resources;
import android.opengl.GLES30;

import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.ShaderHelper3;
import com.glumes.openglbasicshape.utils.ShaderUtil;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @Author glumes
 */
public class VAOTextureRect {

    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用

    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer mTextureBuffer;//顶点着色数据缓冲
    private ByteBuffer mIndicesBuffer; // 索引缓冲

    int vCount;//顶点数量

    float width;
    float height;

    int mVertexBufferId;

    int mTextureBufferId;

    int mIndicesBufferId;

    int mVAOId;

    public VAOTextureRect(Resources res,
                          float width, float height    //纹理矩形的宽高
    ) {

        this.width = width;
        this.height = height;

        initShader(res);
        initVertexData();

    }


    //初始化顶点数据
    public void initVertexData() {
        vCount = 6;//每个格子两个三角形，每个三角形3个顶点

        float vertices[] = {

                -width / 2, height / 2, 0,

                -width / 2, -height / 2, 0,

                width / 2, height / 2, 0,

                width / 2, -height / 2, 0,
        };

//        float vertices[] =
//                {
//                        -width / 2, height / 2, 0,
//                        -width / 2, -height / 2, 0,
//                        width / 2, height / 2, 0,
//
//                        -width / 2, -height / 2, 0,
//                        width / 2, -height / 2, 0,
//                        width / 2, height / 2, 0
//                };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置


        float textures[] =
                {
                        0f, 0f, 0f, 1, 1, 0f, 1, 1
                };

//
//        float textures[] =
//                {
//                        0f, 0f, 0f, 1f, 1f, 0f,
//                        0f, 1f, 1f, 1f, 1f, 0f,
//                };

        //创建顶点纹理数据缓冲
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTextureBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTextureBuffer.put(textures);//向缓冲区中放入顶点着色数据
        mTextureBuffer.position(0);//设置缓冲区起始位置

        byte[] indices = {
                1, 2, 3,
                0, 1, 2,
        };

        mIndicesBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndicesBuffer.order(ByteOrder.nativeOrder());
        mIndicesBuffer.put(indices);
        mIndicesBuffer.position(0);


        int[] bufferIds = new int[3];

        GLES30.glGenBuffers(3, bufferIds, 0);

        // 顶点缓冲
        mVertexBufferId = bufferIds[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);

        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, mVertexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);


        // 纹理缓冲
        mTextureBufferId = bufferIds[1];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);

        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, textures.length * 4, mTextureBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);


        // 索引缓冲
        mIndicesBufferId = bufferIds[2];

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndicesBufferId);

        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.length, mIndicesBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);

        initVAO();
    }

    public void initVAO() {

        int[] vaoIds = new int[1];

        GLES30.glGenVertexArrays(1, vaoIds, 0);

        mVAOId = vaoIds[0];

        GLES30.glBindVertexArray(mVAOId);


        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);
        GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);


//        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndicesBufferId);
//        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);


        GLES30.glBindVertexArray(0);

    }


    public void initShader(Resources res) {
        //加载顶点着色器的脚本内容
        mVertexShader = TextResourceReader.readTextFileFromAsset(res, "vertex_tex_3.glsl");
        //加载片元着色器的脚本内容
        mFragmentShader = TextResourceReader.readTextFileFromAsset(res, "frag_tex_3.glsl");
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }


    public void drawSelf(int texId) {
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        //将最终变换矩阵传入渲染管线
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);


        GLES30.glBindVertexArray(mVAOId);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndicesBufferId);


        //绑定纹理
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, vCount, GLES30.GL_UNSIGNED_BYTE, 0);

//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);


        GLES30.glBindVertexArray(0);

    }
}
