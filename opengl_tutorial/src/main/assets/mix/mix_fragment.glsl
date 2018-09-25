precision mediump float;

varying vec2 textureCoordinate;
varying vec2 mipmapCoordinate;

uniform sampler2D inputTexture;
uniform sampler2D mipmapTexture;

void main(){

    lowp vec4 sourceColor = texture2D(inputTexture,1.0-textureCoordinate);

    lowp vec4 mipmapColor = texture2D(mipmapTexture,mipmapCoordinate);

    gl_FragColor = mipmapColor * mipmapColor.a + sourceColor * (1.0 - mipmapColor.a);

}