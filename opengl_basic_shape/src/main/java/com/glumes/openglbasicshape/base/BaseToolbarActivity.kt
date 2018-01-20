package com.glumes.openglbasicshape.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.glumes.openglbasicshape.R

abstract class BaseToolbarActivity : AppCompatActivity() {


    private lateinit var mToolbar: Toolbar
    private lateinit var mContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_toolbar)
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        mContainer = findViewById(R.id.baseContainer) as FrameLayout
        updateToolbar()

    }

    private fun setContent(id: Int) {
        LayoutInflater.from(this).inflate(id, mContainer)
    }

    override fun setContentView(layoutResID: Int) {
        if (layoutResID == R.layout.activity_base_toolbar) {
            super.setContentView(layoutResID)
        } else {
            setContent(layoutResID)
        }
    }

    override fun setContentView(view: View) {
        mContainer.removeAllViews()
        mContainer.addView(view)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        mContainer.removeAllViews()
        mContainer.addView(view,params)
    }

    fun setToolbarTitle(title: String) {
        mToolbar.title = title
    }

    fun setToolbarTitle(title: Int) {
        mToolbar.setTitle(title)
    }

    fun setToolbarMenu(menu: Int) {
        mToolbar.inflateMenu(menu)
    }

    fun setToolbarMenuItemClickListener(listener: Toolbar.OnMenuItemClickListener) {
        mToolbar.setOnMenuItemClickListener(listener)
    }

    fun setToolbarLogo(logo: Int) {
        mToolbar.setLogo(logo)
    }

    abstract fun updateToolbar()

}
