#version 330
#extension GL_ARB_explicit_uniform_location : enable

layout (location = 0) uniform sampler2D uDiffuseTexture;
layout (location = 1) uniform sampler2D uProperties;
uniform vec3 uWorldPos;

in vec2 vTextureUV;
in vec4 vVertexColor;
in vec3 vLocalOffset;

out vec4 color;

void main() {
	vec4 textureColor = texture(uDiffuseTexture, vTextureUV);
	vec4 properties = texture(uProperties, vTextureUV);
	color = textureColor * vVertexColor;
}