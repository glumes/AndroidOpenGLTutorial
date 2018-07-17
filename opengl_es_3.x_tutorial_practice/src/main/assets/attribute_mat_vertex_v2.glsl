//#version 300 es

attribute vec4 a_Position;

attribute mat4 u_Matrix;

uniform mat4 u_ProMatrix;

uniform mat4 modelMatrix;


void main() {


     mat4 mvp = modelMatrix * u_Matrix;

     gl_Position = mvp * a_Position;

//    gl_Position =vec4(a_Position,1);

}
