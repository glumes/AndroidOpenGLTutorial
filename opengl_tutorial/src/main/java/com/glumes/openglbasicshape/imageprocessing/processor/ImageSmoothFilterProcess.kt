package com.glumes.openglbasicshape.imageprocessing.processor

/**
 * Created by glumes on 06/06/2018
 */
class ImageSmoothFilterProcess : ImageProcess(vertexShader = NO_FILTER_VERTEX_SHADER, textureShader = ImageHaHaProcess.HaHa_FRAGMENT_SHADER) {

    companion object {
        var SMOOTH_FILTER: String = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "void main()\n" +
                "{\n" +
                "vec2 offset0 = vec2(-1.0,-1.0);\n" +
                "vec2 offset1 = vec2(0.0,-1.0);\n" +
                "vec2 offset2 = vec2(1.0,-1.0);\n" +
                "vec2 offset3 = vec2(-1.0,0.0);\n" +
                "vec2 offset4 = vec2(0.0,0.0);\n" +
                "vec2 offset5 = vec2(1.0,0.0);\n" +
                "vec2 offset6 = vec2(-1.0,1.0);\n" +
                "vec2 offset7 = vec2(0.0,1.0f);\n" +
                "vec2 offset8 = vec2(1.0,1.0);\n" +
                "const float scaleFactor = 1.0/9.0;" +
                "float kernelValue0 = 1.0;\n" +
                "float kernelValue1 = 1.0;\n" +
                "float kernelValue2 = 1.0;\n" +
                "float kernelValue3 = 1.0;\n" +
                "float kernelValue4 = 1.0;\n" +
                "float kernelValue5 = 1.0;\n" +
                "float kernelValue6 = 1.0;\n" +
                "float kernelValue7 = 1.0;\n" +
                "float kernelValue8 = 1.0;\n" +
                "vec4 sum;\n" +
                "vec4 cTemp0,cTemp1,cTemp2,cTemp3,cTemp4,cTemp5,cTemp6,cTemp7,cTemp8;\n" +
                "}"

    }
}