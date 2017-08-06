package com.glumes.openglbasicshape;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glumes.openglbasicshape.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RendererActivity extends AppCompatActivity {

    @BindView(R.id.rvRenderer)
    RecyclerView rvRenderer;

    private List<String> mRenderList;
    private RendererAdpater mRendererAdapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renderer);
        ButterKnife.bind(this);

        mContext = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRenderer.setLayoutManager(layoutManager);

        mRendererAdapter = new RendererAdpater(this, getmRenderList());
        mRendererAdapter.setmListener(new RendererAdpater.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(Constant.RENDERER_TYPE, position);
                startActivity(intent);
            }
        });
        rvRenderer.setAdapter(mRendererAdapter);

    }

    public List<String> getmRenderList() {

        mRenderList = new ArrayList<>();
        mRenderList.add("Point Render");
        mRenderList.add("Line Render");
        mRenderList.add("Triangle Render");
        mRenderList.add("Rectangle Render");
        mRenderList.add("Circle Render");
        return mRenderList;
    }
}
