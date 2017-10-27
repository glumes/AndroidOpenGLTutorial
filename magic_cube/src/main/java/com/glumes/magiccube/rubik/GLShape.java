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

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class GLShape {
	public static int COUNT = 0;
	public String id = "";
	public M4						mTransform;
	public M4						mAnimateTransform;
	protected ArrayList<GLFace> mFaceList = new ArrayList<GLFace>();
	protected ArrayList<GLVertex> mVertexList = new ArrayList<GLVertex>();
//	protected ArrayList<Integer>	mIndexList = new ArrayList<Integer>();	// make more efficient?
	protected GLWorld mWorld;
	
    private FloatBuffer mVertexBuffer;
//    private IntBuffer   mColorBuffer;
//    private ShortBuffer mIndexBuffer;
//    ShortBuffer[] indicesFace = new ShortBuffer[6];
//    FloatBuffer[] mTextureBufferFace = new FloatBuffer[6];
//    private FloatBuffer mTextureBuffer;
    
	public GLShape(GLWorld world) {
		mWorld = world;
		
		synchronized (GLShape.class) {
			//缺中心一个点
			if (COUNT==13) {
				COUNT++;
			}
			
			id = String.valueOf(COUNT);
			COUNT++;
		}
	}
	
	public void addFace(GLFace face) {
		mFaceList.add(face);
	}
	
	public void setFaceColor(int face, GLColor color) {
		mFaceList.get(face).setColor(color);
	}
	
	public void putVertex(FloatBuffer buffer) {
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			buffer.put(vertex.x);
			buffer.put(vertex.y);
			buffer.put(vertex.z);
		}		
	}
	
	public GLVertex addVertex(float x, float y, float z) {
		
		// look for an existing GLVertex first
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			if (vertex.x == x && vertex.y == y && vertex.z == z) {
				return vertex;
			}
		}
		
		// doesn't exist, so create new vertex
		GLVertex vertex = new GLVertex(x, y, z,mVertexList.size());
		mVertexList.add(vertex);
		
		return vertex;
	}

	public void animateTransform(M4 transform) {
		mAnimateTransform = transform;
		
		if (mTransform != null)
			transform = mTransform.multiply(transform);

		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			//mWorld.transformVertex(vertex, transform);
			vertex.update(mVertexBuffer, transform);
		}
	}
	
	public void startAnimation() {
		Log.d("GLWORLD","startAnimation empty");
	}

	public void endAnimation() {
		//旋转角度累计
		if (mTransform == null) {
			mTransform = new M4(mAnimateTransform);
		} else {
			mTransform = mTransform.multiply(mAnimateTransform);
		}
	}
	
	public void loadBitmap(Bitmap bitmap) {
		for (GLFace face : mFaceList) {
			face.loadBitmap(bitmap);
		}
	} 

	public void draw(GL10 gl) {
		// TODO Auto-generated method stub
		mVertexBuffer.position(0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		
		for (GLFace face : mFaceList) {
			face.draw(gl);
		}
	}

	public void generate() {
		// TODO Auto-generated method stub
		//顶点颜色
	    ByteBuffer bb = ByteBuffer.allocateDirect(mVertexList.size()*4*4);

		//顶点坐标
	    bb = ByteBuffer.allocateDirect(mVertexList.size()*3*4);
	    bb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = bb.asFloatBuffer();

		Iterator<GLVertex> iter2 = mVertexList.iterator();
		
		while (iter2.hasNext()) {
			GLVertex vertex = iter2.next();
			vertex.put(mVertexBuffer);
		}
	}
}
