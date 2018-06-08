uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
varying vec3 vPosition;  //顶点位置
void main() {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vPosition=aPosition;//将顶点的原始位置传递给片元着色器
}