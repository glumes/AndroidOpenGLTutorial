package lib;

public class Quat4f {

	public float x, y, z, w;

	/**
	 * convert euler angle to Quat4f
	 * 
	 * @param euler
	 */
	public final void set(Vector3f euler) {
		float angle = 0.0f;
		float sr, sp, sy, cr, cp, cy;

		// rescale the inputs to 1/2 angle
		angle = euler.z * 0.5f;
		sy = (float) Math.sin(angle);
		cy = (float) Math.cos(angle);
		angle = euler.y * 0.5f;
		sp = (float) Math.sin(angle);
		cp = (float) Math.cos(angle);
		angle = euler.x * 0.5f;
		sr = (float) Math.sin(angle);
		cr = (float) Math.cos(angle);

		x = sr * cp * cy - cr * sp * sy; // X
		y = cr * sp * cy + sr * cp * sy; // Y
		z = cr * cp * sy - sr * sp * cy; // Z
		w = cr * cp * cy + sr * sp * sy; // W
	}

	/**
	 * Performs a great circle interpolation between quaternion q1 and
	 * quaternion q2 and places the result into this quaternion.
	 * 
	 * @param q1
	 *            the first quaternion
	 * @param q2
	 *            the second quaternion
	 * @param alpha
	 *            the alpha interpolation parameter
	 */
	public final void interpolate(Quat4f q1, Quat4f q2, float alpha) {
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be
		// incorrect. Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed.

		double dot, s1, s2, om, sinom;

		dot = q2.x * q1.x + q2.y * q1.y + q2.z * q1.z + q2.w * q1.w;

		if (dot < 0) {
			// negate quaternion
			q1.x = -q1.x;
			q1.y = -q1.y;
			q1.z = -q1.z;
			q1.w = -q1.w;
			dot = -dot;
		}

		if ((1.0 - dot) > 0.000001f) {
			om = Math.acos(dot);
			sinom = Math.sin(om);
			s1 = Math.sin((1.0 - alpha) * om) / sinom;
			s2 = Math.sin(alpha * om) / sinom;
		} else {
			s1 = 1.0 - alpha;
			s2 = alpha;
		}
		w = (float) (s1 * q1.w + s2 * q2.w);
		x = (float) (s1 * q1.x + s2 * q2.x);
		y = (float) (s1 * q1.y + s2 * q2.y);
		z = (float) (s1 * q1.z + s2 * q2.z);
	}

}
