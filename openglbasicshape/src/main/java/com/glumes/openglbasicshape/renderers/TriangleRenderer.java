package com.glumes.openglbasicshape.renderers;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by glumes on 2017/7/22.
 */

public class TriangleRenderer extends BaseRenderer {


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int aColorLocation;
    private int aPositionLocation;

    private FloatBuffer vertexData;
    float[] triangleVertex = {
            -1f, 1f,
            -1f, -1f,
            1f, -1f
    };

    public static final int POSITION_COMPONENT_COUNT = 2;

    public TriangleRenderer(Context mContext) {
        super(mContext);

        vertexData = ByteBuffer.allocateDirect(triangleVertex.length * BYTES_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(triangleVertex);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);


        aColorLocation = glGetUniformLocation(program, U_COLOR);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT);

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    @Override
    public void readShaderSource() {
        vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.triangle_vertex_shader);
        fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.triangle_fragment_shader);
    }
}
