#version 300 es
precision mediump float;
//接收从顶点着色器过来的参数
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
out vec4 fragColor;//输出的片元颜色
void main()
{
   //将计算出的颜色给此片元
   vec4 finalColor=vec4(1.0,1.0,1.0,1.0);
   fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;//给此片元颜色值
}