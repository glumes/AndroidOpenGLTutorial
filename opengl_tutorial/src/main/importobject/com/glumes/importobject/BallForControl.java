package com.glumes.importobject;

import com.glumes.openglbasicshape.utils.MatrixState;

/**
 * Created by glumes on 25/06/2018
 */
//存储球运动过程中物理信息的对象所属类
public class BallForControl {
    public static final float TIME_SPAN = 0.05f;//单位时间间隔
    public static final float G = 0.8f;//重力加速度

    BallTextureByVertex btv;//用于绘制的篮球
    float startY;//每轮起始点位置
    float timeLive = 0;//此周期存活时长
    float currentY = 0;//当前Y位置
    float vy = 0;//每轮初始速度

    float BALL_SCALE = 1.0f;//球单位尺寸
    float UNIT_SIZE = 0.8f;//球单位尺寸

    public BallForControl(BallTextureByVertex btv, float startYIn) {
        this.btv = btv;
        this.startY = startYIn;
        currentY = startYIn;
        new Thread() {//开启一个线程运动篮球
            public void run() {
                while (true) {
                    //此轮运动时间增加
                    timeLive += TIME_SPAN;
                    //根据此轮起始Y坐标、此轮运动时间、此轮起始速度计算当前位置
                    float tempCurrY = startY - 0.5f * G * timeLive * timeLive + vy * timeLive;


                    if (tempCurrY <= 0) {//若当前位置低于地面则碰到地面反弹
                        //反弹后起始位置为0
                        startY = 0;
                        //反弹后起始速度
                        vy = -(vy - G * timeLive) * 0.995f;
                        //反弹后此轮运动时间清0
                        timeLive = 0;
                        //若速度小于阈值则停止运动
                        if (vy < 0.35f) {
                            currentY = 0;
                            break;
                        }
                    } else {//若没有碰到地面则正常运动
                        currentY = tempCurrY;
                    }

                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void drawSelf(int texId) {//绘制物体自己
        MatrixState.pushMatrix();
        MatrixState.translate(0, UNIT_SIZE * BALL_SCALE + currentY, 0);
        btv.drawSelf(texId);
        MatrixState.popMatrix();
    }

    public void drawSelfMirror(int texId) {//绘制 镜像体
        MatrixState.pushMatrix();
        MatrixState.translate(0, -UNIT_SIZE * BALL_SCALE - currentY, 0);
        btv.drawSelf(texId);
        MatrixState.popMatrix();
    }
}
