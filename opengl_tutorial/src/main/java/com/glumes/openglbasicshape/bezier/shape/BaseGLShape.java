package com.glumes.openglbasicshape.bezier.shape;

import android.opengl.GLES20;

import com.glumes.openglbasicshape.utils.GLHelper;


/**
 * Created by jindongping on 2017/7/7.
 * 定义最基本的GLShape
 * 调用createShape会完成openGL所有装载操作
 */

public abstract class BaseGLShape {
    protected int mProgramHandler;

    /**
     * 必须在OpenGL的子线程中调用,会出现无法加载shader问题。
     */
    public void createShape() {
        //1 创建program
        createProgram();
        //2 绑定shader到program
        loadShaderInProgram();
        //3 切换program
        useProgram();
        //4 创建对应handler的默认buffer
        createBuffer();
        //5 从GLSL中找到handler
        findHandlersInGLSL();
        //6 设置默认buffer到对应handler
        fillHandlersValue();
    }

    private void createProgram() {
        mProgramHandler = GLES20.glCreateProgram();
    }

    protected void useProgram() {
        GLES20.glUseProgram(mProgramHandler);
    }

    protected void loadShaderInProgram() {
        String vertexShaderCode = getVertexShaderCode();
        String fragmentShaderCode = getFragmentShaderCode();
        if (vertexShaderCode == null || fragmentShaderCode == null) {
            throw new RuntimeException("getVertexShaderCode and getFragmentShaderCode can't return null");
        }
        GLHelper.loadShaders(mProgramHandler, vertexShaderCode, fragmentShaderCode);
    }
    
    protected abstract String getVertexShaderCode();

    protected abstract String getFragmentShaderCode();

    protected abstract void findHandlersInGLSL();

    protected abstract void createBuffer();

    protected abstract void fillHandlersValue();

    /**
     * onDrawFrame 时调用
     */
    public void draw(float[] mvpMatrix) {
        useProgram();
        enableAttrArrays();
        drawGL(mvpMatrix);
        disableAttrArrays();
    }

    protected abstract void enableAttrArrays();
    
    protected abstract void disableAttrArrays();

    protected abstract void drawGL(float[] MVPMatrix);

}
