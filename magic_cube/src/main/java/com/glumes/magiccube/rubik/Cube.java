/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glumes.magiccube.rubik;


import lib.Vector3f;

public class Cube extends GLShape {
	private float sphereRadius;
	
	public Cube(GLWorld world, float left, float bottom, float back, float right, float top, float front) {
		super(world);

		sphereRadius = 0.6f;
		
		/* 
		 *   2 3
		 *  6 7
		 *   0 1
		 *  4 5
		 * */
       	GLVertex leftBottomBack = addVertex(left, bottom, back);
        GLVertex rightBottomBack = addVertex(right, bottom, back);
       	
        GLVertex leftTopBack = addVertex(left, top, back);
        GLVertex rightTopBack = addVertex(right, top, back);
       	
        GLVertex leftBottomFront = addVertex(left, bottom, front);
        GLVertex rightBottomFront = addVertex(right, bottom, front);
       	
        GLVertex leftTopFront = addVertex(left, top, front);
        GLVertex rightTopFront = addVertex(right, top, front);

        // vertices are added in a clockwise orientation (when viewed from the outside)
        GLFace bottomFace = new GLFace(leftBottomBack, leftBottomFront, rightBottomFront, rightBottomBack);
        bottomFace.setColor(GLColor.ORANGE);
        bottomFace.setIndices(new short[] { 4, 0, 5, 1, });
        bottomFace.setTextureCoordinates(new float[] { 0,1 , 1,1, 0,0, 0,0, 0,0, 1,0, 0,0, 0,0});
        addFace(bottomFace);

        //1 2
        //0 3
        GLFace frontFace = new GLFace(leftBottomFront, leftTopFront, rightTopFront, rightBottomFront);
        frontFace.setColor(GLColor.RED);
        frontFace.setIndices(new short[] { 4, 5, 6, 7, });
        frontFace.setTextureCoordinates(new float[] { 0,0 , 0,0, 0,0, 0,0, 0,1 ,1,1, 0,0, 1,0});
        addFace(frontFace);

        GLFace leftFace = new GLFace(leftBottomBack, leftTopBack, leftTopFront, leftBottomFront);
        leftFace.setColor(GLColor.YELLOW);
        leftFace.setIndices(new short[] { 4, 6, 0, 2, });
        leftFace.setTextureCoordinates(new float[]{ 0,1 , 0,0, 0,0, 0,0, 1,1, 0,0, 1,0, 0,0});       
        addFace(leftFace);

        GLFace rightFace = new GLFace(rightBottomBack, rightBottomFront, rightTopFront, rightTopBack);
        rightFace.setColor(GLColor.WHITE);
        rightFace.setIndices(new short[] { 1, 3, 5, 7, });
        rightFace.setTextureCoordinates(new float[] { 0,0 , 1,1, 0,0, 1,0, 0,0, 0,1, 0,0, 0,0});         
        addFace(rightFace);

        GLFace backFace = new GLFace(leftBottomBack, rightBottomBack, rightTopBack, leftTopBack);
        backFace.setColor(GLColor.BLUE);
        backFace.setIndices(new short[] { 0, 2, 1, 3, });
        backFace.setTextureCoordinates(new float[] { 1,1 , 0,1, 1,0, 0,0, 0,0, 0,0, 0,0, 0,0});  
        addFace(backFace);
        
        GLFace topFace = new GLFace(leftTopBack, rightTopBack, rightTopFront, leftTopFront);
        topFace.setColor(GLColor.GREEN);
        topFace.setIndices(new short[] { 6, 7, 2, 3, });
        topFace.setTextureCoordinates(new float[] { 0,0 , 0,0, 1,1, 0,1, 0,0, 0,0, 1,0, 0,0});  
        addFace(topFace);
	}
	
	public static final int kBottom = 0;
    public static final int kFront = 1;
    public static final int kLeft = 2;
    public static final int kRight = 3;
    public static final int kBack = 4;
    public static final int kTop = 5;
    
    /**
	 * @return {minX,maxX,minY,maxY,minZ,maxZ}
	 */
	public float[] getMinMax(){
		float[] arr = new float[6];
		
		boolean init = true;
		
		for(GLVertex vertex : mVertexList){
			if (init) {
				arr[0] = vertex.tempX;
				arr[1] = vertex.tempX;
				arr[2] = vertex.tempY;
				arr[3] = vertex.tempY;
				arr[4] = vertex.tempZ;
				arr[5] = vertex.tempZ;
				
				init = false;
			}
			else{
				arr[0] = arr[0]>vertex.tempX ? vertex.tempX: arr[0];
				arr[1] = arr[1]<vertex.tempX ? vertex.tempX: arr[1];
				arr[2] = arr[2]>vertex.tempY ? vertex.tempY: arr[2];
				arr[3] = arr[3]<vertex.tempY ? vertex.tempY: arr[3];
				arr[4] = arr[4]>vertex.tempZ ? vertex.tempZ: arr[4];
				arr[5] = arr[5]<vertex.tempZ ? vertex.tempZ: arr[5];
			}
		}
		
		return arr;
	}
	
	public Vector3f getSphereCenter() {
		// TODO Auto-generated method stub
		float[] arr = getMinMax();
		
		return new Vector3f((arr[0]+arr[1])/2,(arr[2]+arr[3])/2,(arr[4]+arr[5])/2);
	}

	public float getSphereRadius() {
		// TODO Auto-generated method stub
		return sphereRadius;
	}	
}
