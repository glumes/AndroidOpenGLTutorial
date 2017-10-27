package lib;

public class PickFactory {

	private static Ray gPickRay = new Ray();

	public static Ray getPickRay() {
		return gPickRay;
	}

	private static Projector gProjector = new Projector();

	private static float[] gpObjPosArray = new float[4];

	/**
	 * 更新拾取射线
	 * @param screenX - 屏幕坐标X
	 * @param screenY - 屏幕坐标Y
	 */
	public static void update(float screenX, float screenY) {
		AppConfig.gMatView.fillFloatArray(AppConfig.gpMatrixViewArray);

		// 由于OpenGL屏幕坐标系原点为左下角，而窗口坐标系原点为左上角
		// 因此，在OpenGl中的Y应该需要用当前视口高度，减去窗口坐标Y
		float openglY = AppConfig.gpViewport[3] - screenY;
		
		// z = 0 , 得到P0
		gProjector.gluUnProject(screenX, openglY, 0.0f,
				AppConfig.gpMatrixViewArray, 0, AppConfig.gpMatrixProjectArray,
				0, AppConfig.gpViewport, 0, gpObjPosArray, 0);
		
		// 填充射线原点P0
		gPickRay.mvOrigin.set(gpObjPosArray[0], gpObjPosArray[1],gpObjPosArray[2]);

		// z = 1 ，得到P1
		gProjector.gluUnProject(screenX, openglY, 1.0f,
				AppConfig.gpMatrixViewArray, 0, AppConfig.gpMatrixProjectArray,
				0, AppConfig.gpViewport, 0, gpObjPosArray, 0);
		
		// 计算射线的方向，P1 - P0
		gPickRay.mvDirection.set(gpObjPosArray[0], gpObjPosArray[1],gpObjPosArray[2]);
		gPickRay.mvDirection.sub(gPickRay.mvOrigin);
		
		// 向量归一化
		gPickRay.mvDirection.normalize();
	}

}
