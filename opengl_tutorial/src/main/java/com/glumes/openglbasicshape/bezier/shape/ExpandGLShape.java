package com.glumes.openglbasicshape.bezier.shape;



import java.nio.FloatBuffer;
import java.util.Arrays;


public abstract class ExpandGLShape extends SimpleGLShape{
    
    protected int mDataSize;
    
    protected FloatBuffer mNewVertexBuffer;
    private int mStartPosition;

    public ExpandGLShape() {
        super();
    }

    public ExpandGLShape(int defaultSize) {
        super(defaultSize);
    }

    public void addVertexData(float... fs) {
        int oldCapacity = mData.length;
        if (mDataSize > oldCapacity - fs.length) {
            mData = Arrays.copyOf(mData, oldCapacity + (oldCapacity >> 1));
            createBuffer();
        }
        for (float f : fs) {
            mData[mDataSize] = f;
            mVertexBuffer.put(mDataSize, f);
            mDataSize++;
        }
        mVertexBuffer.position(0);
        
//        int size = fs.length * Constant.FLOAT_SIZE;
//        mNewVertexBuffer = GLHelper.initFloatBuffer(fs);
//        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, mStartPosition, size, mNewVertexBuffer);
//        mStartPosition += size;
//        mDataSize += fs.length;
    }

    public void resetVertexData() {
        mData = new float[3000];
        createBuffer();
        mDataSize = 0;
    }
}
