/*
 * Copyright 2011-2014 Zhaotian Wang <zhaotianzju@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glumes.openglbasicshape.magiccube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Cube {

	// public variables
	public static final int XAxis = 0;
	public static final int YAxis = 1;
	public static final int ZAxis = 2;

	public static final int ClockWise = 0;
	public static final int CounterClockWise = 1;

	// private variables
	private int ID;
	private int[] textureIDs;
	public static final float CubeSize = 2.f;

	private float xp, yp, zp; // xyz position

	private FloatBuffer vertexBuffer; // Vertex Buffer
	private FloatBuffer texBuffer; // Texture Coords Buffer
	private FloatBuffer normBuffer; // Normal Buffer
	private FloatBuffer mat_specular; // specular material
	private FloatBuffer mat_diffuse; // specular material

	private float[] modelM; // Rotate Matrix

	// public methods
	public Cube(int i, GL10 gl, Context context) {
		// initial the index
		this.ID = i;

		// Initial buffer
		this.InitialBuffer();

		// LoadTexture
		this.LoadTexture(gl, context);

		// SetPosition
		this.SetPosition(ID);

		// Initial matrix
		modelM = new float[16];
		Matrix.setIdentityM(modelM, 0);
	}

	public Cube(int i) {
		// initial the index
		this.ID = i;

		// Initial buffer
		this.InitialBuffer();

		// SetPosition
		this.SetPosition(ID);

		// Initial matrix
		modelM = new float[16];
		Matrix.setIdentityM(modelM, 0);
	}

	public void Reset() {
		Matrix.setIdentityM(modelM, 0);
	}

	public void Rotate(int axis, float angle) {
		float[] m = new float[16];
		float[] m2 = new float[16];
		Matrix.setIdentityM(m, 0);

		if (axis == Cube.XAxis) {
			Matrix.rotateM(m, 0, angle, 1.f, 0.f, 0.f);
			Matrix.multiplyMM(m2, 0, m, 0, modelM, 0);
		}

		if (axis == Cube.YAxis) {
			Matrix.rotateM(m, 0, angle, 0.f, 1.f, 0.f);
			Matrix.multiplyMM(m2, 0, m, 0, modelM, 0);
		}

		if (axis == Cube.ZAxis) {
			Matrix.rotateM(m, 0, angle, 0.f, 0.f, 1.f);
			Matrix.multiplyMM(m2, 0, m, 0, modelM, 0);
		}
		for (int i = 0; i < 16; i++) {
			modelM[i] = m2[i];
		}
	}

	// private methods

	public void LoadTexture(GL10 gl, Context context) {
		int[] imageFileIDs = { // Image file IDs
		R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
				R.drawable.f, R.drawable.g };

		Bitmap[] bitmap = new Bitmap[7];

		textureIDs = new int[7];
		gl.glGenTextures(6, textureIDs, 0); // Generate texture-ID array for 6
											// IDs

		// Generate OpenGL texture images
		for (int face = 0; face < 7; face++) {
			bitmap[face] = BitmapFactory.decodeStream(context.getResources()
					.openRawResource(imageFileIDs[face]));
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_MODULATE);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR);
			// Build Texture from loaded bitmap for the currently-bind texture
			// ID
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[face], 0);
			bitmap[face].recycle();
		}
	}

	private void SetPosition(int index) {

		// y-axis
		if (index < 9 && index >= 0) {
			yp = 1.f;
		} else if (index >= 9 && index < 18) {
			yp = 0.f;
		} else if (index < 27) {
			yp = -1.f;
		}
		// z-axis
		if (index % 9 >= 0 && index % 9 < 3) {
			zp = 1.f;
		} else if (index % 9 >= 3 && index % 9 < 6) {
			zp = 0.f;
		} else if (index % 9 >= 6 && index % 9 < 9) {
			zp = -1.f;
		}
		// x-axis
		if (index % 9 % 3 == 0) {
			xp = -1.f;
		} else if (index % 9 % 3 == 1) {
			xp = 0.f;
		} else if (index % 9 % 3 == 2) {
			xp = 1.f;
		}

		xp *= CubeSize;
		yp *= CubeSize;
		zp *= CubeSize;

	}

	private void InitialBuffer() {

		ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * 6);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();

		ByteBuffer sbb = ByteBuffer.allocateDirect(4 * 4);
		sbb.order(ByteOrder.nativeOrder());
		mat_specular = sbb.asFloatBuffer();
		float[] specular = { 0.5f, 0.5f, 1.f, 1.f };
		mat_specular.put(specular);
		mat_specular.position(0);

		ByteBuffer dbb = ByteBuffer.allocateDirect(4 * 4);
		dbb.order(ByteOrder.nativeOrder());
		mat_diffuse = dbb.asFloatBuffer();
		float[] diffuse = { 0.2f, 0.2f, 1.f, 1.f };
		mat_diffuse.put(diffuse);
		mat_diffuse.position(0);

		ByteBuffer nbb = ByteBuffer.allocateDirect(12 * 4 * 6);
		nbb.order(ByteOrder.nativeOrder());
		normBuffer = nbb.asFloatBuffer();
		// Read images. Find the aspect ratio and adjust the vertices
		// accordingly.
		for (int face = 0; face < 6; face++) {
			// Adjust for aspect ratio
			float faceLeft = -CubeSize / 2;
			float faceRight = -faceLeft;
			float faceTop = CubeSize / 2;
			float faceBottom = -faceTop;

			// Define the vertices for this face
			float[] vertices = { faceLeft, faceBottom, 0.0f, // 0.
																// left-bottom-front
					faceRight, faceBottom, 0.0f, // 1. right-bottom-front
					faceLeft, faceTop, 0.0f, // 2. left-top-front
					faceRight, faceTop, 0.0f, // 3. right-top-front
			};

			float[] norm = { 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 0.f,
					0.f, 1.f, };
			vertexBuffer.put(vertices); // Populate
			normBuffer.put(norm);
		}
		vertexBuffer.position(0); // Rewind
		normBuffer.position(0); // Rewind

		// Allocate texture buffer. An float has 4 bytes. Repeat for 6 faces.
		float[] texCoords = { 0.0f, 1.0f, // A. left-bottom
				1.0f, 1.0f, // B. right-bottom
				0.0f, 0.0f, // C. left-top
				1.0f, 0.0f // D. right-top
		};
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * 6);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		for (int face = 0; face < 6; face++) {
			texBuffer.put(texCoords);
		}
		texBuffer.position(0); // Rewind
	}

	public void DrawSimple(GL10 gl) {
		if (ID == 13) // avoid the center cube
			return;

		// if( ID == 4 || ID == 10 || ID == 12 || ID == 14 || ID == 16 || ID ==
		// 22)
		// return;
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise
										// orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK); // Cull the back face (don't display)

		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, mat_specular);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, mat_diffuse);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, mat_diffuse);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable texture (NEW)
		gl.glPushMatrix();

		gl.glMultMatrixf(modelM, 0);
		gl.glTranslatef(xp, yp, zp);
		float cubeHalfSize = CubeSize / 2.f;
		// left
		if (ID % 9 == 0 || ID % 9 == 3 || ID % 9 == 6) {
			gl.glPushMatrix();
			gl.glRotatef(270.0f, 0.f, 1.f, 0.f);
			gl.glTranslatef(0f, 0f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
			gl.glPopMatrix();
		}

		if (ID % 9 == 0 || ID % 9 == 1 || ID % 9 == 2) {
			// front
			gl.glPushMatrix();
			gl.glTranslatef(0.f, 0.f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		}

		// back
		if (ID % 9 == 6 || ID % 9 == 7 || ID % 9 == 8) {
			gl.glPushMatrix();
			gl.glRotatef(180.0f, 0f, 1f, 0f);
			gl.glTranslatef(0f, 0f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
			gl.glPopMatrix();
		}

		if (ID % 9 == 2 || ID % 9 == 5 || ID % 9 == 8) {
			// right
			gl.glPushMatrix();
			gl.glRotatef(90.0f, 0f, 1f, 0f);
			gl.glTranslatef(0f, 0f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[3]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
			gl.glPopMatrix();
		}

		if (ID >= 0 && ID <= 8) {
			// top
			gl.glPushMatrix();
			gl.glRotatef(270.0f, 1f, 0f, 0f);
			gl.glTranslatef(0f, 0f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[4]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
			gl.glPopMatrix();
		}

		if (ID >= 18 && ID <= 26) {
			// bottom
			gl.glPushMatrix();
			gl.glRotatef(90.0f, 1f, 0f, 0f);
			gl.glTranslatef(0f, 0f, cubeHalfSize);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[5]);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
			gl.glPopMatrix();
		}

		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	public void Draw(GL10 gl) {
		if (ID == 13) // avoid the center cube
			return;

		// if( ID == 4 || ID == 10 || ID == 12 || ID == 14 || ID == 16 || ID ==
		// 22)
		// return;
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise
										// orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK); // Cull the back face (don't display)

		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, mat_specular);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, mat_diffuse);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, mat_diffuse);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable texture (NEW)
		gl.glPushMatrix();

		gl.glMultMatrixf(modelM, 0);
		gl.glTranslatef(xp, yp, zp);
		float cubeHalfSize = CubeSize / 2.f;
		// left

		gl.glPushMatrix();
		gl.glRotatef(270.0f, 0.f, 1.f, 0.f);
		gl.glTranslatef(0f, 0f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		gl.glPopMatrix();

		// front
		gl.glPushMatrix();
		gl.glTranslatef(0.f, 0.f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		// back

		gl.glPushMatrix();
		gl.glRotatef(180.0f, 0f, 1f, 0f);
		gl.glTranslatef(0f, 0f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glPopMatrix();

		// right

		gl.glPushMatrix();
		gl.glRotatef(90.0f, 0f, 1f, 0f);
		gl.glTranslatef(0f, 0f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[3]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		gl.glPopMatrix();

		// top
		gl.glPushMatrix();
		gl.glRotatef(270.0f, 1f, 0f, 0f);
		gl.glTranslatef(0f, 0f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[4]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glPopMatrix();

		// bottom
		gl.glPushMatrix();
		gl.glRotatef(90.0f, 1f, 0f, 0f);
		gl.glTranslatef(0f, 0f, cubeHalfSize);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[5]);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
}
