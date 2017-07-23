package com.glumes.openglbasicshape;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by glumes on 2017/7/22.
 */

public class RendererAdpater extends RecyclerView.Adapter<RendererAdpater.RendererViewHolder> {


    private Context mContext;
    private List<String> mRendererList;
    private onItemClickListener mListener;

    public RendererAdpater(Context mContext, List<String> mRendererList) {
        this.mContext = mContext;
        this.mRendererList = mRendererList;
    }

    @Override
    public RendererViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RendererViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.renderder_item_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RendererViewHolder holder, final int position) {
        holder.textView.setText(mRendererList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRendererList.size();
    }

    public class RendererViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public RendererViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvRenderer);
        }

    }

    public void setmListener(onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }
}
