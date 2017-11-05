package com.gluems.magiccube.shape

import com.gluems.magiccube.util.*

/**
 * @Author  glumes
 */
class Face(var id: Int = 0) {

    var P1: FloatArray = kotlin.FloatArray(3)
    var P2: FloatArray = kotlin.FloatArray(3)
    var P3: FloatArray = kotlin.FloatArray(3)
    var P4: FloatArray = kotlin.FloatArray(3)

    val halfsize: Float = CubeSise / 1.5f

    var Subfaces: IntArray = kotlin.IntArray(9)

    init {
        if (id == FRONT) //front
        {
            P1[0] = -halfsize
            P1[1] = -halfsize
            P1[2] = halfsize

            P2[0] = halfsize
            P2[1] = -halfsize
            P2[2] = halfsize

            P3[0] = halfsize
            P3[1] = halfsize
            P3[2] = halfsize

            P4[0] = -halfsize
            P4[1] = halfsize
            P4[2] = halfsize

        } else if (id == BACK) //back
        {
            P1[0] = halfsize
            P1[1] = -halfsize
            P1[2] = -halfsize

            P2[0] = -halfsize
            P2[1] = -halfsize
            P2[2] = -halfsize

            P3[0] = -halfsize
            P3[1] = halfsize
            P3[2] = -halfsize

            P4[0] = halfsize
            P4[1] = halfsize
            P4[2] = -halfsize
        } else if (id == LEFT) //left
        {
            P1[0] = -halfsize
            P1[1] = -halfsize
            P1[2] = -halfsize

            P2[0] = -halfsize
            P2[1] = -halfsize
            P2[2] = halfsize

            P3[0] = -halfsize
            P3[1] = halfsize
            P3[2] = halfsize

            P4[0] = -halfsize
            P4[1] = halfsize
            P4[2] = -halfsize
        } else if (id == RIGHT) //right
        {
            P1[0] = halfsize
            P1[1] = -halfsize
            P1[2] = halfsize

            P2[0] = halfsize
            P2[1] = -halfsize
            P2[2] = -halfsize

            P3[0] = halfsize
            P3[1] = halfsize
            P3[2] = -halfsize

            P4[0] = halfsize
            P4[1] = halfsize
            P4[2] = halfsize
        } else if (id == TOP) //top
        {
            P1[0] = -halfsize
            P1[1] = halfsize
            P1[2] = halfsize

            P2[0] = halfsize
            P2[1] = halfsize
            P2[2] = halfsize

            P3[0] = halfsize
            P3[1] = halfsize
            P3[2] = -halfsize

            P4[0] = -halfsize
            P4[1] = halfsize
            P4[2] = -halfsize
        } else if (id == BOTTOM) //bottom
        {
            P1[0] = -halfsize
            P1[1] = -halfsize
            P1[2] = -halfsize

            P2[0] = halfsize
            P2[1] = -halfsize
            P2[2] = -halfsize

            P3[0] = halfsize
            P3[1] = -halfsize
            P3[2] = halfsize

            P4[0] = -halfsize
            P4[1] = -halfsize
            P4[2] = halfsize
        }

        for (it in 0..9) {
            Subfaces[it] = id
        }

    }

    fun isSameColor(): Boolean {
        return (0..8).none { it -> Subfaces[it] != Subfaces[it + 1] }
    }


}