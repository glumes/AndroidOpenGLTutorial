attribute vec4 aPosition;
uniform mat4 uMVPMatrix;
attribute vec2 aTextureCoord;
attribute vec3 aMipmapCoord;
varying vec2 textureCoordinate;
varying vec2 mipmapCoordinate;

void main(){

    gl_Position = aPosition;

    textureCoordinate = aTextureCoord;

    mipmapCoordinate = (uMVPMatrix * vec4(aMipmapCoord,1)).xy;
}