precision mediump float ;

uniform vec4 u_Color;
varying vec4 v_Color;

void main() {

//    gl_FragColor = v_Color;
    gl_FragColor = u_Color;

}
