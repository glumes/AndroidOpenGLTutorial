//#version 120
//
//void main() {
//
//}

precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform sampler2D sTexture1;//纹理内容数据
uniform sampler2D sTexture2;

uniform float progress;
uniform vec2 direction;


//const vec2 direction = vec2(0.0,1.0);
//const float progress = 0.5;

void main()
{
    //给此片元从纹理中采样出颜色值

    vec2 p = vTextureCoord + progress * sign(direction);

    float m = step(0.0,p.y) * step(p.y,1.0) * step(0.0,p.x) * step(p.x,1.0);

    vec4 color = mix(texture2D(sTexture1,vTextureCoord),texture2D(sTexture2,vTextureCoord),m);

    gl_FragColor = vec4(color);

}