package com.glumes.openglbasicshape

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import com.glumes.openglbasicshape.base.BaseToolbarActivity
import com.glumes.openglbasicshape.glviews.BaseShapeView
import com.glumes.openglbasicshape.objects.*
import com.glumes.openglbasicshape.renderers.BasicShapeRender
import com.glumes.openglbasicshape.utils.ACTIVITY_TITLE

class BasicGraphActivity : BaseToolbarActivity() {

    lateinit var mBaseShapeView: BaseShapeView
    lateinit var mRenderer: BasicShapeRender

    var shapeClazzArray = SparseArray<Class<out BaseShape>>(4)


    var clazz: Class<out BaseShape> = Point::class.java



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRenderer = BasicShapeRender(this)

        mBaseShapeView = BaseShapeView(this, mRenderer)


        if (savedInstanceState != null) {
            mRenderer.setShape(savedInstanceState.getSerializable("shape") as Class<out BaseShape>)
        } else {
            mRenderer.setShape(Point::class.java)
        }

        shapeClazzArray.put(R.id.point,Point::class.java)
        shapeClazzArray.put(R.id.line,Line::class.java)
        shapeClazzArray.put(R.id.rectangle,Rectangle::class.java)
        shapeClazzArray.put(R.id.polygon,Polygon::class.java)
        shapeClazzArray.put(R.id.circle,Circle::class.java)
        shapeClazzArray.put(R.id.cube,Cube::class.java)
        shapeClazzArray.put(R.id.sphere,Sphere::class.java)

        setContentView(mBaseShapeView)
    }

    override fun updateToolbar() {

        setToolbarTitle(intent.getStringExtra(ACTIVITY_TITLE))
        setToolbarMenu(R.menu.basic_graph_menu)
        setToolbarMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            updateShape(item.itemId)
            true
        })
    }


    private fun updateShape(menuId: Int) {

        clazz = shapeClazzArray.get(menuId)

        recreate()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("shape",clazz)
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
