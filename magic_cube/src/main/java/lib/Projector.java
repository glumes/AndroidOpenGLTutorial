package lib;

/**
 * 自定义实现的投影/反投影函数
 * 
 * @author Yong
 */
public class Projector {
	private static final float[] IDENTITY_MATRIX = new float[] { 1, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
	private final float[] matrix = new float[16];

	private final float[][] tempMatrix = new float[4][4];
	private final float[] in = new float[4];
	private final float[] out = new float[4];

	/**
	 * Method gluProject
	 * 
	 * @param objx
	 * @param objy
	 * @param objz
	 * @param modelMatrix
	 * @param projMatrix
	 * @param viewport
	 * @param win_pos
	 * 
	 * @return
	 */
	public boolean gluProject(float objx, float objy, float objz,
			float[] modelMatrix, int modelMatrix_offset, float[] projMatrix,
			int projMatrix_offset, int[] viewport, int viewport_offset,
			float[] win_pos, int win_pos_offset) {

		float[] in = this.in;
		float[] out = this.out;

		in[0] = objx;
		in[1] = objy;
		in[2] = objz;
		in[3] = 1.0f;

		__gluMultMatrixVecd(modelMatrix, modelMatrix_offset, in, out);
		__gluMultMatrixVecd(projMatrix, projMatrix_offset, out, in);

		if (in[3] == 0.0)
			return false;

		in[3] = (1.0f / in[3]) * 0.5f;

		// Map x, y and z to range 0-1
		in[0] = in[0] * in[3] + 0.5f;
		in[1] = in[1] * in[3] + 0.5f;
		in[2] = in[2] * in[3] + 0.5f;

		// Map x,y to viewport
		win_pos[0 + win_pos_offset] = in[0] * viewport[2 + viewport_offset]
				+ viewport[0 + viewport_offset];
		win_pos[1 + win_pos_offset] = in[1] * viewport[3 + viewport_offset]
				+ viewport[1 + viewport_offset];
		win_pos[2 + win_pos_offset] = in[2];

		return true;
	}

	/**
	 * Method gluUnproject
	 * 
	 * @param winx
	 * @param winy
	 * @param winz
	 * @param modelMatrix
	 * @param projMatrix
	 * @param viewport
	 * @param obj_pos
	 * 
	 * @return
	 */
	public boolean gluUnProject(float winx, float winy, float winz,
			float[] modelMatrix, int modelMatrix_offset, float[] projMatrix,
			int projMatrix_offset, int[] viewport, int viewport_offset,
			float[] obj_pos, int obj_pos_offset) {
		float[] in = this.in;
		float[] out = this.out;

		__gluMultMatricesd(modelMatrix, modelMatrix_offset, projMatrix,
				projMatrix_offset, matrix);

		if (!__gluInvertMatrixd(matrix, matrix))
			return false;

		in[0] = winx;
		in[1] = winy;
		in[2] = winz;
		in[3] = 1.0f;

		// Map x and y from window coordinates
		in[0] = (in[0] - viewport[0 + viewport_offset])
				/ viewport[2 + viewport_offset];
		in[1] = (in[1] - viewport[1 + viewport_offset])
				/ viewport[3 + viewport_offset];

		// Map to range -1 to 1
		in[0] = in[0] * 2 - 1;
		in[1] = in[1] * 2 - 1;
		in[2] = in[2] * 2 - 1;

		__gluMultMatrixVecd(matrix, 0, in, out);

		if (out[3] == 0.0)
			return false;

		out[3] = 1.0f / out[3];

		obj_pos[0 + obj_pos_offset] = out[0] * out[3];
		obj_pos[1 + obj_pos_offset] = out[1] * out[3];
		obj_pos[2 + obj_pos_offset] = out[2] * out[3];

		return true;
	}

	/**
	 * @param a
	 * @param b
	 * @param r
	 */
	private void __gluMultMatricesd(float[] a, int a_offset, float[] b,
			int b_offset, float[] r) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				r[i * 4 + j] = a[i * 4 + 0 + a_offset]
						* b[0 * 4 + j + b_offset] + a[i * 4 + 1 + a_offset]
						* b[1 * 4 + j + b_offset] + a[i * 4 + 2 + a_offset]
						* b[2 * 4 + j + b_offset] + a[i * 4 + 3 + a_offset]
						* b[3 * 4 + j + b_offset];
			}
		}
	}

	/**
	 * Method __gluMultMatrixVecd
	 * 
	 * @param matrix
	 * @param in
	 * @param out
	 */
	private void __gluMultMatrixVecd(float[] matrix, int matrix_offset,
			float[] in, float[] out) {
		for (int i = 0; i < 4; i++) {
			out[i] = in[0] * matrix[0 * 4 + i + matrix_offset] + in[1]
					* matrix[1 * 4 + i + matrix_offset] + in[2]
					* matrix[2 * 4 + i + matrix_offset] + in[3]
					* matrix[3 * 4 + i + matrix_offset];
		}
	}

	/**
	 * @param src
	 * @param inverse
	 * 
	 * @return
	 */
	private boolean __gluInvertMatrixd(float[] src, float[] inverse) {
		int i, j, k, swap;
		float t;
		float[][] temp = tempMatrix;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				temp[i][j] = src[i * 4 + j];
			}
		}
		__gluMakeIdentityd(inverse);

		for (i = 0; i < 4; i++) {
			//
			// Look for largest element in column
			//
			swap = i;
			for (j = i + 1; j < 4; j++) {
				if (Math.abs(temp[j][i]) > Math.abs(temp[i][i])) {
					swap = j;
				}
			}

			if (swap != i) {
				//
				// Swap rows.
				//
				for (k = 0; k < 4; k++) {
					t = temp[i][k];
					temp[i][k] = temp[swap][k];
					temp[swap][k] = t;

					t = inverse[i * 4 + k];
					inverse[i * 4 + k] = inverse[swap * 4 + k];
					inverse[swap * 4 + k] = t;
				}
			}

			if (temp[i][i] == 0) {
				//
				// No non-zero pivot. The matrix is singular, which shouldn't
				// happen. This means the user gave us a bad matrix.
				//
				return false;
			}

			t = temp[i][i];
			for (k = 0; k < 4; k++) {
				temp[i][k] /= t;
				inverse[i * 4 + k] /= t;
			}
			for (j = 0; j < 4; j++) {
				if (j != i) {
					t = temp[j][i];
					for (k = 0; k < 4; k++) {
						temp[j][k] -= temp[i][k] * t;
						inverse[j * 4 + k] -= inverse[i * 4 + k] * t;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Make matrix an identity matrix
	 */
	private void __gluMakeIdentityd(float[] m) {
		for (int i = 0; i < 16; i++) {
			m[i] = IDENTITY_MATRIX[i];
		}
	}

}
