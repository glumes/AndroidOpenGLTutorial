package com.glumes.openglbasicshape.base

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import com.glumes.openglbasicshape.draw.BaseShape
import com.glumes.openglbasicshape.draw.BaseShapeView
import com.glumes.openglbasicshape.draw.graph.Point
import com.glumes.openglbasicshape.draw.shape.Cube
import com.glumes.openglbasicshape.renderers.BasicShapeRender
import com.glumes.openglbasicshape.utils.ACTIVITY_TITLE
import com.glumes.openglbasicshape.utils.RENDERER_SHAPE

/**
 * @Author glumes
 */
abstract class BaseRenderActivity : BaseToolbarActivity() {


    lateinit var mBaseShapeView: BaseShapeView
    lateinit var mRenderer: BasicShapeRender

    var shapeClazzArray = SparseArray<Class<out BaseShape>>(4)

    var clazz: Class<out BaseShape> = Point::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRenderer = BasicShapeRender(this)
        mBaseShapeView = BaseShapeView(this, mRenderer)
        mBaseShapeView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY


        if (savedInstanceState != null) {
            mRenderer.setShape(savedInstanceState.getSerializable(RENDERER_SHAPE) as Class<out BaseShape>)
        } else {
            mRenderer.setShape(Cube::class.java)
        }

        initShapeClass()

        setContentView(mBaseShapeView)
    }

    abstract fun initShapeClass()

    abstract fun setMenuId()

    override fun updateToolbar() {
        setToolbarTitle(intent.getStringExtra(ACTIVITY_TITLE))
        setMenuId()
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            updateShape(item.itemId)
            true
        })
    }

    private fun updateShape(itemId: Int) {
        clazz = shapeClazzArray.get(itemId)
        recreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(RENDERER_SHAPE, clazz)
        super.onSaveInstanceState(outState)
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