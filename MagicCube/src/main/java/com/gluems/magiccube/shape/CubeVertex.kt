package com.gluems.magiccube.shape

/**
 * @Author  glumes
 */
class CubeVertex() {


    var x = 0f
    var y = 0f
    var z = 0f
    var tempX = 0f
    var tempY = 0f
    var tempZ = 0f
    var index = -1


    constructor(x: Float, y: Float, z: Float, i: Int) : this() {

        this.tempX = x
        this.x = x

        this.tempY = y
        this.y = y

        this.tempZ = z
        this.z = z

        this.index = i
    }


}