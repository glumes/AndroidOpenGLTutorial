package com.glumes.importobject.utils;

import android.content.res.Resources;
import android.util.Log;

import com.glumes.importobject.LoadedObjectVertexNormal;
import com.glumes.importobject.LoadedObjectVertexNormalAverage;
import com.glumes.importobject.LoadedObjectVertexNormalFace;
import com.glumes.openglbasicshape.collision.CollisionObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by glumes on 25/06/2018
 */
public class ObjectLoadUtil {
    //求两个向量的叉积
    public static float[] getCrossProduct(float x1, float y1, float z1, float x2, float y2, float z2) {
        //求出两个矢量叉积矢量在XYZ轴的分量ABC
        float A = y1 * z2 - y2 * z1;
        float B = z1 * x2 - z2 * x1;
        float C = x1 * y2 - x2 * y1;

        return new float[]{A, B, C};
    }

    //向量规格化
    public static float[] vectorNormal(float[] vector) {
        //求向量的模
        float module = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        return new float[]{vector[0] / module, vector[1] / module, vector[2] / module};
    }

    //从obj文件中加载携带顶点信息的物体，并自动计算每个顶点的平均法向量
    public static LoadedObjectVertexNormalAverage loadFromFileVertexOnlyAverage(String fname, Resources r) {
        //加载后物体的引用
        LoadedObjectVertexNormalAverage lo = null;
        //原始顶点坐标列表--直接从obj文件中加载
        ArrayList<Float> alv = new ArrayList<Float>();
        //顶点组装面索引列表--根据面的信息从文件中加载
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();
        //结果顶点坐标列表--按面组织好
        ArrayList<Float> alvResult = new ArrayList<Float>();
        //平均前各个索引对应的点的法向量集合Map
        //此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
        HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();

        try {
            InputStream in = r.getAssets().open(fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            //扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                //用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {//此行为三角形面
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
                     *的法向量集合组成的Map中
                     */

                    int[] index = new int[3];//三个顶点索引值的数组

                    //计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    //记录此面的顶点索引
                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    //通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                    //求0号点到1号点的向量
                    float vxa = x1 - x0;
                    float vya = y1 - y0;
                    float vza = z1 - z0;
                    //求0号点到2号点的向量
                    float vxb = x2 - x0;
                    float vyb = y2 - y0;
                    float vzb = z2 - z0;
                    //通过求两个向量的叉积计算法向量
                    float[] vNormal = vectorNormal(getCrossProduct
                            (
                                    vxa, vya, vza, vxb, vyb, vzb
                            ));

                    for (int tempInxex : index) {//记录每个索引点的法向量到平均前各个索引对应的点的法向量集合组成的Map中
                        //获取当前索引对应点的法向量集合
                        HashSet<Normal> hsn = hmn.get(tempInxex);
                        if (hsn == null) {//若集合不存在则创建
                            hsn = new HashSet<Normal>();
                        }
                        //将此点的法向量添加到集合中
                        //由于Normal类重写了equals方法，因此同样的法向量不会重复出现在此点
                        //对应的法向量集合中
                        hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
                        //将集合放进HsahMap中
                        hmn.put(tempInxex, hsn);
                    }
                }
            }

            //生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            //生成法向量数组
            float[] nXYZ = new float[alFaceIndex.size() * 3];
            int c = 0;
            for (Integer i : alFaceIndex) {
                //根据当前点的索引从Map中取出一个法向量的集合
                HashSet<Normal> hsn = hmn.get(i);
                //求出平均法向量
                float[] tn = Normal.getAverage(hsn);
                //将计算出的平均法向量存放到法向量数组中
                nXYZ[c++] = tn[0];
                nXYZ[c++] = tn[1];
                nXYZ[c++] = tn[2];
            }
            //创建3D物体对象
            lo = new LoadedObjectVertexNormalAverage(r, vXYZ, nXYZ);
        } catch (Exception e) {
            Log.d("load error", "load error");
            e.printStackTrace();
        }
        return lo;
    }

    //从obj文件中加载仅携带顶点信息的物体
    //首先加载顶点信息，再根据顶点组成三角形面的情况自动计算出每个面的法向量
    //然后将这个面的法向量分配给这个面上的顶点
    public static LoadedObjectVertexNormalFace loadFromFileVertexOnlyFace(String fname, Resources r) {
        //加载后3D对象的引用
        LoadedObjectVertexNormalFace lo = null;
        //原始顶点坐标列表--按顺序从obj文件中加载的
        ArrayList<Float> alv = new ArrayList<Float>();
        //结果顶点坐标列表 --根据组成面的情况组织好的
        ArrayList<Float> alvResult = new ArrayList<Float>();
        //结果法向量列表--根据组成面的情况组织好的
        ArrayList<Float> alnResult = new ArrayList<Float>();

        try {
            InputStream in = r.getAssets().open(fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            //循环不断从文件中读取行，根据行类型的不同执行
            //不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {//此行为三角形面
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出法向量并添加到结果法向量列表中
                     */

                    //提取三角形第一个顶点的坐标
                    int index = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index);
                    float y0 = alv.get(3 * index + 1);
                    float z0 = alv.get(3 * index + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //提取三角形第二个顶点的坐标
                    index = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index);
                    float y1 = alv.get(3 * index + 1);
                    float z1 = alv.get(3 * index + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //提取三角形第三个顶点的坐标
                    index = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index);
                    float y2 = alv.get(3 * index + 1);
                    float z2 = alv.get(3 * index + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    //通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                    //求0号点到1号点的向量
                    float vxa = x1 - x0;
                    float vya = y1 - y0;
                    float vza = z1 - z0;
                    //求0号点到2号点的向量
                    float vxb = x2 - x0;
                    float vyb = y2 - y0;
                    float vzb = z2 - z0;

                    //通过球两个向量的叉积计算法向量
                    float[] vNormal = vectorNormal
                            (
                                    getCrossProduct
                                            (
                                                    vxa, vya, vza, vxb, vyb, vzb
                                            )
                            );
                    //将计算出的法向量添加到结果法向量列表中
                    for (int i = 0; i < 3; i++) {
                        alnResult.add(vNormal[0]);
                        alnResult.add(vNormal[1]);
                        alnResult.add(vNormal[2]);
                    }
                }
            }

            //obj文件读取结束后生成顶点数组及生成法向量数组
            //生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            //生成法向量数组
            size = alnResult.size();
            float[] nXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                nXYZ[i] = alnResult.get(i);
            }

            //创建3D对象
            lo = new LoadedObjectVertexNormalFace(r, vXYZ, nXYZ);
        } catch (Exception e) {
            Log.d("load error", "load error");
            e.printStackTrace();
        }
        return lo;
    }

    //从obj文件中加载携带顶点信息的物体，并自动计算每个顶点的平均法向量
    public static LoadedObjectVertexNormal loadFromFileVertexOnly(String fname, Resources r) {
        //加载后物体的引用
        LoadedObjectVertexNormal lo = null;
        //原始顶点坐标列表--直接从obj文件中加载
        ArrayList<Float> alv = new ArrayList<Float>();
        //顶点组装面索引列表--根据面的信息从文件中加载
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();
        //结果顶点坐标列表--按面组织好
        ArrayList<Float> alvResult = new ArrayList<Float>();
        //平均前各个索引对应的点的法向量集合Map
        //此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
        HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();

        try {
            InputStream in = r.getAssets().open(fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            //扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                //用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {//此行为三角形面
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
                     *的法向量集合组成的Map中
                     */

                    int[] index = new int[3];//三个顶点索引值的数组

                    //计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    //记录此面的顶点索引
                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    //通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                    //求0号点到1号点的向量
                    float vxa = x1 - x0;
                    float vya = y1 - y0;
                    float vza = z1 - z0;
                    //求0号点到2号点的向量
                    float vxb = x2 - x0;
                    float vyb = y2 - y0;
                    float vzb = z2 - z0;
                    //通过求两个向量的叉积计算法向量
                    float[] vNormal = getCrossProduct
                            (
                                    vxa, vya, vza, vxb, vyb, vzb
                            );

                    for (int tempInxex : index) {//记录每个索引点的法向量到平均前各个索引对应的点的法向量集合组成的Map中
                        //获取当前索引对应点的法向量集合
                        HashSet<Normal> hsn = hmn.get(tempInxex);
                        if (hsn == null) {//若集合不存在则创建
                            hsn = new HashSet<Normal>();
                        }
                        //将此点的法向量添加到集合中
                        //由于Normal类重写了equals方法，因此同样的法向量不会重复出现在此点
                        //对应的法向量集合中
                        hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
                        //将集合放进HsahMap中
                        hmn.put(tempInxex, hsn);
                    }
                }
            }

            //生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            //生成法向量数组
            float[] nXYZ = new float[alFaceIndex.size() * 3];
            int c = 0;
            for (Integer i : alFaceIndex) {
                //根据当前点的索引从Map中取出一个法向量的集合
                HashSet<Normal> hsn = hmn.get(i);
                //求出平均法向量
                float[] tn = Normal.getAverage(hsn);
                //将计算出的平均法向量存放到法向量数组中
                nXYZ[c++] = tn[0];
                nXYZ[c++] = tn[1];
                nXYZ[c++] = tn[2];
            }
            //创建3D物体对象
            lo = new LoadedObjectVertexNormal(r, vXYZ, nXYZ);
        } catch (Exception e) {
            Log.d("load error", "load error");
            e.printStackTrace();
        }
        return lo;
    }


    public static CollisionObject loadCollisionObjectloadFromFile(String fname, Resources r) {
        //加载后物体的引用
        CollisionObject lo = null;
        //原始顶点坐标列表--直接从obj文件中加载
        ArrayList<Float> alv = new ArrayList<Float>();
        //顶点组装面索引列表--根据面的信息从文件中加载
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();
        //结果顶点坐标列表--按面组织好
        ArrayList<Float> alvResult = new ArrayList<Float>();
        //平均前各个索引对应的点的法向量集合Map
        //此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
        HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();

        try {
            InputStream in = r.getAssets().open(fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            //扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                //用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {//此行为三角形面
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
                     *的法向量集合组成的Map中
                     */

                    int[] index = new int[3];//三个顶点索引值的数组

                    //计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    //记录此面的顶点索引
                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    //通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                    //求0号点到1号点的向量
                    float vxa = x1 - x0;
                    float vya = y1 - y0;
                    float vza = z1 - z0;
                    //求0号点到2号点的向量
                    float vxb = x2 - x0;
                    float vyb = y2 - y0;
                    float vzb = z2 - z0;
                    //通过求两个向量的叉积计算法向量
                    float[] vNormal = vectorNormal(getCrossProduct
                            (
                                    vxa, vya, vza, vxb, vyb, vzb
                            ));

                    for (int tempInxex : index) {//记录每个索引点的法向量到平均前各个索引对应的点的法向量集合组成的Map中
                        //获取当前索引对应点的法向量集合
                        HashSet<Normal> hsn = hmn.get(tempInxex);
                        if (hsn == null) {//若集合不存在则创建
                            hsn = new HashSet<Normal>();
                        }
                        //将此点的法向量添加到集合中
                        //由于Normal类重写了equals方法，因此同样的法向量不会重复出现在此点
                        //对应的法向量集合中
                        hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
                        //将集合放进HsahMap中
                        hmn.put(tempInxex, hsn);
                    }
                }
            }

            //生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            //生成法向量数组
            float[] nXYZ = new float[alFaceIndex.size() * 3];
            int c = 0;
            for (Integer i : alFaceIndex) {
                //根据当前点的索引从Map中取出一个法向量的集合
                HashSet<Normal> hsn = hmn.get(i);
                //求出平均法向量
                float[] tn = Normal.getAverage(hsn);
                //将计算出的平均法向量存放到法向量数组中
                nXYZ[c++] = tn[0];
                nXYZ[c++] = tn[1];
                nXYZ[c++] = tn[2];
            }
            //创建3D物体对象
            lo = new CollisionObject(r, vXYZ, nXYZ);
        } catch (Exception e) {
            Log.d("load error", "load error");
            e.printStackTrace();
        }
        return lo;
    }
}
