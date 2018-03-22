package com.glumes.openglbasicshape.activitiy

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.draw.shape.Cube
import com.glumes.openglbasicshape.draw.shape.Sphere

class BasicShapeActivity : BaseRenderActivity() {


    override fun initShapeClass() {
        shapeClazzArray.put(R.id.sphere,Sphere::class.java)
        shapeClazzArray.put(R.id.cube,Cube::class.java)
    }


    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_shape_menu)
    }


}
