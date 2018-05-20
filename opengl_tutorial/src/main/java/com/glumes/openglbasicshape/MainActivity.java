package com.glumes.openglbasicshape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glumes.databindingadapter.DataBindingAdapter;
import com.glumes.databindingadapter.Items;
import com.glumes.openglbasicshape.activitiy.BasicGraphActivity;
import com.glumes.openglbasicshape.activitiy.BasicShapeActivity;
import com.glumes.openglbasicshape.activitiy.TextureActivity;
import com.glumes.openglbasicshape.filter.GLSurfaceViewFilterActivity;
import com.glumes.openglbasicshape.utils.RenderType;
import com.glumes.openglbasicshape.viewholderitem.RenderBinder;
import com.glumes.openglbasicshape.viewholderitem.RenderModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvRenderer)
    RecyclerView rvRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renderer);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRenderer.setLayoutManager(layoutManager);

        Items items = new Items();

        DataBindingAdapter mAdapter = new DataBindingAdapter();
        mAdapter
                .map(RenderModel.class, new RenderBinder())
                .setItems(items);

        rvRenderer.setAdapter(mAdapter);

        items.add(new RenderModel("绘制篇", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("基本图形的绘制", RenderType.RENDER_TYPE_JUMP_ACTIVITY, BasicGraphActivity.class));
        items.add(new RenderModel("基本形状的绘制", RenderType.RENDER_TYPE_JUMP_ACTIVITY, BasicShapeActivity.class));
        items.add(new RenderModel("绘制纹理", RenderType.RENDER_TYPE_JUMP_ACTIVITY, TextureActivity.class));

        items.add(new RenderModel("旋转与移动篇", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("滤镜篇", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("基于GLSurfaceView的滤镜", RenderType.RENDER_TYPE_JUMP_ACTIVITY, GLSurfaceViewFilterActivity.class));

        items.add(new RenderModel("基于EGL的滤镜", RenderType.RENDER_TYPE_JUMP_ACTIVITY));
        items.add(new RenderModel("组合滤镜效果实现", RenderType.RENDER_TYPE_JUMP_ACTIVITY));
        items.add(new RenderModel("多个滤镜切换", RenderType.RENDER_TYPE_JUMP_ACTIVITY));


        items.add(new RenderModel("相机信息", RenderType.RENDER_TYPE_JUMP_ACTIVITY));

        items.add(new RenderModel("触摸操作", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("光照篇", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("VR实践", RenderType.RENDER_TYPE_TITLE));

        items.add(new RenderModel("导入模型", RenderType.RENDER_TYPE_TITLE));


    }


}
