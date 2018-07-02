package com.glumes.openglbasicshape.imageprocessing

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.imageprocessing.processor.ImageProcess
import com.glumes.openglbasicshape.utils.TextureHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 负责传入纹理和坐标，而 Filter 则只负责对图像进行处理
 */
class ImageProcessRenderer(private var mContext: Context) : GLSurfaceView.Renderer {

    private var mImageProcess: ImageProcess? = ImageProcess()

    private val mRunOnDraw: Queue<Runnable>
    private val mRunOnDrawEnd: Queue<Runnable>
    private var textureId: Int = 0

    private val mVertexBuffer: FloatBuffer
    private val mTextureBuffer: FloatBuffer

    private val vertexArray = floatArrayOf(-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f)

    private val textureArray = floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f)

    init {
        mRunOnDraw = LinkedList()
        mRunOnDrawEnd = LinkedList()

        mVertexBuffer = ByteBuffer
                .allocateDirect(vertexArray.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        mVertexBuffer.put(vertexArray).position(0)

        mTextureBuffer = ByteBuffer
                .allocateDirect(textureArray.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        mTextureBuffer.put(textureArray).position(0)

    }


    override fun onDrawFrame(p0: GL10?) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        runAll(mRunOnDraw)
        mImageProcess?.onDraw(textureId, mVertexBuffer, mTextureBuffer)
        runAll(mRunOnDrawEnd)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        mImageProcess?.init()
        textureId = TextureHelper.loadTexture(mContext, R.drawable.texture)
    }

    fun changeImageSource() {

    }

    /**
     * 改变图片处理器，需要在 onDrawFrame 中去响应和应用到这个更改。
     * 1.通过 new 和 current 在 onDrawFrame 中做比较来更改 参考 grafika
     * 2.通过在 onDrawFrame 添加线程队列来处理 参考 gpuimage
     */
    fun changeImageProcess(processor: ImageProcess) {
        addRunOnDraw(Runnable {
            val oldImageProcess = mImageProcess
            mImageProcess = processor
            oldImageProcess?.destroy()
            mImageProcess!!.init()
            GLES20.glUseProgram(mImageProcess!!.getProgram())
        })
    }


    protected fun addRunOnDraw(runnable: Runnable) {
        synchronized(mRunOnDraw) {
            mRunOnDraw.add(runnable)
        }
    }

    protected fun addRunOnDrawEnd(runnable: Runnable) {
        synchronized(mRunOnDrawEnd) {
            mRunOnDrawEnd.add(runnable)
        }
    }

    private fun runAll(queue: Queue<Runnable>) {
        synchronized(queue) {
            while (!queue.isEmpty()) {
                queue.poll().run()
            }
        }
    }

}

