package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by glumes on 2017/8/9.
 */

public class Sphere extends BaseShape {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";

    private int uMatrixLocation;
    private int aPositionLocation;

    public Sphere(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.sphere_vertex_shader, R.raw.sphere_fragment_shader);

        glUseProgram(mProgram);


    }

}
