package com.glumes.openglbasicshape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glumes.databindingadapter.DataBindingAdapter;
import com.glumes.databindingadapter.Items;
import com.glumes.openglbasicshape.utils.RenderType;
import com.glumes.openglbasicshape.viewholderitem.RenderBinder;
import com.glumes.openglbasicshape.viewholderitem.RenderModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RendererActivity extends AppCompatActivity {

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
        mAdapter.map(RenderModel.class, new RenderBinder()).setItems(items);


        rvRenderer.setAdapter(mAdapter);

        items.add(new RenderModel("基本形状的绘制", RenderType.RENDER_TYPE_TITLE));
        items.add(new RenderModel("绘制点", RenderType.RENDER_TYPE_POINT));
        items.add(new RenderModel("绘制线", RenderType.RENDER_TYPE_LINE));
        items.add(new RenderModel("绘制三角形", RenderType.RENDER_TYPE_TRIANGLE));
        items.add(new RenderModel("绘制矩形", RenderType.RENDER_TYPE_RECTANGLE));
        items.add(new RenderModel("绘制圆", RenderType.RENDER_TYPE_CIRCLE));
        items.add(new RenderModel("绘制立方体", RenderType.RENDER_TYPE_CUBE));
        items.add(new RenderModel("绘制多个立方体", RenderType.RENDER_TYPE_MULTI_CUBE));
        items.add(new RenderModel("绘制球体", RenderType.RENDER_TYPE_SPHERE));

        items.add(new RenderModel("参考的魔方游戏", RenderType.RENDER_TYPE_TITLE));
    }


}
