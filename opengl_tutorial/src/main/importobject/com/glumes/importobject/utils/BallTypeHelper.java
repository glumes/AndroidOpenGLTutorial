package com.glumes.importobject.utils;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glumes on 18/07/2018
 */
public class BallTypeHelper {

    @SuppressLint("UseSparseArrays")

    public static int NormalBall = 0;
    public static int AmbientBall = 1;

    public static SparseArray<String> mVertexMap = new SparseArray<>();

    public static SparseArray<String> mFragMap = new SparseArray<>();

    static {
        mVertexMap.append(NormalBall, "ball_vertex.glsl");
        mVertexMap.append(AmbientBall, "ball_vertex_ambient.glsl");


        mFragMap.append(NormalBall, "ball_frag.glsl");
        mFragMap.append(AmbientBall, "ball_frag_ambient.glsl");
    }
}
