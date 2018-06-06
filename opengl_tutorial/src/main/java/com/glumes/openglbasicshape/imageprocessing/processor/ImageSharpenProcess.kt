package com.glumes.openglbasicshape.imageprocessing.processor

/**
 * @Author  glumes
 */
class ImageSharpenProcess : ImageProcess(vertexShader = NO_FILTER_VERTEX_SHADER, textureShader = SHARPEN_HANDLE) {

    companion object {
        val SHARPEN_HANDLE: String = "" +
                "precision mediump float;\n" +
                "varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   vec2 offset0 = vec2(-1.0,-1.0);\n" +
                "   vec2 offset1 = vec2(0.0,-1.0);\n" +
                "   vec2 offset2 = vec2(1.0,-1.0);\n" +

                "   vec2 offset3 = vec2(-1.0,0.0);\n" +
                "   vec2 offset4 = vec2(0.0,0.0);\n" +
                "   vec2 offset5 = vec2(1.0,0.0);\n" +

                "   vec2 offset6 = vec2(-1.0,1.0);\n" +
                "   vec2 offset7 = vec2(0.0,1.0);\n" +
                "   vec2 offset8 = vec2(1.0,1.0);\n" +

                "   const float scaleFactor = 1.0;\n" +

                "   float kernelValue0 = 0.0;\n" +
                "   float kernelValue1 = -1.0;\n" +
                "   float kernelValue2 = 0.0;\n" +

                "   float kernelValue3 = -1.0;\n" +
                "   float kernelValue4 = 5.0;\n" +
                "   float kernelValue5 = -1.0;\n" +

                "   float kernelValue6 = 0.0;\n" +
                "   float kernelValue7 = -1.0;\n" +
                "   float kernelValue8 = 0.0;\n" +

                "   vec4 sum;\n" +

                "   vec4 cTemp0,cTemp1,cTemp2,cTemp3,cTemp4,cTemp5,cTemp6,cTemp7,cTemp8;\n" +
                "   cTemp0 = texture2D(inputImageTexture,textureCoordinate.st + offset0.xy/512.0);\n" +
                "   cTemp1 = texture2D(inputImageTexture,textureCoordinate.st + offset1.xy/512.0);\n" +
                "   cTemp2 = texture2D(inputImageTexture,textureCoordinate.st + offset1.xy/512.0);\n" +
                "   cTemp3 = texture2D(inputImageTexture,textureCoordinate.st + offset3.xy/512.0);\n" +
                "   cTemp4 = texture2D(inputImageTexture,textureCoordinate.st + offset4.xy/512.0);\n" +
                "   cTemp5 = texture2D(inputImageTexture,textureCoordinate.st + offset5.xy/512.0);\n" +
                "   cTemp6 = texture2D(inputImageTexture,textureCoordinate.st + offset6.xy/512.0);\n" +
                "   cTemp7 = texture2D(inputImageTexture,textureCoordinate.st + offset7.xy/512.0);\n" +
                "   cTemp8 = texture2D(inputImageTexture,textureCoordinate.st + offset8.xy/512.0);\n" +

                "   sum = kernelValue0*cTemp0 + kernelValue1*cTemp1 + kernelValue2*cTemp2 + kernelValue3*cTemp3  + kernelValue4*cTemp4 + kernelValue5*cTemp5 +kernelValue6*cTemp6 + kernelValue7*cTemp7 + kernelValue8*cTemp8;\n" +

                "   gl_FragColor = sum * scaleFactor;\n" +

                "}"

    }
}