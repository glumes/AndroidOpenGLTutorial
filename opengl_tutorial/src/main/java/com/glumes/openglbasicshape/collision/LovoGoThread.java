package com.glumes.openglbasicshape.collision;

import com.glumes.openglbasicshape.base.LogUtil;

import java.util.ArrayList;

/**
 * Created by glumes on 24/07/2018
 */
public class LovoGoThread extends Thread {

    ArrayList<RigidBody> al;//控制列表
    boolean flag = true;//线程控制标志位

    public LovoGoThread(ArrayList<RigidBody> al) {
        this.al = al;
    }

    public void run() {
        while (flag) {
            int size = al.size();
            for (int i = 0; i < size; i++) {
                al.get(i).go(al);
            }
            try {
                sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
