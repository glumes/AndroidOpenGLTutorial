package com.glumes.openglbasicshape.bezier.drawer;


/**
 *
 * 将系统返回二维坐标归一化处理
 */

public class NormalSizeHelper {
    private static boolean sIsVertical;
    private static float sAspectRatio;
    private static int sWidth;
    private static int sHeight;

    private NormalSizeHelper() {
    }

    public static void setIsVertical(boolean isVertical) {
        sIsVertical = isVertical;
    }

    public static void setAspectRatio(float aspectRatio) {
        sAspectRatio = aspectRatio;
    }

    public static void setSurfaceViewInfo(int width, int height) {
        sWidth = width;
        sHeight = height;
    }

    public static float getAspectRatio() {
        return sAspectRatio;
    }

    /**
     * @return X坐标归一
     */
    public static float convertNormalX(float x) {
        if (sIsVertical) {
            return (x / sWidth) * 2 - 1;
        } else {
            return (x / sWidth) * 2 * sAspectRatio - sAspectRatio;
        }
    }

    /**
     * @return Y坐标归一
     */
    public static float convertNormalY(float y) {
        if (sIsVertical) {
            return sAspectRatio - (y / sHeight) * 2 * sAspectRatio;
        } else {
            return 1 - (y / sHeight) * 2;
        }
    }

    /**
     * @return X距离归一
     */
    public static float convertNormalDX(float dx) {
        return dx / getNormalScreenSize() * 2;
    }

    /**
     * @return Y距离归一
     */
    public static float convertNormalDY(float dy) {
        return -(dy / getNormalScreenSize()) * 2;
    }

    public static float getNormalScreenSize() {
        if (sIsVertical) {
            return sWidth;
        } else {
            return sHeight;
        }
    }
}
