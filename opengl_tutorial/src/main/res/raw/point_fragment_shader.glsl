precision mediump float;


//#ifdef GL_OES_standard_derivatives
//#extension GL_OES_standard_derivatives : enable
//#endif

uniform vec4 u_Color;

void main()
{

//    float dist = length(gl_PointCoord - vec2(0.5));
//
//    float value = -smoothstep(0.5,0.5,dist) + 1.0;
//
//    if(value == 0.0){
//        discard;
//    }
//
//    gl_FragColor = u_Color;

//    float r = 0.0 , delta = 0.0 , alpha = 1.0;
//    vec2 cxy = 2.0 * gl_PointCoord - 1.0;
//    r = dot(cxy,cxy);
//    if(r > 1.0){
//        discard;
//    }
//    gl_FragColor = u_Color * (alpha);

    float r = 0.0 , delta = 0.0 , alpha = 1.0;
    vec2 cxy = 2.0 * gl_PointCoord - 1.0;
    r = dot(cxy,cxy);

//    #ifdef GL_OES_standard_derivatives
//        delta = fwidth(r);
//        alpha = 1.0 - smoothstep(1.0 - delta, 1.0 + delta, r);
//    #endif

    alpha = 1.0 - smoothstep(0.85, 1.0, r);

    gl_FragColor = color * alpha;
}