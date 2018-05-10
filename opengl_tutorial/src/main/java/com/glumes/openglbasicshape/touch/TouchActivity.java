package com.glumes.openglbasicshape.touch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glumes.openglbasicshape.R;

public class TouchActivity extends AppCompatActivity {


    TouchView touchSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        touchSurfaceView = (TouchView) findViewById(R.id.touchSurface);

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
