package com.glumes.openglbasicshape.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @Author glumes
 */
public class LoadUtil {

    /**
     * 从 obj 文件中加载物体的顶点信息
     * @param fname
     * @param context
     * @return
     */
    public static float[] loadFromFile(String fname, Context context) {
        ArrayList<Float> alv = new ArrayList<>();
        ArrayList<Float> alvResult = new ArrayList<>();
        float[] vXYZ;
        try {
            InputStream in = context.getResources().getAssets().open(fname);

            InputStreamReader isr = new InputStreamReader(in);

            BufferedReader br = new BufferedReader(isr);

            String temps = null;

            while ((temps = br.readLine()) != null) {
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {
                    int index = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    alvResult.add(alv.get(3 * index));
                    alvResult.add(alv.get(3 * index + 1));
                    alvResult.add(alv.get(3 * index + 2));

                    index = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    alvResult.add(alv.get(3 * index));
                    alvResult.add(alv.get(3 * index + 1));
                    alvResult.add(alv.get(3 * index + 2));

                    index = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    alvResult.add(alv.get((3 * index)));
                    alvResult.add(alv.get((3 * index + 1)));
                    alvResult.add(alv.get((3 * index + 2)));
                }
            }
            int size = alvResult.size();
            vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }
            return vXYZ;
        } catch (IOException e) {
            return null;
        }
    }
}
