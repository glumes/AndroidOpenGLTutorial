package com.glumes.openglbasicshape.activitiy

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.draw.graph.*

class BasicGraphActivity : BaseRenderActivity() {

    override fun initShapeClass() {
        shapeClazzArray.put(R.id.point, Point::class.java)
        shapeClazzArray.put(R.id.line, Line::class.java)
        shapeClazzArray.put(R.id.triangle, Triangle::class.java)
        shapeClazzArray.put(R.id.rectangle, Rectangle::class.java)
        shapeClazzArray.put(R.id.polygon, Polygon::class.java)
        shapeClazzArray.put(R.id.circle, Circle::class.java)

    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.basic_graph_menu)
    }


    override fun setInitShape() {
        super.setInitShape()
        mRenderer.setShape(Point::class.java)
    }
}
