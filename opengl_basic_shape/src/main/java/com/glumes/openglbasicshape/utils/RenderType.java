package com.glumes.openglbasicshape.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author glumes
 */

public class RenderType {


    public static List<Integer> RENDER_TYPE_LIST = new ArrayList<>();

    public static final int RENDER_TYPE_TITLE = 0x00;

    public static final int RENDER_TYPE_POINT = RENDER_TYPE_TITLE + 1;
    public static final int RENDER_TYPE_LINE = RENDER_TYPE_TITLE + 2;
    public static final int RENDER_TYPE_TRIANGLE = RENDER_TYPE_TITLE + 3;
    public static final int RENDER_TYPE_RECTANGLE = RENDER_TYPE_TITLE + 4;
    public static final int RENDER_TYPE_CIRCLE = RENDER_TYPE_TITLE + 5;
    public static final int RENDER_TYPE_CUBE = RENDER_TYPE_TITLE + 6;
    public static final int RENDER_TYPE_MULTI_CUBE = RENDER_TYPE_TITLE + 7;
    public static final int RENDER_TYPE_SPHERE = RENDER_TYPE_TITLE + 8;

    public static final int RENDER_TYPE_MAGIC_CUBE = RENDER_TYPE_TITLE + 9;

    static {

        RENDER_TYPE_LIST.add(RENDER_TYPE_POINT);
        RENDER_TYPE_LIST.add(RENDER_TYPE_LINE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_TRIANGLE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_RECTANGLE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_CIRCLE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_CUBE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_MULTI_CUBE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_SPHERE);
        RENDER_TYPE_LIST.add(RENDER_TYPE_MAGIC_CUBE);

    }
}
