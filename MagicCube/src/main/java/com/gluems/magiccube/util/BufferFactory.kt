package com.gluems.magiccube.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * @Author  glumes
 */
class BufferFactory {

    companion object {
        fun newShortBuffer(shortArray: ShortArray): ShortBuffer {
            val bb = ByteBuffer.allocateDirect(shortArray.size * 2)
            bb.order(ByteOrder.nativeOrder())
            val sb = bb.asShortBuffer()
            sb.put(shortArray)
            sb.position(0)
            return sb
        }

        fun newFloatBuffer(floatArray: FloatArray): FloatBuffer {
            val bb = ByteBuffer.allocateDirect(floatArray.size * 4)
            bb.order(ByteOrder.nativeOrder())
            val fb = bb.asFloatBuffer()
            fb.put(floatArray)
            fb.position(0)
            return fb
        }
    }
}