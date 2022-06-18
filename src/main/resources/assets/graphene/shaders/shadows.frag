#version 130

in vec3 position;

void main() {
	gl_FragDepth = 0.5;//position.z;//0.5;
}