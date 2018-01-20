package com.glumes.openglbasicshape.viewholderitem;

/**
 * @Author glumes
 */

public class RenderModel {

    public String mTitle;
    public int mType;
    public Class<?> mClass ;

    public RenderModel(String mTitle, int mType) {
        this.mTitle = mTitle;
        this.mType = mType;
    }

    public RenderModel(String title, int type, Class<?> aClass) {
        mTitle = title;
        mType = type;
        mClass = aClass;
    }
}
