package com.glumes.openglbasicshape.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by glumes on 11/05/2018
 */
class ActivityUtils {

    companion object {

        fun replaceFragment(frag: Fragment, fragManager: FragmentManager, contentId: Int) {
            checkNotNull(frag)
            checkNotNull(fragManager)
            val transaction = fragManager.beginTransaction()
            transaction.replace(contentId, frag)
            transaction.commit()
        }

        private fun <T> checkNotNull(reference: T?): T {
            return if (reference == null) {
                throw NullPointerException()
            } else {
                reference
            }
        }
    }
}