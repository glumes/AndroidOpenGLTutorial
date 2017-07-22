package com.glumes.openglbasicshape;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by glumes on 2017/7/22.
 */

public class RendererAdpater extends RecyclerView.Adapter<RendererAdpater.RendererViewHolder> {


    @Override
    public RendererViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RendererViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RendererViewHolder extends RecyclerView.ViewHolder {

        public RendererViewHolder(View itemView) {
            super(itemView);
        }

    }
}
