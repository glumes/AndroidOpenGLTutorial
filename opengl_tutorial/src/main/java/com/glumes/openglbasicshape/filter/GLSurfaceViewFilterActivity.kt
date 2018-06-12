package com.glumes.openglbasicshape.filter

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions

class GLSurfaceViewFilterActivity : AppCompatActivity() {


    var mView: FilterSurfaceView? = null
    lateinit var mPermission: RxPermissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPermission = RxPermissions(this)
        mPermission.request(Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        mView = FilterSurfaceView(this)
                        setContentView(mView)
                    } else {
                        val tipsView = TextView(this)
                        tipsView.text = "请打开相机权限"
                        setContentView(tipsView)
                    }
                }

    }

    override fun onResume() {
        super.onResume()
        mView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView?.onPause()
    }


}
