package lib;

import java.nio.FloatBuffer;

public class Matrix4f {

	public static FloatBuffer gFBMatrix = IBufferFactory.newFloatBuffer(16);

	public float m00, m01, m02, m03;
	public float m10, m11, m12, m13;
	public float m20, m21, m22, m23;
	public float m30, m31, m32, m33;

	public Matrix4f() {
		this.setIdentity();
	}

	public void setIdentity() {
		this.m00 = 1.0f;
		this.m01 = 0.0f;
		this.m02 = 0.0f;
		this.m03 = 0.0f;

		this.m10 = 0.0f;
		this.m11 = 1.0f;
		this.m12 = 0.0f;
		this.m13 = 0.0f;

		this.m20 = 0.0f;
		this.m21 = 0.0f;
		this.m22 = 1.0f;
		this.m23 = 0.0f;

		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	public final void setTranslation(Vector3f trans) {
		m03 = trans.x;
		m13 = trans.y;
		m23 = trans.z;
	}

	public final void setTranslation(float x, float y, float z) {
		m03 = x;
		m13 = y;
		m23 = z;
	}

	/**
	 * 绕X轴旋转矩阵
	 */
	public final void rotX(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = 1;
		this.m01 = 0;
		this.m02 = 0;
		this.m03 = 0;

		this.m10 = 0;
		this.m11 = cosAngle;
		this.m12 = -sinAngle;
		this.m13 = 0;

		this.m20 = 0;
		this.m21 = sinAngle;
		this.m22 = cosAngle;
		this.m23 = 0;

		this.m30 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 1;
	}

	/**
	 * 绕Y轴旋转矩阵
	 */
	public final void rotY(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = cosAngle;
		this.m01 = 0;
		this.m02 = sinAngle;
		this.m03 = 0;

		this.m10 = 0;
		this.m11 = 1;
		this.m12 = 0;
		this.m13 = 0;

		this.m20 = -sinAngle;
		this.m21 = 0;
		this.m22 = cosAngle;
		this.m23 = 0;

		this.m30 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 1;
	}

	/**
	 * 绕Z轴旋转矩阵
	 */
	public final void rotZ(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = cosAngle;
		this.m01 = -sinAngle;
		this.m02 = 0;
		this.m03 = 0;

		this.m10 = sinAngle;
		this.m11 = cosAngle;
		this.m12 = 0;
		this.m13 = 0;

		this.m20 = 0;
		this.m21 = 0;
		this.m22 = 1;
		this.m23 = 0;

		this.m30 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 1;
	}
	
	public final void scale(float x,float y,float z){
		this.m00 = x;
		this.m01 = 0;
		this.m02 = 0;
		this.m03 = 0;

		this.m10 = 0;
		this.m11 = y;
		this.m12 = 0;
		this.m13 = 0;

		this.m20 = 0;
		this.m21 = 0;
		this.m22 = z;
		this.m23 = 0;

		this.m30 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 1;		
	}

	/**
	 * 绕任意单位轴旋转任意角度的矩阵
	 */
	public final void glRotatef(float angle, float x, float y, float z) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = cosAngle + (1 - cosAngle) * x * x;
		this.m01 = (1 - cosAngle) * x * y - sinAngle * z;
		this.m02 = (1 - cosAngle) * x * z + sinAngle * y;
		this.m03 = 0;

		this.m10 = (1 - cosAngle) * x * y + sinAngle * z;
		this.m11 = cosAngle + (1 - cosAngle) * y * y;
		this.m12 = (1 - cosAngle) * y * z - sinAngle * x;
		this.m13 = 0;

		this.m20 = (1 - cosAngle) * x * z - sinAngle * y;
		this.m21 = (1 - cosAngle) * y * z + sinAngle * x;
		this.m22 = cosAngle + (1 - cosAngle) * z * z;
		this.m23 = 0;

		this.m30 = 0;
		this.m31 = 0;
		this.m32 = 0;
		this.m33 = 1;
	}

	/*
	 * 统一缩放矩阵
	 * 
	 * s 0 0 0
	 * 
	 * 0 s 0 0
	 * 
	 * 0 0 s 0
	 * 
	 * 0 0 0 1
	 */

	/*
	 * 平移矩阵
	 * 
	 * 1 0 0 0
	 * 
	 * 0 1 0 0
	 * 
	 * 0 0 1 0
	 * 
	 * dx dy dz 1
	 */

	public final void set(Matrix4f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;

		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	public final void mul(Matrix4f m1) {
		// vars
		float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

		m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20
				+ this.m03 * m1.m30;
		m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21
				+ this.m03 * m1.m31;
		m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22
				+ this.m03 * m1.m32;
		m03 = this.m00 * m1.m03 + this.m01 * m1.m13 + this.m02 * m1.m23
				+ this.m03 * m1.m33;

		m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20
				+ this.m13 * m1.m30;
		m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21
				+ this.m13 * m1.m31;
		m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22
				+ this.m13 * m1.m32;
		m13 = this.m10 * m1.m03 + this.m11 * m1.m13 + this.m12 * m1.m23
				+ this.m13 * m1.m33;

		m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20
				+ this.m23 * m1.m30;
		m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21
				+ this.m23 * m1.m31;
		m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22
				+ this.m23 * m1.m32;
		m23 = this.m20 * m1.m03 + this.m21 * m1.m13 + this.m22 * m1.m23
				+ this.m23 * m1.m33;

		m30 = this.m30 * m1.m00 + this.m31 * m1.m10 + this.m32 * m1.m20
				+ this.m33 * m1.m30;
		m31 = this.m30 * m1.m01 + this.m31 * m1.m11 + this.m32 * m1.m21
				+ this.m33 * m1.m31;
		m32 = this.m30 * m1.m02 + this.m31 * m1.m12 + this.m32 * m1.m22
				+ this.m33 * m1.m32;
		m33 = this.m30 * m1.m03 + this.m31 * m1.m13 + this.m32 * m1.m23
				+ this.m33 * m1.m33;

		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public final void mul(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {

			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20
					+ m1.m03 * m2.m30;
			this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21
					+ m1.m03 * m2.m31;
			this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22
					+ m1.m03 * m2.m32;
			this.m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23
					+ m1.m03 * m2.m33;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20
					+ m1.m13 * m2.m30;
			this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21
					+ m1.m13 * m2.m31;
			this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22
					+ m1.m13 * m2.m32;
			this.m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23
					+ m1.m13 * m2.m33;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20
					+ m1.m23 * m2.m30;
			this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21
					+ m1.m23 * m2.m31;
			this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22
					+ m1.m23 * m2.m32;
			this.m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23
					+ m1.m23 * m2.m33;

			this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20
					+ m1.m33 * m2.m30;
			this.m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21
					+ m1.m33 * m2.m31;
			this.m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22
					+ m1.m33 * m2.m32;
			this.m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23
					+ m1.m33 * m2.m33;
		} else {
			// vars result matrix
			float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

			m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03
					* m2.m30;
			m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03
					* m2.m31;
			m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03
					* m2.m32;
			m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03
					* m2.m33;

			m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13
					* m2.m30;
			m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13
					* m2.m31;
			m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13
					* m2.m32;
			m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13
					* m2.m33;

			m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23
					* m2.m30;
			m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23
					* m2.m31;
			m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23
					* m2.m32;
			m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23
					* m2.m33;

			m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33
					* m2.m30;
			m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33
					* m2.m31;
			m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33
					* m2.m32;
			m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33
					* m2.m33;

			this.m00 = m00;
			this.m01 = m01;
			this.m02 = m02;
			this.m03 = m03;
			this.m10 = m10;
			this.m11 = m11;
			this.m12 = m12;
			this.m13 = m13;
			this.m20 = m20;
			this.m21 = m21;
			this.m22 = m22;
			this.m23 = m23;
			this.m30 = m30;
			this.m31 = m31;
			this.m32 = m32;
			this.m33 = m33;
		}
	}

	public final void set(Quat4f q1) {
		this.m00 = (1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z);
		this.m10 = (2.0f * (q1.x * q1.y + q1.w * q1.z));
		this.m20 = (2.0f * (q1.x * q1.z - q1.w * q1.y));

		this.m01 = (2.0f * (q1.x * q1.y - q1.w * q1.z));
		this.m11 = (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z);
		this.m21 = (2.0f * (q1.y * q1.z + q1.w * q1.x));

		this.m02 = (2.0f * (q1.x * q1.z + q1.w * q1.y));
		this.m12 = (2.0f * (q1.y * q1.z - q1.w * q1.x));
		this.m22 = (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y);

		this.m03 = (float) 0.0;
		this.m13 = (float) 0.0;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	public final void invTransform(Vector3f point, Vector3f pointOut) {
		Vector3f tmp = new Vector3f();
		tmp.x = point.x - m03;
		tmp.y = point.y - m13;
		tmp.z = point.z - m23;

		// transform normal
		invTransformRotate(tmp, pointOut);
	}

	public final void invTransformRotate(Vector3f normal, Vector3f normalOut) {
		float x, y;
		x = m00 * normal.x + m10 * normal.y + m20 * normal.z;
		y = m01 * normal.x + m11 * normal.y + m21 * normal.z;
		normalOut.z = m02 * normal.x + m12 * normal.y + m22 * normal.z;
		normalOut.x = x;
		normalOut.y = y;
	}

	public final void transform(Vector3f point, Vector3f pointOut) {
		float x, y;
		x = m00 * point.x + m01 * point.y + m02 * point.z + m03;
		y = m10 * point.x + m11 * point.y + m12 * point.z + m13;
		pointOut.z = m20 * point.x + m21 * point.y + m22 * point.z + m23;
		pointOut.x = x;
		pointOut.y = y;
	}

	/**
	 * Sets the value of this matrix to its transpose in place.
	 */
	public final void transpose() {
		float temp;

		temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;

		temp = this.m20;
		this.m20 = this.m02;
		this.m02 = temp;

		temp = this.m30;
		this.m30 = this.m03;
		this.m03 = temp;

		temp = this.m21;
		this.m21 = this.m12;
		this.m12 = temp;

		temp = this.m31;
		this.m31 = this.m13;
		this.m13 = temp;

		temp = this.m32;
		this.m32 = this.m23;
		this.m23 = temp;
	}

	/**
	 * Inverts this matrix in place.
	 */
	public final void invert() {
		invertGeneral(this);
	}

	/**
	 * General invert routine. Inverts m1 and places the result in "this". Note
	 * that this routine handles both the "this" version and the non-"this"
	 * version.
	 * 
	 * Also note that since this routine is slow anyway, we won't worry about
	 * allocating a little bit of garbage.
	 */
	@SuppressWarnings("unused")
	final void invertGeneral(Matrix4f m1) {
		double temp[] = new double[16];
		double result[] = new double[16];
		int row_perm[] = new int[4];
		int i, r, c;

		// Use LU decomposition and backsubstitution code specifically
		// for floating-point 4x4 matrices.

		// Copy source matrix to t1tmp
		temp[0] = m1.m00;
		temp[1] = m1.m01;
		temp[2] = m1.m02;
		temp[3] = m1.m03;

		temp[4] = m1.m10;
		temp[5] = m1.m11;
		temp[6] = m1.m12;
		temp[7] = m1.m13;

		temp[8] = m1.m20;
		temp[9] = m1.m21;
		temp[10] = m1.m22;
		temp[11] = m1.m23;

		temp[12] = m1.m30;
		temp[13] = m1.m31;
		temp[14] = m1.m32;
		temp[15] = m1.m33;

		// Calculate LU decomposition: Is the matrix singular?
		if (!luDecomposition(temp, row_perm)) {
			// Matrix has no inverse
			throw new RuntimeException("Matrix4f12");
		}

		// Perform back substitution on the identity matrix
		for (i = 0; i < 16; i++) {
			result[i] = 0.0;
		}
		result[0] = 1.0;
		result[5] = 1.0;
		result[10] = 1.0;
		result[15] = 1.0;
		luBacksubstitution(temp, row_perm, result);

		this.m00 = (float) result[0];
		this.m01 = (float) result[1];
		this.m02 = (float) result[2];
		this.m03 = (float) result[3];

		this.m10 = (float) result[4];
		this.m11 = (float) result[5];
		this.m12 = (float) result[6];
		this.m13 = (float) result[7];

		this.m20 = (float) result[8];
		this.m21 = (float) result[9];
		this.m22 = (float) result[10];
		this.m23 = (float) result[11];

		this.m30 = (float) result[12];
		this.m31 = (float) result[13];
		this.m32 = (float) result[14];
		this.m33 = (float) result[15];
	}

	/**
	 * Given a 4x4 array "matrix0", this function replaces it with the LU
	 * decomposition of a row-wise permutation of itself. The input parameters
	 * are "matrix0" and "dimen". The array "matrix0" is also an output
	 * parameter. The vector "row_perm[4]" is an output parameter that contains
	 * the row permutations resulting from partial pivoting. The output
	 * parameter "even_row_xchg" is 1 when the number of row exchanges is even,
	 * or -1 otherwise. Assumes data type is always double.
	 * 
	 * This function is similar to luDecomposition, except that it is tuned
	 * specifically for 4x4 matrices.
	 * 
	 * @return true if the matrix is nonsingular, or false otherwise.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 40-45.
	//
	static boolean luDecomposition(double[] matrix0, int[] row_perm) {

		double row_scale[] = new double[4];

		// Determine implicit scaling information by looping over rows
		{
			int i, j;
			int ptr, rs;
			double big, temp;

			ptr = 0;
			rs = 0;

			// For each row ...
			i = 4;
			while (i-- != 0) {
				big = 0.0;

				// For each column, find the largest element in the row
				j = 4;
				while (j-- != 0) {
					temp = matrix0[ptr++];
					temp = Math.abs(temp);
					if (temp > big) {
						big = temp;
					}
				}

				// Is the matrix singular?
				if (big == 0.0) {
					return false;
				}
				row_scale[rs++] = 1.0 / big;
			}
		}

		{
			int j;
			int mtx;

			mtx = 0;

			// For all columns, execute Crout's method
			for (j = 0; j < 4; j++) {
				int i, imax, k;
				int target, p1, p2;
				double sum, big, temp;

				// Determine elements of upper diagonal matrix U
				for (i = 0; i < j; i++) {
					target = mtx + (4 * i) + j;
					sum = matrix0[target];
					k = i;
					p1 = mtx + (4 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 4;
					}
					matrix0[target] = sum;
				}

				// Search for largest pivot element and calculate
				// intermediate elements of lower diagonal matrix L.
				big = 0.0;
				imax = -1;
				for (i = j; i < 4; i++) {
					target = mtx + (4 * i) + j;
					sum = matrix0[target];
					k = j;
					p1 = mtx + (4 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 4;
					}
					matrix0[target] = sum;

					// Is this the best pivot so far?
					if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
						big = temp;
						imax = i;
					}
				}

				if (imax < 0) {
					throw new RuntimeException("Matrix4f13");
				}

				// Is a row exchange necessary?
				if (j != imax) {
					// Yes: exchange rows
					k = 4;
					p1 = mtx + (4 * imax);
					p2 = mtx + (4 * j);
					while (k-- != 0) {
						temp = matrix0[p1];
						matrix0[p1++] = matrix0[p2];
						matrix0[p2++] = temp;
					}

					// Record change in scale factor
					row_scale[imax] = row_scale[j];
				}

				// Record row permutation
				row_perm[j] = imax;

				// Is the matrix singular
				if (matrix0[(mtx + (4 * j) + j)] == 0.0) {
					return false;
				}

				// Divide elements of lower diagonal matrix L by pivot
				if (j != (4 - 1)) {
					temp = 1.0 / (matrix0[(mtx + (4 * j) + j)]);
					target = mtx + (4 * (j + 1)) + j;
					i = 3 - j;
					while (i-- != 0) {
						matrix0[target] *= temp;
						target += 4;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Solves a set of linear equations. The input parameters "matrix1", and
	 * "row_perm" come from luDecompostionD4x4 and do not change here. The
	 * parameter "matrix2" is a set of column vectors assembled into a 4x4
	 * matrix of floating-point values. The procedure takes each column of
	 * "matrix2" in turn and treats it as the right-hand side of the matrix
	 * equation Ax = LUx = b. The solution vector replaces the original column
	 * of the matrix.
	 * 
	 * If "matrix2" is the identity matrix, the procedure replaces its contents
	 * with the inverse of the matrix from which "matrix1" was originally
	 * derived.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 44-45.
	//
	static void luBacksubstitution(double[] matrix1, int[] row_perm,
			double[] matrix2) {

		int i, ii, ip, j, k;
		int rp;
		int cv, rv;

		// rp = row_perm;
		rp = 0;

		// For each column vector of matrix2 ...
		for (k = 0; k < 4; k++) {
			// cv = &(matrix2[0][k]);
			cv = k;
			ii = -1;

			// Forward substitution
			for (i = 0; i < 4; i++) {
				double sum;

				ip = row_perm[rp + i];
				sum = matrix2[cv + 4 * ip];
				matrix2[cv + 4 * ip] = matrix2[cv + 4 * i];
				if (ii >= 0) {
					// rv = &(matrix1[i][0]);
					rv = i * 4;
					for (j = ii; j <= i - 1; j++) {
						sum -= matrix1[rv + j] * matrix2[cv + 4 * j];
					}
				} else if (sum != 0.0) {
					ii = i;
				}
				matrix2[cv + 4 * i] = sum;
			}

			// Backsubstitution
			// rv = &(matrix1[3][0]);
			rv = 3 * 4;
			matrix2[cv + 4 * 3] /= matrix1[rv + 3];

			rv -= 4;
			matrix2[cv + 4 * 2] = (matrix2[cv + 4 * 2] - matrix1[rv + 3]
					* matrix2[cv + 4 * 3])
					/ matrix1[rv + 2];

			rv -= 4;
			matrix2[cv + 4 * 1] = (matrix2[cv + 4 * 1] - matrix1[rv + 2]
					* matrix2[cv + 4 * 2] - matrix1[rv + 3]
					* matrix2[cv + 4 * 3])
					/ matrix1[rv + 1];

			rv -= 4;
			matrix2[cv + 4 * 0] = (matrix2[cv + 4 * 0] - matrix1[rv + 1]
					* matrix2[cv + 4 * 1] - matrix1[rv + 2]
					* matrix2[cv + 4 * 2] - matrix1[rv + 3]
					* matrix2[cv + 4 * 3])
					/ matrix1[rv + 0];
		}
	}

	public FloatBuffer asFloatBuffer() {
		gFBMatrix.position(0);
		fillFloatBuffer(gFBMatrix);
		return gFBMatrix;
	}

	/**
	 * get float buffer coloum major
	 * 
	 * @param buffer
	 */
	public final void fillFloatBuffer(FloatBuffer buffer) {
		buffer.position(0);
		buffer.put(m00);
		buffer.put(m10);
		buffer.put(m20);
		buffer.put(m30);

		buffer.put(m01);
		buffer.put(m11);
		buffer.put(m21);
		buffer.put(m31);

		buffer.put(m02);
		buffer.put(m12);
		buffer.put(m22);
		buffer.put(m32);

		buffer.put(m03);
		buffer.put(m13);
		buffer.put(m23);
		buffer.put(m33);

		buffer.position(0);
	}

	public void fillFloatArray(float[] array) {
		FloatBuffer buf = asFloatBuffer();
		buf.get(array);
		buf.position(0);
	}

	private static Vector3f tmpF = new Vector3f(), tmpUp = new Vector3f(),
			tmpS = new Vector3f(), tmpT = new Vector3f();
	private static Matrix4f tmpMat = new Matrix4f();

	/**
	 * 模拟实现GLU.gluLookAt()函数，参数相同，将计算结果矩阵返回
	 * 
	 * @param eye
	 * @param center
	 * @param up
	 * @param out
	 *            - 返回的计算结果矩阵
	 */
	public static void gluLookAt(Vector3f eye, Vector3f center, Vector3f up,
			Matrix4f out) {
		tmpF.x = center.x - eye.x;
		tmpF.y = center.y - eye.y;
		tmpF.z = center.z - eye.z;

		tmpF.normalize();
		tmpUp.set(up);
		tmpUp.normalize();

		tmpS.cross(tmpF, tmpUp);
		tmpT.cross(tmpS, tmpF);

		out.m00 = tmpS.x;
		out.m10 = tmpT.x;
		out.m20 = -tmpF.x;
		out.m30 = 0;

		out.m01 = tmpS.y;
		out.m11 = tmpT.y;
		out.m21 = -tmpF.y;
		out.m31 = 0;

		out.m02 = tmpS.z;
		out.m12 = tmpT.z;
		out.m22 = -tmpF.z;
		out.m32 = 0;

		out.m03 = 0;
		out.m13 = 0;
		out.m23 = 0;
		out.m33 = 1;

		tmpMat.setIdentity();
		tmpMat.setTranslation(-eye.x, -eye.y, -eye.z);

		out.mul(tmpMat);
	}

	/**
	 * 模拟实现GLU.gluPersective()函数，参数相同，将计算结果填入返回矩阵中
	 * 
	 * @param fovy
	 * @param aspect
	 * @param zNear
	 * @param zFar
	 * @param out
	 *            - 计算结果返回
	 */
	public static void gluPersective(float fovy, float aspect, float zNear,
			float zFar, Matrix4f out) {
		float sine, cotangent, deltaZ;
		float radians = (float) (fovy / 2 * Math.PI / 180);

		deltaZ = zFar - zNear;
		sine = (float) Math.sin(radians);

		if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
			return;
		}

		cotangent = (float) Math.cos(radians) / sine;

		out.setIdentity();

		out.m00 = cotangent / aspect;
		out.m11 = cotangent;
		out.m22 = -(zFar + zNear) / deltaZ;
		out.m32 = -1;
		out.m23 = -2 * zNear * zFar / deltaZ;
		out.m33 = 0;
	}

}