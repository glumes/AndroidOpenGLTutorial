package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.util.ArrayList;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by glumes on 2017/8/9.
 */

public class Sphere extends BaseShape {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";

    private int uMatrixLocation;
    private int aPositionLocation;

    float[] sphereVertex;

    private float step = 2.0f;
    private float step2 = 4.0f;
    private float radius = 1.0f;


    public Sphere(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.sphere_vertex_shader, R.raw.sphere_fragment_shader);

        glUseProgram(mProgram);

        POSITION_COMPONENT_COUNT = 3;

        sphereVertex = initSphereVertex();

        initSphereVertex();
    }

    private float[] initSphereVertex() {

        float r1;
        float r2;
        float y1;
        float y2;
        float cos;
        float sin;
        ArrayList<Float> data = new ArrayList<>();

        for (float i = -90.0f; i <= 90.0f; i += step) {

            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);

            y1 = (float) Math.sin(i * Math.PI / 180.0);
            y2 = (float) Math.sin((i + step) * Math.PI / 180.0);

            for (float j = 0.0f; j <= 360.0f; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(y2);
                data.add(r2 * sin);

                data.add(r1 * cos);
                data.add(y1);
                data.add(r1 * sin);
            }
        }

        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }


}
