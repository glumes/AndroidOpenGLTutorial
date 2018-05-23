package com.glumes.openglbasicshape.touch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glumes.openglbasicshape.R;


public class TouchActivity extends AppCompatActivity {


    PhotoEditor touchSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        touchSurfaceView = findViewById(R.id.photoEditor);
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
        touchSurfaceView.addView();


        touchSurfaceView.getPhoto();
    }
}
