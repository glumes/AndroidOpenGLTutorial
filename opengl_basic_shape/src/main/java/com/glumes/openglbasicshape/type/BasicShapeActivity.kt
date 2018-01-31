package com.glumes.openglbasicshape.type

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.objects.shape.Cube
import com.glumes.openglbasicshape.objects.shape.Sphere

class BasicShapeActivity : BaseRenderActivity() {


    override fun initShapeClass() {
        shapeClazzArray.put(R.id.sphere,Sphere::class.java)
        shapeClazzArray.put(R.id.cube,Cube::class.java)
    }


    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_shape_menu)
    }


}
