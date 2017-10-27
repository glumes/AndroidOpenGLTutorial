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

import java.nio.FloatBuffer;

public class GLVertex {

    public float x,tempX;
    public float y,tempY;
    public float z,tempZ;
    
    final short index; // index in vertex table
    GLColor color;

    GLVertex() {
        this.tempX = this.x = 0;
        this.tempY = this.y = 0;
        this.tempZ = this.z = 0;
        this.index = -1;
    }

    GLVertex(float x, float y, float z, int index) {
    	this.tempX = this.x = x;
    	this.tempY = this.y = y;
    	this.tempZ = this.z = z;
        this.index = (short)index;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GLVertex) {
            GLVertex v = (GLVertex)other;
            return (x == v.x && y == v.y && z == v.z);
        }
        return false;
    }

    static public int toFixed(float x) {
    	//如果采用GL10.GL_FIXED则需要修正，默认为GL_FLOAT则不需要
        return (int)(x * 65536.0f);
    }

    public void put(FloatBuffer vertexBuffer) {
        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(z);
    }

    public void update(FloatBuffer vertexBuffer, M4 transform) {
        // skip to location of vertex in mVertex buffer
        vertexBuffer.position(index * 3);

        if (transform == null) {
            this.tempX = x;
            this.tempY = y;
            this.tempZ = z;
            
            vertexBuffer.put(x);
            vertexBuffer.put(y);
            vertexBuffer.put(z);
        } else {
            GLVertex temp = new GLVertex();
            transform.multiply(this, temp);
            
            this.tempX = temp.x;
            this.tempY = temp.y;
            this.tempZ = temp.z;
            
            vertexBuffer.put(temp.x);
            vertexBuffer.put(temp.y);
            vertexBuffer.put(temp.z);
        }
    }
}