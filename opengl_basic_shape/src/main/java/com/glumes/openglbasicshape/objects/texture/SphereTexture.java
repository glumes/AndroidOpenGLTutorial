package com.glumes.openglbasicshape.objects.texture;

import android.content.Context;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.objects.BaseShape;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/8/27.
 */

public class SphereTexture extends BaseShape {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
//    private static final String U_COLOR = "u_Color";

    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";


    private int uMatrixLocation;
    private int aPositionLocation;
    //    private int uColorLocation;
    private int aTextureCoordinatesLocation;
    private int uTextureUnitLocation;

    float[] sphereVertex;

    private float step = 2.0f;
    private float step2 = 4.0f;
    private float radius = 1.0f;
    private int length;

    private IntBuffer intBuffer;

    private ShortBuffer indexBuffer;

    private int[] position;

    private short[] indices;

    private float[] textureIndex;

    VertexArray textureVertexArray;

    public SphereTexture(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.sphere_texture_vertex_shader,
                R.raw.sphere_texture_fragment_shader);

        glUseProgram(mProgram);

        POSITION_COMPONENT_COUNT = 3;

        initSphereVertex2();

//        sphereVertex = initSphereVertex();

        vertexArray = new VertexArray(sphereVertex);

        textureVertexArray = new VertexArray(textureIndex);


        length = sphereVertex.length / 3;

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2).order(ByteOrder.nativeOrder())
                .asShortBuffer().put(indices);

        indexBuffer.position(0);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
//        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);

        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        textureVertexArray.setVertexAttribPointer(0, aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, 0);


        setIdentityM(modelMatrix, 0);

        int texture = TextureHelper.loadTexture(mContext, R.drawable.sphere);

        // OpenGL 在使用纹理进行绘制时，不需要直接给着色器传递纹理。
        // 相反，我们使用纹理单元保存那个纹理，因为，一个 GPU 只能同时绘制数量有限的纹理
        // 它使用那些纹理单元表示当前正在被绘制的活动的纹理

        // 通过调用 glActiveTexture 把活动的纹理单元设置为纹理单元 0
        glActiveTexture(GL_TEXTURE0);
        // 然后通过调用 glBindTexture 把纹理绑定到这个单元
        glBindTexture(GL_TEXTURE_2D, texture);
        // 接着通过调用 glUniform1i 把被选定的纹理单元传递给片段着色器中的 u_TextureUnit
        glUniform1i(uTextureUnitLocation, 0);
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);

    }

    @Override
    public void onDrawFrame(GL10 gl, float[] mvpMatrix) {
        super.onDrawFrame(gl, mvpMatrix);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

//        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);

        // 通过索引来绘制圆
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_SHORT, indexBuffer);

    }


    /**
     *
     */
    private void initSphereVertex3() {

        int rings = 90;
        int sectors = 90;

        float PI = 3.1415926535f;
        float PI_2 = 1.57079632679f;


        float R = 1f / (float) (rings - 1);
        float S = 1f / (float) (sectors - 1);
        short r, s;
        float x, y, z;

        sphereVertex = new float[rings * sectors * 3];

        // 相当于 rings = 90
        // 相当于 sectors = 90
        int t = 0, v = 0, n = 0;
        for (r = 0; r < rings; r++) {
            for (s = 0; s < sectors; s++) {
                y = (float) Math.sin(-PI_2 + PI * r * R);
                x = (float) (Math.cos(2 * PI * s * S) * Math.sin(PI * r * R));
                z = (float) (Math.sin(2 * PI * s * S) * Math.sin(PI * r * R));

                sphereVertex[v++] = radius * x;
                sphereVertex[v++] = radius * y;
                sphereVertex[v++] = radius * z;
            }
        }


        int counter = 0;
        indices = new short[rings * sectors * 6];
        for (r = 0; r < rings - 1; r++) {
            for (s = 0; s < sectors - 1; s++) {
                indices[counter++] = (short) (r * sectors + s);       //(a)
                indices[counter++] = (short) (r * sectors + (s + 1));    //(b)
                indices[counter++] = (short) ((r + 1) * sectors + (s + 1));  // (c)
                indices[counter++] = (short) ((r + 1) * sectors + (s + 1));  // (c)
                indices[counter++] = (short) (r * sectors + (s + 1));    //(b)
                indices[counter++] = (short) ((r + 1) * sectors + s);     //(d)
            }
        }


    }

    /**
     * 采用 glDrawElement 方式绘制
     *
     * @return
     */
    private void initSphereVertex2() {

        int rings = 90;
        int sectors = 90;

        float r;
        float y;
        float sin;
        float cos;

        sphereVertex = new float[(rings + 1) * (sectors + 1) * 3];
        textureIndex = new float[(rings + 1) * (sectors + 1) * 2];

        /**
         * 只遍历一边顶点就好了
         */
        int count = 0;
        for (float i = -90.0f; i <= 90.0f; i += step) {

            r = (float) Math.cos(i * Math.PI / 180.0);
            y = (float) Math.sin(i * Math.PI / 180.0);

            for (float j = 0.0f; j <= 360.0f; j += step2) {

                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                sphereVertex[count++] = r * sin;
                sphereVertex[count++] = y;
                sphereVertex[count++] = r * cos;

            }
        }

        int textureCount = 0;
        float xPos;
        float yPos;
        for (int i = 0; i <= rings; i++) {

            xPos = i * 1.0f * (1.0f / rings);
            LogUtil.d("xPos is " + xPos);

            for (int j = 0; j <= sectors; j++) {

                yPos = j * 1.0f * (1.0f / sectors);
                LogUtil.d("yPos is " + yPos);

                textureIndex[textureCount++] = yPos;
                textureIndex[textureCount++] = xPos;
            }
        }

        LogUtil.d("count num is " + sphereVertex.length);

        // 对于不存在的顶点索引 也可以绘制 嘛？
        int counter = 0;
        indices = new short[(rings + 1) * (sectors + 1) * 6];
        for (int i = 0; i <= rings; i++) {
            for (int j = 0; j <= sectors; j++) {
                indices[counter++] = (short) (i * sectors + j);       //(a)
                indices[counter++] = (short) (i * sectors + (j + 1));    //(b)
                indices[counter++] = (short) ((i + 1) * sectors + j);  // (c)
                indices[counter++] = (short) ((i + 1) * sectors + j);  // (c)
                indices[counter++] = (short) (i * sectors + (j + 1));    //(b)
                indices[counter++] = (short) ((i + 1) * sectors + (j + 1));     //(d)
//                if (i == rings * 2 - 1) {
//                    LogUtil.d("count is " + ((i + 1) * sectors + (j + 1)));
//                }
            }
        }

    }


    /**
     * 利用 glDrawArrays 绘制球体时的顶点要求
     * 采用 glDrawArrays 的方式绘制
     * 计算方式:
     * x = radius * cos * sin
     * y = radius * sin
     * z = radius * con * cos
     *
     * @return
     */
    private float[] initSphereVertex() {

        float r1;
        float r2;
        float y1;
        float y2;
        float cos;
        float sin;
        ArrayList<Float> data = new ArrayList<>();

        int count = 0;
        int count2 = 0;
        for (float i = -90.0f; i <= 90.0f; i += step) {

            count++;
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);

            y1 = (float) Math.sin(i * Math.PI / 180.0);
            y2 = (float) Math.sin((i + step) * Math.PI / 180.0);

            for (float j = 0.0f; j <= 360.0f; j += step2) {

                count2++;
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * sin);
                data.add(y2);
                data.add(r2 * cos);

                data.add(r1 * sin);
                data.add(y1);
                data.add(r1 * cos);
            }
        }

        LogUtil.d("count is " + count);
        LogUtil.d("count2 is " + count2);

        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }

        return f;
    }

}
