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
flat in int vBlockID;

out vec4 color;

struct FragmentData {
	vec4 textureColor;
	vec4 vertexColor;
	vec4 properties;
	vec4 fog;
	vec3 worldPos;
	vec3 localOffset;
	vec3 screenPos;
};

// INJECT_BLOCK_FUNCTIONS

void main() {
	vec4 textureColor = texture(uDiffuseTexture, vTextureUV);
	vec4 properties = texture(uProperties, vTextureUV);
	vec4 fog = vec4(vFogColor, vFogDensity);

	vec3 worldPos = vLocalOffset + uWorldPos;
	vec4 blockColor = textureColor * vVertexColor;
	blockColor.rgb = mix(blockColor.rgb, vFogColor, vFogDensity);
	FragmentData data = FragmentData(textureColor, vVertexColor, properties, fog, worldPos, vLocalOffset, vScreenPos);

	// INJECT_BLOCK_SWITCH

	color = blockColor;
}