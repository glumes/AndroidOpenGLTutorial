package com.glumes.openglbasicshape.filter

import android.util.SparseArray
import com.glumes.gpuimage.GPUImageFilter
import com.glumes.gpuimage.GPUImageOpacityFilter
import com.glumes.gpuimage.GPUImageRGBFilter
import com.glumes.gpuimage.GPUImageSketchFilter
import com.glumes.openglbasicshape.base.LogUtil

/**
 * Created by glumes on 14/05/2018
 */


const val NORMAL_FILTER = 0x00
const val RGB = 0x01
const val OPACITY = 0x02
const val SKETCH = 0x03

class FilterFactory {


    companion object {

        private val mFilterCache: SparseArray<GPUImageFilter> = SparseArray()

        private var mCurrentFilterIndex = 0
        fun getFilter(type: Int): GPUImageFilter {

            var filter = mFilterCache.get(type)
            if (filter == null) {
                when (type) {
                    NORMAL_FILTER -> {
                        filter = GPUImageFilter()
                    }
                    RGB -> {
                        filter = GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
                    }
                    OPACITY -> {
                        filter = GPUImageOpacityFilter(1.0f)
                    }
                    SKETCH -> {
                        filter = GPUImageSketchFilter()
                    }

                }
                mFilterCache.put(type, filter)
            }
            return filter
        }

        fun getFilterCycle(): GPUImageFilter {
            mCurrentFilterIndex += 1
            LogUtil.d("filter index is " + mCurrentFilterIndex)
            return getFilter(mCurrentFilterIndex % 4)
        }
    }
}