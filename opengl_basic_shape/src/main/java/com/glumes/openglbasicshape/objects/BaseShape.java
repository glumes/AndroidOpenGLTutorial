package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.data.VertexArray;

/**
 * Created by glumes on 2017/7/29.
 */

public abstract class BaseShape {


    protected Context mContext;

    protected VertexArray vertexArray;

    protected VertexArray indexArray;

    protected int mProgram;

    protected float[] modelMatrix = new float[16];
    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];
    protected float[] mvpMatrix = new float[16];


    protected int POSITION_COMPONENT_COUNT;

    protected int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    protected int STRIDE;

    public BaseShape(Context context) {
        mContext = context;

    }

    public void draw() {

    }

    public void draw(float[] mvpMatrix) {

    }

    public void bindData() {

    }

    public void destroy(){

    }

}
