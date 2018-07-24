package com.glumes.openglbasicshape.collision;

/**
 * Created by glumes on 24/07/2018
 */
public class AABBBox {
    float minX;//x轴最小位置
    float maxX;//x轴最大位置
    float minY;//y轴最小位置
    float maxY;//y轴最大位置
    float minZ;//z轴最小位置
    float maxZ;//z轴最大位置

    public AABBBox(float[] vertices)
    {
        init();
        findMinAndMax(vertices);
    }

    public AABBBox(float minX,float maxX,float minY,float maxY,float minZ,float maxZ)
    {
        this.minX=minX;
        this.maxX=maxX;
        this.minY=minY;
        this.maxY=maxY;
        this.minZ=minZ;
        this.maxZ=maxZ;
    }
    //初始化包围盒的最小以及最大顶点坐标
    public void init()
    {
        minX=Float.POSITIVE_INFINITY;
        maxX=Float.NEGATIVE_INFINITY;
        minY=Float.POSITIVE_INFINITY;
        maxY=Float.NEGATIVE_INFINITY;
        minZ=Float.POSITIVE_INFINITY;
        maxZ=Float.NEGATIVE_INFINITY;
    }
    //获取包围盒的实际最小以及最大顶点坐标
    public void findMinAndMax(float[] vertices)
    {
        for(int i=0;i<vertices.length/3;i++)
        {
            //判断X轴的最小和最大位置
            if(vertices[i*3]<minX)
            {
                minX=vertices[i*3];
            }
            if(vertices[i*3]>maxX)
            {
                maxX=vertices[i*3];
            }
            //判断Y轴的最小和最大位置
            if(vertices[i*3+1]<minY)
            {
                minY=vertices[i*3+1];
            }
            if(vertices[i*3+1]>maxY)
            {
                maxY=vertices[i*3+1];
            }
            //判断Z轴的最小和最大位置
            if(vertices[i*3+2]<minZ)
            {
                minZ=vertices[i*3+2];
            }
            if(vertices[i*3+2]>maxZ)
            {
                maxZ=vertices[i*3+2];
            }
        }
    }
    //获得物体平移后的AABB包围盒
    public AABBBox getCurrAABBBox(Vector3f currPosition)
    {
        AABBBox result=new AABBBox
                (
                        this.minX+currPosition.x,
                        this.maxX+currPosition.x,
                        this.minY+currPosition.y,
                        this.maxY+currPosition.y,
                        this.minZ+currPosition.z,
                        this.maxZ+currPosition.z
                );
        return result;
    }
}
