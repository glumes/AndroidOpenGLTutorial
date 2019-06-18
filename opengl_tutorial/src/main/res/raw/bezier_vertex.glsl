attribute float aData;

uniform vec4 uStartEndData;
uniform vec4 uControlData;

uniform mat4 u_MVPMatrix;

uniform float u_Amp;


vec2 fun(in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p3, in float t){
    float tt = (1.0 - t) * (1.0 -t);
    return tt * (1.0 -t) *p0 +
    3.0 * t * tt * p1 +
    3.0 * t *t *(1.0 -t) *p2 + t *t *t *p3;
}

vec2 fun2(in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p3, in float t)
{
    vec2 q0 = mix(p0, p1, t);
    vec2 q1 = mix(p1, p2, t);
    vec2 q2 = mix(p2, p3, t);

    vec2 r0 = mix(q0, q1, t);
    vec2 r1 = mix(q1, q2, t);

    return mix(r0, r1, t);
}


void main() {

    vec4 pos;
    pos.w = 1.0;

    vec2 p0 = uStartEndData.xy;
    vec2 p3 = uStartEndData.zw;

    vec2 p1 = uControlData.xy;
    vec2 p2 = uControlData.zw;


    p0.y *= u_Amp;
    p1.y *= u_Amp;
    p2.y *= u_Amp;
    p3.y *= u_Amp;

    float t = aData;

    vec2 point = fun2(p0, p1, p2, p3, t);

    pos.xy = point;

    gl_Position = pos;

    gl_PointSize = 10.0;
}

