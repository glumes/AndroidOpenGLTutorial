attribute float aData;

uniform vec4 uStartEndData;
uniform vec4 uControlData;

uniform vec2 uStartPoint;
uniform vec2 uEndPoint;
uniform vec2 uControlPoint1;
uniform vec2 uControlPoint2;

uniform mat4 u_MVPMatrix;

uniform float u_Amp;


vec2 fun(in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p3, in float t){
    float tt = (1.0 - t) * (1.0 -t);
    return tt * (1.0 -t) *p0 +
    3.0 * t * tt * p1 +
    3.0 * t *t *(1.0 -t) *p2 + t *t *t *p3;
}

// 三阶贝塞尔
vec2 fun2(in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p3, in float t)
{
    vec2 q0 = mix(p0, p1, t);
    vec2 q1 = mix(p1, p2, t);
    vec2 q2 = mix(p2, p3, t);

    vec2 r0 = mix(q0, q1, t);
    vec2 r1 = mix(q1, q2, t);

    return mix(r0, r1, t);
}

// 二阶贝塞尔
vec2 fun3(in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p3, in float t)
{

    float tt = (1.0 - t) * (1.0 -t);

    return tt * p0 + 2.0 * t * (1.0 -t) * p1 + t * t * p2;

}


//(1-t)^2P0 + 2(1-t)tP1 + t^2*P2
//(1-t)^3P0 + 3(1-t)^2tP1 + 3(1-t)t^2P2 + t^3*P3


void main() {

    vec4 pos;
    pos.w = 1.0;

//    vec2 p0 = uStartEndData.xy;
//    vec2 p3 = uStartEndData.zw;
//
//    vec2 p1 = uControlData.xy;
//    vec2 p2 = uControlData.zw;


    vec2 p0 = uStartPoint;
    vec2 p3 = uEndPoint;
    vec2 p1 = uControlPoint1;
    vec2 p2 = uControlPoint2;

    p0.y *= u_Amp;
    p1.y *= u_Amp;
    p2.y *= u_Amp;
    p3.y *= u_Amp;

    float t = aData;

    vec2 point = fun3(p0, p1, p2, p3, t);

    pos.xy = point;

    gl_Position = u_MVPMatrix * pos;

    gl_PointSize = 20.0;
}

