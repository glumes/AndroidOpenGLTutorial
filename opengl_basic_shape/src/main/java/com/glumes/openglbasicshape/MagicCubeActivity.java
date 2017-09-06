package com.glumes.openglbasicshape;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glumes.openglbasicshape.magiccube.views.ViewAutoMode;

public class MagicCubeActivity extends AppCompatActivity {

    Context mContext;

    ViewAutoMode glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glView = (ViewAutoMode) findViewById(R.id.viewAutoMode);
    }
}
