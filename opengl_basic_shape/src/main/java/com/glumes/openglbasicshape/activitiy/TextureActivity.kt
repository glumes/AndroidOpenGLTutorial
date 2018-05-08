package com.glumes.openglbasicshape.activitiy

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.draw.texture.CircleTexture
import com.glumes.openglbasicshape.draw.texture.MultiTexture
import com.glumes.openglbasicshape.draw.texture.RectangleTexture
import com.glumes.openglbasicshape.draw.texture.TriangleTexture

class TextureActivity : BaseRenderActivity() {


    override fun initShapeClass() {
        shapeClazzArray.put(R.id.tirangle_texture, TriangleTexture::class.java)
        shapeClazzArray.put(R.id.rectangle_texture, RectangleTexture::class.java)
        shapeClazzArray.put(R.id.circle_texture, CircleTexture::class.java)
        shapeClazzArray.put(R.id.multi_texture, MultiTexture::class.java)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_texture_menu)
    }

}
