package com.glumes.openglbasicshape

import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.objects.texture.TextureTriangle

class TextureActivity : BaseRenderActivity() {

    override fun initShapeClass() {
        shapeClazzArray.put(R.id.tirangle_texture,TextureTriangle::class.java)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_texture_menu)
    }


}
