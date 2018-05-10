package com.glumes.openglbasicshape.viewholderitem;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.glumes.databindingadapter.BindingViewHolder;
import com.glumes.openglbasicshape.databinding.RenderTypeLayoutBinding;
import com.glumes.openglbasicshape.utils.KotlinConstantKt;
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

        if (renderTypeModel.mType == RenderType.RENDER_TYPE_JUMP_ACTIVITY) {
            mBinding.text.setTextColor(Color.BLUE);

            mBinding.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (renderTypeModel.mClass == null){
                        return;
                    }
                    Intent intent = new Intent(v.getContext(), renderTypeModel.mClass);
                    intent.putExtra(KotlinConstantKt.ACTIVITY_TITLE, renderTypeModel.mTitle);
                    v.getContext().startActivity(intent);
                }
            });
        } else if (renderTypeModel.mType == RenderType.RENDER_TYPE_TITLE) {

            mBinding.text.setTextColor(Color.BLACK);

        }
    }

}
