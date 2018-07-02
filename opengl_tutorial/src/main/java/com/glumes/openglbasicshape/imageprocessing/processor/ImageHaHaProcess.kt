package com.glumes.openglbasicshape.imageprocessing.processor

import android.graphics.PointF
import android.opengl.GLES20
import com.glumes.openglbasicshape.base.LogUtil

/**
 * Created by glumes on 06/06/2018
 */
class ImageHaHaProcess : ImageProcess(vertexShader = NO_FILTER_VERTEX_SHADER, textureShader = HaHa_FRAGMENT_SHADER) {


    companion object {
        val HaHa_FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                "uniform sampler2D inputImageTexture;\n" +
                " \n" +
                "uniform vec2 center;\n" +
                "void main()\n" +
                "{\n" +
                " highp vec2 normCoord = 2.0 * textureCoordinate - 1.0; \n" +
                " highp vec2 normCenter = 2.0 * center - 1.0;\n" +
                " normCoord -= normCenter;\n" +
                " mediump vec2 s = sign(normCoord);\n" +
                " normCoord = abs(normCoord);\n" +
                " normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n" +
                " normCoord = s * normCoord;\n" +
                " normCoord += normCenter;\n" +
                " mediump vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;\n" +
                " gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n" +
                "}"

    }

    private var mCenter: Int = 0


    override fun onInit() {
        super.onInit()
        mCenter = GLES20.glGetUniformLocation(getProgram(), "center")
        LogUtil.d("onInitialized")
    }

    override fun onInitialized() {
        super.onInitialized()
        setPoint(mCenter, PointF(0.5f, 0.5f))
        LogUtil.d("onInitialized")
    }


}