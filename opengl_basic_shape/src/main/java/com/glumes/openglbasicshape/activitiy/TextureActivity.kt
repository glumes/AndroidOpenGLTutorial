package com.glumes.openglbasicshape.activitiy

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.draw.texture.SphereTexture
import com.glumes.openglbasicshape.draw.texture.TextureTriangle

class TextureActivity : BaseRenderActivity() {


    override fun initShapeClass() {
        shapeClazzArray.put(R.id.tirangle_texture,TextureTriangle::class.java)
        shapeClazzArray.put(R.id.rectangle_texture,SphereTexture::class.java)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_texture_menu)
    }

}
