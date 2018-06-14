package com.glumes.openglbasicshape

import android.Manifest
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.hardware.Camera
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.FloatingActionButton
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.glumes.openglbasicshape.base.LogUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.ArrayBlockingQueue
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class CameraRawDataCodecActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PreviewCallback {

    private val mButton: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.floatingActionButton)
    }

    private val mSurfaceView: SurfaceView by lazy {
        findViewById<SurfaceView>(R.id.surfaceView)
    }

    private lateinit var mSurfaceHolder: SurfaceHolder
    private val mPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    private val mWidth = 1280
    private val mHeight = 720
    private val framerate = 30
    private val biterate = 8500 * 1000

    private val yuvqueuesize = 10

    var YUVQueue = ArrayBlockingQueue<ByteArray>(yuvqueuesize)

    private var mCamera: Camera? = null

    private val videoEncoder by lazy {
        VideoEncoder(mWidth, mHeight, framerate, biterate)
    }

    lateinit var mVideoEncoder: VideoEncoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_raw_data_codec)


        mSurfaceHolder = mSurfaceView.holder
        mSurfaceHolder.addCallback(this)

        mButton.setOnClickListener {
            startVideoRecord()
        }

        mVideoEncoder = VideoEncoder(mWidth, mHeight, framerate, biterate)

        checkMediaCodecSupport()
    }


    private fun checkMediaCodecSupport(): Boolean {
        for (j in MediaCodecList.getCodecCount() - 1 downTo 0) {
            val codecInfo = MediaCodecList.getCodecInfoAt(j)

            val types = codecInfo.supportedTypes
            for (i in types.indices) {
                if (types[i].equals("video/avc", ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    private fun startVideoRecord() {
        videoEncoder.startRecord()
    }


    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        if (YUVQueue.size >= 10) {
            YUVQueue.poll()
        }
        YUVQueue.add(data!!)
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        setUpCamera()
        LogUtil.d("surface created")
        mVideoEncoder.startRecord()
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCamera?.setPreviewCallback(null)
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
        mVideoEncoder.stopRecord()
    }

    private fun setUpCamera() {
        if (!checkPermission()) {
            return
        }

        mCamera = Camera.open()
        mCamera!!.setPreviewCallback(this)
        mCamera!!.setDisplayOrientation(90)
        val mParameter = mCamera!!.parameters
        mParameter.previewFormat = ImageFormat.NV21
        mParameter.setPreviewSize(mWidth, mHeight)
        mCamera!!.parameters = mParameter
        mCamera!!.setPreviewDisplay(mSurfaceHolder)
        mCamera!!.startPreview()
        LogUtil.d("start preview")
    }

    private fun checkPermission(): Boolean {
        var result = false
        mPermissions.request(Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        LogUtil.d("have permission")
                    } else {
                        Toast.makeText(this, R.string.no_camera_permission, Toast.LENGTH_SHORT).show()
                    }
                    result = it
                }

        return result
    }


    inner class VideoEncoder(var mWidth: Int, var mHeight: Int, var frameRate: Int, var bitrate: Int) {


        var mediaCodec: MediaCodec
        var isRunning = false

        private val TIMEOUT_USEC = 12000L

        var configbyte: ByteArray? = null

        private val path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.h264"
        private var outputStream: BufferedOutputStream? = null

        init {
            /**
             * 指定视频的宽高、帧率、码率、I 帧间隔等基本信息。
             * 还要指定 YUV 的颜色格式
             * 相机输出的 YUV 帧格式选择 NV21 格式，但是 NV21 并不是所有机器的 MediaCodec 都支持作为编码器的输入格式
             */
            val mediaFormat = MediaFormat.createVideoFormat("video/avc", mWidth, mHeight)
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, mWidth * mHeight * 5)
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)

            mediaCodec = MediaCodec.createEncoderByType("video/avc")
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mediaCodec.start()

            var file = File(path)
            if (file.exists()) {
                file.delete()
            }
            outputStream = BufferedOutputStream(FileOutputStream(file))
        }


        private fun stopEncoder() {
            mediaCodec.stop()
            mediaCodec.release()
        }


        fun stopRecord() {
            isRunning = false
            stopEncoder()
            outputStream!!.flush()
            outputStream!!.close()
        }

        fun startRecord() {
            val EncoderThread = Thread(Runnable {
                isRunning = true
                var input: ByteArray? = null
                var pts = 0L
                var generateIndex = 0L

                while (isRunning) {
                    if (YUVQueue.size > 0) {
                        input = YUVQueue.poll()
                        var yuv420sp = ByteArray(mWidth * mHeight * 3 / 2)
                        NV21ToNV12(input, yuv420sp, mWidth, mHeight)
                        input = yuv420sp
                    }


                    if (input != null) {

                        try {

                            var inputBuffers = mediaCodec.inputBuffers
                            var outputBuffers = mediaCodec.outputBuffers
                            var inputBufferIndex = mediaCodec.dequeueInputBuffer(-1)

                            if (inputBufferIndex >= 0) {
                                pts = computePresentationTime(generateIndex)
                                var inputBuffer = inputBuffers[inputBufferIndex]
                                inputBuffer.clear()
                                inputBuffer.put(input)
                                mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.size, pts, 0)
                                generateIndex += 1
                            }

                            var bufferInfo = MediaCodec.BufferInfo()
                            var outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC)

                            while (outputBufferIndex >= 0) {

                                var outputBuffer = outputBuffers[outputBufferIndex]
                                var outData = ByteArray(bufferInfo.size)
                                outputBuffer.get(outData)

                                if (bufferInfo.flags == 2) {
                                    configbyte = ByteArray(bufferInfo.size)
                                    configbyte = outData
                                } else if (bufferInfo.flags == 1) {
                                    var keyFrame = ByteArray(bufferInfo.size + configbyte!!.size)
                                    System.arraycopy(configbyte, 0, keyFrame, 0, configbyte!!.size)
                                    System.arraycopy(outData, 0, keyFrame, configbyte!!.size, outData.size)

                                    outputStream!!.write(keyFrame, 0, keyFrame.size)
                                } else {
                                    outputStream!!.write(outData, 0, outData.size)
                                }
                                mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC)
                            }
                        } catch (t: Throwable) {
                            LogUtil.d(t.message)
                        }
                    } else {
                        Thread.sleep(500)
                    }
                }
            })

            EncoderThread.start()
        }

        private fun computePresentationTime(frameIndex: Long): Long {
            return 132 + frameIndex * 1000000 / frameRate
        }


        private fun NV21ToNV12(nv21: ByteArray, nv12: ByteArray, width: Int, height: Int) {
            var framesize = width * height
            var i = 0
            var j = 0
            System.arraycopy(nv21, 0, nv12, 0, framesize)
            i = 0
            while (i < framesize) {
                nv12[i] = nv21[i]
                i++
            }
            j = 0
            while (j < framesize / 2) {
                nv12[framesize + j - 1] = nv21[j + framesize]
                j += 2
            }
            j = 0
            while (j < framesize / 2) {
                nv12[framesize + j] = nv21[j + framesize - 1]
                j += 2
            }
        }
    }
}