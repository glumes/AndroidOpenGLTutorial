package com.gluems.magiccube.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.gluems.magiccube.shape.Cube
import com.gluems.magiccube.shape.CubeFace
import com.gluems.magiccube.shape.CubeVertex
import com.gluems.magiccube.util.MatrixState
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

//    var face: CubeFace = CubeFace(
//            CubeVertex(-0.5f, 0.5f, 0f, 0),
//            CubeVertex(-0.5f, -0.5f, 0f, 1),
//            CubeVertex(0.5f, 0.5f, 0f, 2),
//            CubeVertex(0.5f, -0.5f, 0f, 3)
//    )

    lateinit var face: CubeFace

    var mVertexData = floatArrayOf(
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
            0.5f, -0.5f, 0f)


    var mTextCoorData = floatArrayOf(

    )


    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
//        drawMagicCube()
        face.draw()

    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        var ratio: Float = (width / height).toFloat()

        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 10f)
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
//        for (it in 0..27) {
//            cubes[it].loadTexture()
//        }

        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)

        face = CubeFace(
                CubeVertex(-0.5f, 0.5f, 0f, 0),
                CubeVertex(-0.5f, -0.5f, 0f, 1),
                CubeVertex(0.5f, 0.5f, 0f, 2),
                CubeVertex(0.5f, -0.5f, 0f, 3)
        )
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        face.initShader()
        face.setIndices(mVertexData)
        face.setTextureCoordinates(mTextCoorData)
        //关闭背面剪裁
        GLES30.glDisable(GLES30.GL_CULL_FACE)
    }

    fun drawMagicCube() {
        for (it in cubes) {
            it.drawSample()
        }
    }
}