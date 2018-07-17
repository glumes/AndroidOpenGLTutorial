#version 300 es
in vec3 a_Position;
in mat4 u_Matrix;

uniform mat4 u_ProMatrix;

uniform mat4 modelMatrix;

void main() {

    mat4 mvp = modelMatrix * u_Matrix;

     gl_Position =   mvp * vec4(a_Position,1);

//    gl_Position = modelMatrix * vec4(a_Position,1);

}
