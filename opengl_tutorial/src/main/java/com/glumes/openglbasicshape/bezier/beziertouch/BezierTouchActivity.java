package com.glumes.openglbasicshape.bezier.beziertouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.glumes.openglbasicshape.R;

public class BezierTouchActivity extends AppCompatActivity {

    private BezierDrawView mBezierDrawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_touch);

        mBezierDrawView = findViewById(R.id.bezierDraw2);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBezierDrawView.render();
            }
        });
    }
}
