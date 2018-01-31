package com.glumes.openglbasicshape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glumes.databindingadapter.DataBindingAdapter;
import com.glumes.databindingadapter.Items;
import com.glumes.openglbasicshape.type.BasicGraphActivity;
import com.glumes.openglbasicshape.type.BasicShapeActivity;
import com.glumes.openglbasicshape.type.TextureActivity;
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

        items.add(new RenderModel("基本图形的绘制", RenderType.RENDER_TYPE_TITLE, BasicGraphActivity.class));
        items.add(new RenderModel("基本形状的绘制", RenderType.RENDER_TYPE_TITLE, BasicShapeActivity.class));
        items.add(new RenderModel("绘制的使用", RenderType.RENDER_TYPE_TITLE, TextureActivity.class));
    }


}
