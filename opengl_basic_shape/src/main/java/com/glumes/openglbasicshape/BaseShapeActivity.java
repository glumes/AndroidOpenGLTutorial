package com.glumes.openglbasicshape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.glumes.openglbasicshape.glviews.BaseShapeView;
import com.glumes.openglbasicshape.renderers.BaseRenderer;
import com.glumes.openglbasicshape.renderers.CircleRenderer;
import com.glumes.openglbasicshape.renderers.MultiCubeRender;
import com.glumes.openglbasicshape.renderers.LineRenderer;
import com.glumes.openglbasicshape.renderers.CubeRender;
import com.glumes.openglbasicshape.renderers.PointRenderer;
import com.glumes.openglbasicshape.renderers.RectangleRenderer;
import com.glumes.openglbasicshape.renderers.SphereRenderer;
import com.glumes.openglbasicshape.renderers.TriangleRenderer;
import com.glumes.openglbasicshape.utils.Constant;
import com.glumes.openglbasicshape.utils.RenderType;

public class BaseShapeActivity extends AppCompatActivity {

    private BaseShapeView mBaseShapeView;

    private int mType;

    private SparseArray<BaseRenderer> mRendererArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRendererMap();

        mType = getIntent().getIntExtra(Constant.RENDERER_TYPE, 1);


        BaseRenderer renderer = mRendererArray.get(mType, new PointRenderer(this));

        mBaseShapeView = new BaseShapeView(this, renderer);

        setContentView(mBaseShapeView);
    }


    public void initRendererMap() {
        mRendererArray.put(RenderType.RENDER_TYPE_POINT, new PointRenderer(this));
        mRendererArray.put(RenderType.RENDER_TYPE_LINE, new LineRenderer(this));
        mRendererArray.put(RenderType.RENDER_TYPE_TRIANGLE, new TriangleRenderer(this));
        mRendererArray.put(RenderType.RENDER_TYPE_RECTANGLE, new RectangleRenderer(this));
        mRendererArray.put(RenderType.RENDER_TYPE_CIRCLE, new CircleRenderer(this));
        mRendererArray.put(RenderType.RENDER_TYPE_CUBE, new CubeRender(this));
        mRendererArray.put(RenderType.RENDER_TYPE_MULTI_CUBE, new MultiCubeRender(this));
        mRendererArray.put(RenderType.RENDER_TYPE_SPHERE, new SphereRenderer(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseShapeView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaseShapeView.onResume();
    }
}
