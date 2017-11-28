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
import android.opengl.GLUtils;
import android.os.Build;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import lib.IBufferFactory;

public class GLFace {
    private ShortBuffer indicesBuffer;
    private FloatBuffer textureBuffer;
	private ArrayList<GLVertex> mVertexList = new ArrayList<GLVertex>();
	private GLColor mColor;
	
	// The bitmap we want to load as a texture. 
	private int mTextureId = -1; 
	private Bitmap mBitmap;
	private boolean bInitTexture = false;
	
	// for quadrilaterals
	public GLFace(GLVertex v1, GLVertex v2, GLVertex v3, GLVertex v4) {
		addVertex(v1);
		addVertex(v2);
		addVertex(v3);
		addVertex(v4);
	}
	
	public void setIndices(short[] indices){
		indicesBuffer = IBufferFactory.newShortBuffer(indices);
	}
	
	public void setTextureCoordinates(float[] textureCoords){
		textureBuffer = IBufferFactory.newFloatBuffer(textureCoords);
	}

	public ShortBuffer getIndicesBuffer(){
		return indicesBuffer;
	}
	
	public FloatBuffer getTextureBuffer(){
		return textureBuffer;
	}
	
	public void addVertex(GLVertex v) {
		mVertexList.add(v);
	}
	
	// must be called after all vertices are added
	public void setColor(GLColor c) {
		mColor = c;
	}
	
	public GLColor getColor(){
		return mColor;
	}
	
	public int getIndexCount() {
		//根据putIndices只画两个三角型
		return (mVertexList.size() - 2) * 3;
	}
		
	public GLVertex getVertex(int index){
		return mVertexList.get(index);
	}
			
	/** 
	 * Set the bitmap to load into a texture. 
	 * 
	 * @param bitmap 
	 */
	public void loadBitmap(Bitmap bitmap) {
		 this.mBitmap = bitmap; 
		 bInitTexture = true; 
	} 
	  
	/** 
	 * Loads the texture. 
	 * 
	 * @param gl 
	 */
	private void loadGLTexture(GL10 gl) {
		 // Generate one texture pointer... 
		 int[] textures = new int[1]; 
		 gl.glGenTextures(1, textures, 0); 
		 mTextureId = textures[0]; 
		  
		 // ...and bind it to our array 
		 gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
		  
		 // Create Nearest Filtered Texture 
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		  
		 // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE 
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		  
		 //纹理贴图和材质混合的方式
	     gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
	        
		 // Use the Android GLUtils to specify a two-dimensional texture image 
		 // from our bitmap 
		 GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
	}
	
	public void draw(GL10 gl){
        //增加贴图
		if (bInitTexture) { 
			 loadGLTexture(gl); 
			 bInitTexture = false;
		}
		
		GLColor color = getColor();


		gl.glColor4f(color.red, color.green, color.blue, color.alpha);
		
		if (!color.equals(GLColor.BLACK)) { 
			 gl.glEnable(GL10.GL_TEXTURE_2D);
			 // Enable the texture state 
			 gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			// Point to our buffers 
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
			  			 
			FloatBuffer textureBuffer = getTextureBuffer();
			textureBuffer.position(0);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		}
		 
		ShortBuffer indicesBuffer = getIndicesBuffer();
		indicesBuffer.position(0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        
        if (!color.equals(GLColor.BLACK)) { 
		   	 gl.glDisable(GL10.GL_TEXTURE_2D);
		   	 gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
	}
}
