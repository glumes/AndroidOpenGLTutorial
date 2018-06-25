package com.glumes.importobject;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glumes on 25/06/2018
 */

public class TreeGroup {
    TreeForDraw tfd;
    public List<SingleTree> alist = new ArrayList<SingleTree>();

    final float UNIT_SIZE = 1f;

    public TreeGroup(Resources res) {

        tfd = new TreeForDraw(res);

        alist.add(new SingleTree(0, 0, 0, this));
        alist.add(new SingleTree(8 * UNIT_SIZE, 0, 0, this));
        alist.add(new SingleTree(5.7f * UNIT_SIZE, 5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(0, -8 * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(-5.7f * UNIT_SIZE, 5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(-8 * UNIT_SIZE, 0, 0, this));
        alist.add(new SingleTree(-5.7f * UNIT_SIZE, -5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(0, 8 * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(5.7f * UNIT_SIZE, -5.7f * UNIT_SIZE, 0, this));

    }

    public void calculateBillboardDirection() {
        //计算列表中每个树木的朝向
        for (int i = 0; i < alist.size(); i++) {
            alist.get(i).calculateBillboardDirection();
        }
    }

    public void drawSelf(int texId) {//绘制列表中的每个树木
        for (int i = 0; i < alist.size(); i++) {
            alist.get(i).drawSelf(texId);
        }
    }
}