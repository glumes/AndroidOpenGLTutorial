package com.glumes.openglbasicshape.data;

import com.glumes.openglbasicshape.utils.Constant;

import java.nio.ByteBuffer;

/**
 * Created by glumes on 2017/8/13.
 */

public class PositionArray {

    private final ByteBuffer byteBuffer;

    public PositionArray(byte[] positionData) {
        byteBuffer = ByteBuffer.allocateDirect(positionData.length * Constant.BYTES_PRE_BYTE)
                .put(positionData);
    }


}
