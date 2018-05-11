#version 300 es
uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵
in vec3 aPosition;  //顶点位置
in vec4 aColor;    //顶点颜色
out vec4 vColor;  //用于传递给片元着色器的颜色
out vec3 vPosition;//用于传递给片元着色器的顶点位置
void main()  {
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vColor = aColor;//将接收的颜色传递给片元着色器
   vPosition=(uMMatrix * vec4(aPosition,1)).xyz;//计算出此顶点变换后的位置传递给片元着色器
}