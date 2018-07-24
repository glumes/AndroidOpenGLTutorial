package com.glumes.openglbasicshape.collision.collisionfragment


import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.glumes.importobject.utils.ObjectLoadUtil
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.collision.CollisionObject
import com.glumes.openglbasicshape.collision.LovoGoThread
import com.glumes.openglbasicshape.collision.RigidBody
import com.glumes.openglbasicshape.collision.Vector3f
import com.glumes.openglbasicshape.utils.LoadUtil
import com.glumes.openglbasicshape.utils.MatrixState
import java.util.ArrayList
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * A simple [Fragment] subclass.
 *
 */
class AABBFragment : Fragment() {

    lateinit var mView: AABBSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = AABBSurfaceView(context!!).also {
            it.requestFocus()
            it.isFocusableInTouchMode = true
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
}

class AABBSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {


    private var mRenderer: AABBRenderer

    lateinit var ch: CollisionObject
    lateinit var pm: CollisionObject

    var aList = ArrayList<RigidBody>()
    lateinit var lgt: LovoGoThread

    init {
        mRenderer = AABBRenderer()
        setEGLContextClientVersion(3)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }


    private inner class AABBRenderer : GLSurfaceView.Renderer {

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            for (it in 0 until aList.size) {
                aList[it].drawSelf()
            }
            pm.drawSelf()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 4f, 100f)
            MatrixState.setCamera(0f, 13f, 40f, 0f, 0f, -10f, 0f, 1.0f, 0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            GLES30.glEnable(GLES30.GL_CULL_FACE)
            MatrixState.setInitStack()
            MatrixState.setLightLocation(40f, 10f, 20f)

            ch = ObjectLoadUtil.loadCollisionObjectloadFromFile("collision/ch.obj", mContext.resources)
            pm = ObjectLoadUtil.loadCollisionObjectloadFromFile("collision/pm.obj", mContext.resources)

            aList.add(RigidBody(ch, true, Vector3f(-13f, 0f, 0f), Vector3f(0f, 0f, 0f)))
            aList.add(RigidBody(ch, true, Vector3f(13f, 0f, 0f), Vector3f(0f, 0f, 0f)))
            aList.add(RigidBody(ch, false, Vector3f(0f, 0f, 0f), Vector3f(0.1f, 0f, 0f)))

            lgt = LovoGoThread(aList)
            lgt.start()
        }

    }
}
