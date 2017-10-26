package lib;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Ray {

	private static FloatBuffer gBufPosition = IBufferFactory
			.newFloatBuffer(2 * 3);

	/**
	 * 射线原点
	 */
	public Vector3f mvOrigin = new Vector3f();
	/**
	 * 射线方向
	 */
	public Vector3f mvDirection = new Vector3f();

	/**
	 * 变换射线，将结果存储到out中
	 * 
	 * @param matrix
	 *            - 变换矩阵
	 * @param out
	 *            - 变换后的射线
	 */
	public void transform(Matrix4f matrix, Ray out) {
		Vector3f v0 = Vector3f.TEMP;
		Vector3f v1 = Vector3f.TEMP1;
		v0.set(mvOrigin);
		v1.set(mvOrigin);
		v1.add(mvDirection);

		matrix.transform(v0, v0);
		matrix.transform(v1, v1);

		out.mvOrigin.set(v0);
		v1.sub(v0);
		v1.normalize();
		out.mvDirection.set(v1);
	}

	/**
	 * 渲染射线
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gBufPosition.position(0);

		IBufferFactory.fillBuffer(gBufPosition, mvOrigin);

		Vector3f.TEMP.set(mvDirection);
		float len = 100.0f;
		Vector3f.TEMP.scale(len);
		Vector3f.TEMP.add(mvOrigin);
		IBufferFactory.fillBuffer(gBufPosition, Vector3f.TEMP);
		gBufPosition.position(0);

		if (AppConfig.gbTrianglePicked) {
			gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		} else {
			gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		}

		gl.glPointSize(4.0f);
		gl.glLineWidth(4.0f);
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, gBufPosition);
		gl.glDrawArrays(GL10.GL_POINTS, 0, 2);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPointSize(1f);
		gl.glLineWidth(1.0f);
	}

	/**
	 * 检测射线是否与三角形相交
	 * 
	 * @param v0
	 *            三角形顶点0
	 * @param v1
	 *            三角形顶点1
	 * @param v2
	 *            三角形顶点2
	 * @param location
	 *            - 相交点位置，以Vector4f的形式存储。其中(x,y,z)表示相交点的具体位置，w表示相交点离射线原点的距离
	 * @return 如果相交返回true
	 */
	public boolean intersectTriangle(Vector3f v0, Vector3f v1, Vector3f v2,
			Vector4f location) {
		return intersect(v0, v1, v2, location);
	}

	private static final float MAX_ABSOLUTE_ERROR = 0.000001f;

	private static Vector3f tmp0 = new Vector3f(), tmp1 = new Vector3f(),
			tmp2 = new Vector3f(), tmp3 = new Vector3f(),
			tmp4 = new Vector3f();

	/**
	 * 射线与三角形相交检测函数
	 * 
	 * @param v0
	 *            三角形顶点0
	 * @param v1
	 *            三角形顶点1
	 * @param v2
	 *            三角形顶点2
	 * @param loc
	 *            相交点位置，以Vector4f的形式存储。其中(x,y,z)表示相交点的具体位置，w表示相交点离射线原点的距离;
	 *            如果为null则不计算相交点
	 * @return 如果相交返回true
	 */
	private boolean intersect(Vector3f v0, Vector3f v1, Vector3f v2,
			Vector4f loc) {
		Vector3f diff = tmp0;
		Vector3f edge1 = tmp1;
		Vector3f edge2 = tmp2;
		Vector3f norm = tmp3;
		Vector3f tmp = tmp4;
		diff.sub(mvOrigin, v0);
		edge1.sub(v1, v0);
		edge2.sub(v2, v0);
		norm.cross(edge1, edge2);

		float dirDotNorm = mvDirection.dot(norm);
		float sign = 0.0f;

		if (dirDotNorm > MAX_ABSOLUTE_ERROR) {
			sign = 1;
		} else if (dirDotNorm < -MAX_ABSOLUTE_ERROR) {
			sign = -1;
			dirDotNorm = -dirDotNorm;
		} else {
			// 射线和三角形平行，不可能相交
			return false;
		}

		tmp.cross(diff, edge2);
		float dirDotDiffxEdge2 = sign * mvDirection.dot(tmp);
		if (dirDotDiffxEdge2 >= 0.0f) {
			tmp.cross(edge1, diff);
			float dirDotEdge1xDiff = sign * mvDirection.dot(tmp);
			if (dirDotEdge1xDiff >= 0.0f) {
				if (dirDotDiffxEdge2 + dirDotEdge1xDiff <= dirDotNorm) {
					float diffDotNorm = -sign * diff.dot(norm);
					if (diffDotNorm >= 0.0f) {
						// 检测到相交事件
						// 如果不需要计算精确相交点，则直接返回
						if (loc == null) {
							return true;
						}
						// 计算相交点具体位置，存储在Vector4f的x,y,z中，把距离存储在w中
						float inv = 1f / dirDotNorm;
						float t = diffDotNorm * inv;

						loc.set(mvOrigin);
						loc.add(mvDirection.x * t, mvDirection.y * t,
								mvDirection.z * t);
						loc.w = t;

						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * 检测射线是否与包围球相交
	 * 
	 * @param center
	 *            圆心
	 * @param radius
	 *            半径
	 * @return 如果相交返回true
	 */
	public boolean intersectSphere(Vector3f center, float radius) {
		Vector3f diff = tmp0;
		
		//射线原点到圆点中心点的矢量
		diff.sub(mvOrigin, center);
		float r2 = radius * radius;
		
		//两点间距离的平方和半径的平方比较
		float a = diff.dot(diff) - r2;
		
		if (a <= 0.0f) {
			// 在包围球内
			return true;
		}

		//P·Q = |P| |Q| cos(P, Q)
		float b = mvDirection.dot(diff);
		
		//射线原点到圆点中心点的矢量与射线方向夹角>=90
		if (b >= 0.0f) {
			return false;
		}
		
		//除非射线段的长度小于射线原点到球的距离
		return b * b >= a;
	}
}
