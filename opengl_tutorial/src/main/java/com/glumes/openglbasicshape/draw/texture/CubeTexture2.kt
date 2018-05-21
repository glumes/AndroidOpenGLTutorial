package com.glumes.openglbasicshape.draw.texture

import android.content.Context
import android.opengl.GLES20
import com.glumes.openglbasicshape.utils.MatrixState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */
class CubeTexture2(context: Context) : BaseCube(context) {


    val eyeDistance = 2.0f
    var num = 0
    var RotateNum = 360
    val radian = (2 * Math.PI / RotateNum).toFloat()


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        Observable.interval(30, TimeUnit.MILLISECONDS)
                .subscribe {
                    eyeX = eyeDistance * Math.sin((radian * num).toDouble()).toFloat()
                    eyeZ = eyeDistance * Math.cos((radian * num).toDouble()).toFloat()
                    num++
                    if (num > 360) {
                        num = 0
                    }
                }
        MatrixState.rotate(-30f, 0f, 0f, 1f)
    }

    override fun onDrawCubePre() {
        super.onDrawCubePre()

        // 控制调整相机来观察不同的面
        MatrixState.setCamera(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        GLES20.glUniformMatrix4fv(uViewMatrixAttr, 1, false, MatrixState.getVMatrix(), 0)
    }

}