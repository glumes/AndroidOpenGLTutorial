precision mediump float;
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
void main()
{//绘制球本身，纹理从球纹理采样
	vec4 finalColor=vec4(1.0,1.0,1.0,0.0);//物体颜色
	gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;//给此片元颜色值
}