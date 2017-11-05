package com.gluems.magiccube.render

import android.opengl.GLSurfaceView
import com.gluems.magiccube.shape.Cube
import com.orhanobut.logger.Logger
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author glumes
 */
class MagicCubeRender : GLSurfaceView.Renderer {


    var cubes: List<Cube> = List(27) {
        Cube(it)
    }


    override fun onDrawFrame(p0: GL10?) {
        drawMagicCube()
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {

    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

    }

    fun drawMagicCube() {
        for (it in cubes) {
            it.drawSample()
        }
    }
}