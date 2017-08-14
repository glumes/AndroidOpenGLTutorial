attribute vec4 a_Position ;

uniform mat4 u_Matrix;

varying vec4 v_Color;

void main(){

    gl_Position = u_Matrix * a_Position;

    float color;

    if(a_Position.z>0.0){

        color= a_Position.z;

    }else{

        color=- a_Position.z;
    }

    v_Color = vec4(color,color,color,1.0);
}