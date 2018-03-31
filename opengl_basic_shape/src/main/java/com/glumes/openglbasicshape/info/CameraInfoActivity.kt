package com.glumes.openglbasicshape.info

import android.Manifest
import android.graphics.SurfaceTexture
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import com.glumes.camera.Camera2
import com.glumes.camera.CameraUtil
import com.glumes.openglbasicshape.R
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_camera_info.*


/**
 * 打开相机的流程：
 * 1、根据相机的提供的画面尺寸，以及生产的照片尺寸，还有预览的界面的尺寸综合得出合适的比例
 * 2、调整旋转的方向
 * 3、调整 3A 模式
 *
 */
class CameraInfoActivity : AppCompatActivity() {


    val mCamera2 = Camera2(this)

    var mSurfaceWidth = 0
    var mSurfaceHeight = 0
    var mSurfaceTexture: SurfaceTexture? = null

    private val REQUEST_CAMERA_PERMISSION = 1


    private val mSurfaceTextureListener = object : SurfaceTextureListener {

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return true
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {

            mSurfaceTexture = surface!!
            mSurfaceWidth = width
            mSurfaceHeight = height
            openCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!mCamera2.openCamera()) {
            Logger.d("open camera failed")
        }

        setContentView(R.layout.activity_camera_info)
    }


    override fun onResume() {
        super.onResume()
        Logger.d("onresume")
        if (mCameraView.isAvailable) {
            openCamera()
        } else {
            mCameraView.surfaceTextureListener = mSurfaceTextureListener
        }
    }

    private fun openCamera() {
        if (!CameraUtil.checkCameraPermission(this)) {
            requestCameraPermission()
            return
        }


        mCamera2.setPreviewSurfaceTexture(mSurfaceTexture)

        mCamera2.setPreviewSize(mSurfaceWidth, mSurfaceHeight)

        mCamera2.openCamera()

        mCamera2.startPreview()

    }


    private fun requestCameraPermission() {
        // 如果之前请求过该权限但用户拒绝了请求，此方法将返回 true
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {

            }
        }
    }

}
