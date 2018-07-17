#version 300 es
precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
in vec2 vTextureCoord;//接收从顶点着色器过来的参数
out vec4 fragColor;
void main()
{
   //给此片元从纹理中采样出颜色值
   fragColor = texture(sTexture, vTextureCoord);
}