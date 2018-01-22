package com.glumes.openglbasicshape

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.glumes.openglbasicshape.base.BaseToolbarActivity
import com.glumes.openglbasicshape.glviews.BaseShapeView
import com.glumes.openglbasicshape.objects.BaseShape
import com.glumes.openglbasicshape.objects.shape.Cube
import com.glumes.openglbasicshape.renderers.BasicShapeRender
import com.glumes.openglbasicshape.utils.ACTIVITY_TITLE

class BasicShapeActivity : BaseToolbarActivity() {

    lateinit var mBaseShapeView: BaseShapeView
    lateinit var mRenderer: BasicShapeRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mRenderer = BasicShapeRender(this)
        mBaseShapeView = BaseShapeView(this,mRenderer)
        mBaseShapeView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        if (savedInstanceState != null) {
            mRenderer.setShape(savedInstanceState.getSerializable("shape") as Class<out BaseShape>)
        } else {
            mRenderer.setShape(Cube::class.java)
        }

        setContentView(mBaseShapeView)
    }


    override fun updateToolbar() {
        setToolbarTitle(intent.getStringExtra(ACTIVITY_TITLE))
        setToolbarMenu(R.menu.basic_shape_menu)
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            updateShape(item.itemId)
            true
        })
    }

    fun updateShape(itemId: Int) {

    }

    override fun onResume() {
        super.onResume()
        mBaseShapeView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBaseShapeView.onPause()
    }


}
