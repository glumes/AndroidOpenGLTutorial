package com.glumes.openglbasicshape.obj

import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity

class ImportObjectActivity : BaseRenderActivity() {

    override fun initShapeClass() {
        shapeClazzArray.put(R.id.obj, LoadedObjectVertexOnly::class.java)

    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.object_import_menu)
    }

    override fun setInitShape() {
        super.setInitShape()
        mRenderer.setShape(LoadedObjectVertexOnly::class.java)
    }

}
