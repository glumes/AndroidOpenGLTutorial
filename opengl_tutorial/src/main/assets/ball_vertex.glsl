#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
in vec3 aPosition;  //顶点位置
out vec3 vPosition;//用于传递给片元着色器的顶点位置
void main()
{
   //根据总变换矩阵计算此次绘制此顶点位置
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   //将顶点的位置传给片元着色器
   vPosition = aPosition;//将原始顶点位置传递给片元着色器
}