#version 330
#extension GL_ARB_explicit_uniform_location : enable

layout (location = 0) uniform sampler2D uDiffuseTexture;
uniform sampler2D uProperties;

uniform vec3 uWorldPos;

in vec2 vTextureUV;
in vec4 vVertexColor;
in vec3 vLocalOffset;
in vec3 vScreenPos;
in vec3 vFogColor;
in float vFogDensity;

out vec4 color;

void main() {
	vec4 textureColor = texture(uDiffuseTexture, vTextureUV);
	vec4 properties = texture(uProperties, vTextureUV);
	color = textureColor * vVertexColor;
	color.rgb = mix(color.rgb, vFogColor, vFogDensity);
}