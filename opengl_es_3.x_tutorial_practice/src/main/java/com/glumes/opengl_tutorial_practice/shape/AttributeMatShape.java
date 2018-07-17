package com.glumes.opengl_tutorial_practice.shape;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.glumes.opengl_tutorial_practice.utils.MatrixState;
import com.glumes.opengl_tutorial_practice.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.glUniform4f;
import static com.glumes.opengl_tutorial_practice.utils.Constant.UNIT_SIZE;

/**
 * @Author glumes
 */
public class AttributeMatShape {

    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵引用


    int mModelMatrixHandle;

    int maPositionHandle; //顶点位置属性引用
    int maColorHandle; //顶点颜色属性引用
    String mVertexShader;//顶点着色器代码脚本
    String mFragmentShader;//片元着色器代码脚本

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲

    FloatBuffer mVecBuffer1;
    FloatBuffer mVecBuffer2;
    FloatBuffer mVecBuffer3;
    FloatBuffer mVecBuffer4;

    FloatBuffer mColorBuffer;//顶点着色数据缓冲
    int vCount = 0;

    int mProMatrixLocation;
    int mMMatrixLocation;

    public float[] mat1 = new float[]{1.0f, 0.0f, 0.0f, 0.0f};
    float[] mat2 = new float[]{0.0f, 1.0f, 0.0f, 0.0f};
    float[] mat3 = new float[]{0.0f, 0.0f, 1.0f, 0.0f};
    float[] mat4 = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    public AttributeMatShape(Resources resources) {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader(resources);
    }


    float[] model = new float[16];

    //初始化顶点坐标与着色数据的方法
    public void initVertexData() {
        //顶点坐标数据的初始化================begin============================
        vCount = 6;
        float vertices[] = new float[]
                {
                        0, 0, 0,
                        UNIT_SIZE, UNIT_SIZE, 0,
                        -UNIT_SIZE, UNIT_SIZE, 0,
                        -UNIT_SIZE, -UNIT_SIZE, 0,
                        UNIT_SIZE, -UNIT_SIZE, 0,
                        UNIT_SIZE, UNIT_SIZE, 0
                };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置


        ByteBuffer mat1bb = ByteBuffer.allocateDirect(mat1.length * 4);
        mat1bb.order(ByteOrder.nativeOrder());
        mVecBuffer1 = mat1bb.asFloatBuffer();
        mVecBuffer1.put(mat1);
        mVecBuffer1.position(0);


        ByteBuffer mat2bb = ByteBuffer.allocateDirect(mat2.length * 4);
        mat2bb.order(ByteOrder.nativeOrder());
        mVecBuffer2 = mat2bb.asFloatBuffer();
        mVecBuffer2.put(mat2);
        mVecBuffer2.position(0);

        ByteBuffer mat3bb = ByteBuffer.allocateDirect(mat3.length * 4);
        mat3bb.order(ByteOrder.nativeOrder());
        mVecBuffer3 = mat3bb.asFloatBuffer();
        mVecBuffer3.put(mat3);
        mVecBuffer3.position(0);

        ByteBuffer mat4bb = ByteBuffer.allocateDirect(mat4.length * 4);
        mat4bb.order(ByteOrder.nativeOrder());
        mVecBuffer4 = mat4bb.asFloatBuffer();
        mVecBuffer4.put(mat4);
        mVecBuffer4.position(0);


        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点着色数据的初始化================begin============================
//        float colors[] = new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
//                {
//                        1, 1, 1, 0,
//                        0, 0, 1, 0,
//                        0, 0, 1, 0,
//                        0, 0, 1, 0,
//                        0, 0, 1, 0,
//                        0, 0, 1, 0,
//                };
//        //创建顶点着色数据缓冲
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
//        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
//        mColorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
//        mColorBuffer.put(colors);//向缓冲区中放入顶点着色数据
//        mColorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================

        int[] buffIds = new int[2];

        GLES30.glGenBuffers(2, buffIds, 0);

        mVertexBufferId = buffIds[0];

        mModelBufferId = buffIds[1];


        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);

        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, mVertexBuffer, GLES30.GL_STATIC_DRAW);


        float[] modelMatrix = new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };


        ByteBuffer modelbb = ByteBuffer.allocateDirect(modelMatrix.length * 4);
        modelbb.order(ByteOrder.nativeOrder());
        uModelVertexBuffer = modelbb.asFloatBuffer();
        uModelVertexBuffer.put(modelMatrix);
        uModelVertexBuffer.position(0);


        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mModelBufferId);

        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, modelMatrix.length * 4, uModelVertexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

    }

    private FloatBuffer uModelVertexBuffer;

    private int uMatrixLocation1;
    private int uMatrixLocation2;
    private int uMatrixLocation3;
    private int uMatrixLocation4;


    int mVertexBufferId;//顶点坐标数据缓冲

    int mModelBufferId;

    //初始化着色器
    public void initShader(Resources resources) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("attribute_mat_vertex_v2.glsl", resources);
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("attribute_mat_frag_v2.glsl", resources);
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "a_Position");

        //获取程序中顶点颜色属性引用id
        maColorHandle = GLES30.glGetUniformLocation(mProgram, "u_Color");

        //获取程序中总变换矩阵引用id
//        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取位置、旋转变换矩阵引用id
//        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");

//        mProMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_ProMatrix");

//        mMMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix");


        mModelMatrixHandle = GLES30.glGetUniformLocation(mProgram, "modelMatrix");

        mMMatrixLocation = GLES30.glGetAttribLocation(mProgram, "u_Matrix");
//

        uMatrixLocation1 = mMMatrixLocation;
        uMatrixLocation2 = mMMatrixLocation + 1;
        uMatrixLocation3 = mMMatrixLocation + 2;
        uMatrixLocation4 = mMMatrixLocation + 3;
//
//        GLES30.glVertexAttribPointer(uMatrixLocation1, 4, GLES30.GL_FLOAT, false, 0, mVecBuffer1);

//        Matrix.setIdentityM(model, 0);
    }

    public void drawSelf() {
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        //将最终变换矩阵传入渲染管线

//
//        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
//        //将平移、旋转变换矩阵传入渲染管线
//        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);


//        GLES30.glUniformMatrix4fv(mProMatrixLocation, 1, false, MatrixState.getProMatrix(), 0);

//        GLES30.glUniformMatrix4fv(mMMatrixLocation, 1, false, MatrixState.getMMatrix(), 0);


        Matrix.setRotateM(model, 0, 0f, 0f, 1f, 0f);
        Matrix.translateM(model, 0, 0f, 0f, 1f);

        GLES30.glUniformMatrix4fv(mModelMatrixHandle, 1, false, model, 0);


        glUniform4f(maColorHandle, 0.0f, 1.0f, 1.0f, 1.0f);


        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);


        GLES30.glEnableVertexAttribArray(uMatrixLocation1);
        GLES30.glEnableVertexAttribArray(uMatrixLocation2);
        GLES30.glEnableVertexAttribArray(uMatrixLocation3);
        GLES30.glEnableVertexAttribArray(uMatrixLocation4);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mModelBufferId);

        GLES30.glVertexAttribPointer(uMatrixLocation1, 4, GLES30.GL_FLOAT, false, 4 * 4, 0);
        GLES30.glVertexAttribPointer(uMatrixLocation2, 4, GLES30.GL_FLOAT, false, 4 * 4, 4);
        GLES30.glVertexAttribPointer(uMatrixLocation3, 4, GLES30.GL_FLOAT, false, 4 * 4, 8);
        GLES30.glVertexAttribPointer(uMatrixLocation4, 4, GLES30.GL_FLOAT, false, 4 * 4, 12);

        GLES30.glVertexAttribDivisor(uMatrixLocation1,1);
        GLES30.glVertexAttribDivisor(uMatrixLocation2,2);
        GLES30.glVertexAttribDivisor(uMatrixLocation3,3);
        GLES30.glVertexAttribDivisor(uMatrixLocation4,4);

//
//        //将顶点位置数据传入渲染管线
//        GLES30.glVertexAttribPointer
//                (
//                        maPositionHandle,
//                        3,
//                        GLES30.GL_FLOAT,
//                        false,
//                        3 * 4,
//                        mVertexBuffer
//                );


//        //将顶点颜色数据传入渲染管线
//        GLES30.glVertexAttribPointer
//                (
//                        maColorHandle,
//                        4,
//                        GLES30.GL_FLOAT,
//                        false,
//                        4 * 4,
//                        mColorBuffer
//                );
        //启用顶点位置数据数组
//        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点颜色数据数组
//        GLES30.glEnableVertexAttribArray(maColorHandle);
        //绘制颜色矩形

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vCount);
    }
}
