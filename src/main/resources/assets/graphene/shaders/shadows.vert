#version 120

varying out vec3 position;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = vec3(0.5, 0.5, 0.005);//gl_Position.xyz;//gl_Position.y;
}