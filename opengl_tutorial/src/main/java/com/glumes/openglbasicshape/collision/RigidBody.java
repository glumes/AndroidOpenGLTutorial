package com.glumes.openglbasicshape.collision;

import com.glumes.openglbasicshape.base.LogUtil;
import com.glumes.openglbasicshape.utils.LoadUtil;
import com.glumes.openglbasicshape.utils.MatrixState;

import java.util.ArrayList;

/**
 * Created by glumes on 24/07/2018
 */
public class RigidBody {
    CollisionObject renderObject;//渲染者
    AABBBox collObject;//碰撞者
    boolean isStatic;//是否静止的标志位
    Vector3f currLocation;//位置三维变量
    Vector3f currV;//速度三维变量
    final float V_UNIT = 0.02f;//阈值

    public RigidBody(CollisionObject renderObject, boolean isStatic, Vector3f currLocation, Vector3f currV) {
        this.renderObject = renderObject;
        collObject = new AABBBox(renderObject.vertices);
        this.isStatic = isStatic;
        this.currLocation = currLocation;
        this.currV = currV;
    }

    public void drawSelf() {
        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(currLocation.x, currLocation.y, currLocation.z);
        renderObject.drawSelf();//绘制物体
        MatrixState.popMatrix();//恢复现场
    }

    public void go(ArrayList<RigidBody> al) {
        if (isStatic) return;
        currLocation.add(currV);
        for (int i = 0; i < al.size(); i++) {

            RigidBody rb = al.get(i);
            if (rb != this) {
                if (check(this, rb))//检验碰撞
                {
                    this.currV.x = -this.currV.x;//哪个方向的有速度，该方向上的速度置反
                }
            }
        }
    }

    public boolean check(RigidBody ra, RigidBody rb)//true为撞上
    {
        float[] over = calOverTotal
                (
                        ra.collObject.getCurrAABBBox(ra.currLocation),
                        rb.collObject.getCurrAABBBox(rb.currLocation)
                );
        return over[0] > V_UNIT && over[1] > V_UNIT && over[2] > V_UNIT;
    }

    public float[] calOverTotal(AABBBox a, AABBBox b) {
        float xOver = calOverOne(a.maxX, a.minX, b.maxX, b.minX);
        float yOver = calOverOne(a.maxY, a.minY, b.maxY, b.minY);
        float zOver = calOverOne(a.maxZ, a.minZ, b.maxZ, b.minZ);
        return new float[]{xOver, yOver, zOver};
    }

    public float calOverOne(float amax, float amin, float bmax, float bmin) {
        float minMax = 0;
        float maxMin = 0;
        if (amax < bmax)//a物体在b物体左侧
        {
            minMax = amax;
            maxMin = bmin;
        } else //a物体在b物体右侧
        {
            minMax = bmax;
            maxMin = amin;
        }

        if (minMax > maxMin) {
            return minMax - maxMin;
        } else {
            return 0;
        }
    }
}
