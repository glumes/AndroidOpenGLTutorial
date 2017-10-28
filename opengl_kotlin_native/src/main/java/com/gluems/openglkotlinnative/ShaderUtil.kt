package com.gluems.openglkotlinnative

import android.content.Context
import android.opengl.GLES30
import com.glumes.comlib.LogUtil
import com.orhanobut.logger.Logger
import java.io.*

/**
 * @Author  glumes
 */
class ShaderUtil {

    companion object {

        fun loadShader(shaderType: Int, source: String): Int {
            var shader: Int = GLES30.glCreateShader(shaderType)

            if (shader != 0) {
                GLES30.glShaderSource(shader, source)
                GLES30.glCompileShader(shader)

                var compiled: IntArray = kotlin.IntArray(1)
                GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)

                if (compiled[0] == 0) {
                    LogUtil.d("count not compile shader")
                    GLES30.glDeleteShader(shader)
                    shader = 0
                }
            }
            return shader
        }

        fun createProgram(vertexSource: String, fragmentSource: String): Int {
            val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
            if (vertexShader == 0) {
                return 0
            }

            val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
            if (fragmentShader == 0) {
                return 0
            }

            var program: Int = GLES30.glCreateProgram()
            if (program != 0) {
                GLES30.glAttachShader(program, vertexShader)
                checkError("glAttachShader")

                GLES30.glAttachShader(program, fragmentShader)
                checkError("glAttachShader")

                GLES30.glLinkProgram(program)

                var linkStatus: IntArray = kotlin.IntArray(1)

                GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)

                if (linkStatus[0] != GLES30.GL_TRUE) {
                    LogUtil.d("can not link program")
                    GLES30.glDeleteProgram(program)
                    program = 0
                }
            }

            return program
        }

        fun checkError(op: String) {
            var error: Int? = 0
            while ({ error = GLES30.glGetError();error }() != GLES30.GL_NO_ERROR) {
                LogUtil.e("error is " + error)
                throw RuntimeException("error is " + error)
            }
        }


        fun readTextFileFromResource(context: Context, resourceId: Int): String {

            val sb = StringBuilder()
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferReader = BufferedReader(inputStreamReader)
            var nextLine: String? = null
            while ({ nextLine = bufferReader.readLine();nextLine }() != null) {
                sb.append(nextLine)
                sb.append('\n')
            }


            Logger.d(sb.toString())
            return sb.toString()
        }

    }
}