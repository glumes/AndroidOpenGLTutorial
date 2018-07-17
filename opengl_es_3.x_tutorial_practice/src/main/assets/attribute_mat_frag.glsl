#version 300 es
precision mediump float;

uniform vec4 u_Color;

out vec4 fragColor;//输出到的片元颜色

void main() {

     fragColor = u_Color;

}
