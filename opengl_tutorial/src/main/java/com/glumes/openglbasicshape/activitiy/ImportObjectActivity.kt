package com.glumes.openglbasicshape.activitiy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseRenderActivity
import com.glumes.openglbasicshape.draw.shape.Sphere
import com.glumes.openglbasicshape.draw.texture.TriangleTexture
import com.glumes.openglbasicshape.obj.LoadedObjectVertexOnly

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
