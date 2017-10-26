/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glumes.magiccube.rubik;

public class GLColor {

    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;
    
    public static GLColor RED = new GLColor(1, 0, 0, 1);
    public static GLColor GREEN = new GLColor(0, 1, 0 , 1);
    public static GLColor BLUE = new GLColor(0, 0, 1, 1);
    public static GLColor YELLOW = new GLColor(1, 1, 0, 1);
    public static GLColor ORANGE = new GLColor(1, 0.5f,0,1);
    public static GLColor WHITE = new GLColor(1, 1, 1, 1);
    public static GLColor BLACK = new GLColor(0, 0, 0, 1);
    
    public GLColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public GLColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GLColor) {
            GLColor color = (GLColor)other;
            return (red == color.red &&
                    green == color.green &&
                    blue == color.blue &&
                    alpha == color.alpha);
        }
        return false;
    }
}
