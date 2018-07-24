package com.glumes.openglbasicshape.collision;

/**
 * Created by glumes on 24/07/2018
 */
public class Vector3f {
    float x;//三维变量中的x值
    float y;//三维变量中的y值
    float z;//三维变量中的z值

    public Vector3f(float x,float y,float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public void add(Vector3f temp)
    {
        this.x+=temp.x;
        this.y+=temp.y;
        this.z+=temp.z;
    }
}
