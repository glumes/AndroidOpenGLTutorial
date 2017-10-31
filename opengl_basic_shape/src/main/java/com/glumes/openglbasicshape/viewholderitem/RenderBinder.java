package com.glumes.openglbasicshape.viewholderitem;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.glumes.databindingadapter.ViewHolderBinder;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.databinding.RenderTypeLayoutBinding;

/**
 * @Author glumes
 */

public class RenderBinder extends ViewHolderBinder<RenderModel, RenderViewHolder> {


    @Override
    public RenderViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {

        RenderTypeLayoutBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.render_type_layout, viewGroup, false);
        return new RenderViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RenderViewHolder renderViewHolder, RenderModel renderModel, int i) {
        renderViewHolder.onBind(renderModel, i);
    }
}
