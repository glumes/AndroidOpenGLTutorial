package com.glumes.openglbasicshape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glumes.openglbasicshape.glviews.TouchSurfaceView;

public class TouchActivity extends AppCompatActivity {


    TouchSurfaceView touchSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        touchSurfaceView = (TouchSurfaceView) findViewById(R.id.touchSurface);

    }

    @Override
    protected void onPause() {
        super.onPause();
        touchSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        touchSurfaceView.onResume();
    }
}
