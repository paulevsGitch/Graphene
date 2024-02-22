#version 120

varying vec2 textureUV;
varying vec4 vertexColor;
varying vec3 worldPos;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	textureUV = gl_MultiTexCoord0.st;
	vertexColor = gl_Color;
	worldPos = gl_Vertex.xyz;
}