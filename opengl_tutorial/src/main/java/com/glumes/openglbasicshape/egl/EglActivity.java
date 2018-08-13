package com.glumes.openglbasicshape.egl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.glumes.openglbasicshape.R;

public class EglActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private EglDemo mEglDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egl);
        mSurfaceView = findViewById(R.id.eglview);

        mEglDemo = new EglDemo();
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mEglDemo.initEgl();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mEglDemo.render(mSurfaceView.getHolder().getSurface(), width, height, EglActivity.this);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
