#version 430
out vec4 color;
void main(void) 
{ color = vec4(gl_FragCoord.y/255, gl_FragCoord.x/255, gl_FragCoord.x/255, 1.0); }
