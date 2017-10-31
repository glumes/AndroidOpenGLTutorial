package com.glumes.openglbasicshape.viewholderitem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.glumes.databindingadapter.BindingViewHolder;
import com.glumes.openglbasicshape.BaseShapeActivity;
import com.glumes.openglbasicshape.databinding.RenderTypeLayoutBinding;
import com.glumes.openglbasicshape.utils.Constant;
import com.glumes.openglbasicshape.utils.RenderType;

/**
 * @Author glumes
 */

public class RenderViewHolder extends BindingViewHolder<RenderModel, RenderTypeLayoutBinding> {


    public RenderViewHolder(RenderTypeLayoutBinding binding) {
        super(binding);
    }

    @Override
    protected void onBind(final RenderModel renderTypeModel, int i) {
        mBinding.setViewmodel(renderTypeModel);
        mBinding.executePendingBindings();

        if (renderTypeModel.mType == RenderType.RENDER_TYPE_TITLE) {
            mBinding.text.setTextColor(Color.BLUE);
        }

        mBinding.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RenderType.RENDER_TYPE_LIST.contains(renderTypeModel.mType)) {
                    startActivity(v.getContext(), renderTypeModel.mType);
                }
            }
        });
    }

    private void startActivity(Context context, int type) {
        Intent intent = new Intent(context, BaseShapeActivity.class);
        intent.putExtra(Constant.RENDERER_TYPE, type);
        context.startActivity(intent);
    }
}
