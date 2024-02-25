#version 130
#extension GL_ARB_explicit_uniform_location : enable

uniform sampler2D uProperties;
uniform sampler2D uWindMap;

uniform mat4 uProjection;
uniform mat4 uModelView;
uniform vec3 uCamepaPos;

uniform float uTime;
uniform vec3 uWorldPos;
uniform vec2 uFogParams;
uniform vec3 uFogColor;

out vec2 vTextureUV;
out vec4 vVertexColor;
out vec3 vLocalOffset;
out vec3 vScreenPos;
out vec4 vFog;

flat out int vBlockID;

vec2 getWindHorizontal(vec2 pos) {
	vec2 delta = 1.0 / vec2(textureSize(uWindMap, 0));
	vec2 dx = vec2(delta.x, 0.0);
	vec2 dy = vec2(0.0, delta.y);
	float vx = texture(uWindMap, pos + dx).r - texture(uWindMap, pos - dx).r;
	float vy = texture(uWindMap, pos + dy).r - texture(uWindMap, pos - dy).r;
	return vec2(vx, vy);
}

vec3 getWindIntensity(vec4 properties) {
	int data = int(properties.r * 255.0);
	float intensity = float(data & 15) / 15.0 * 0.5;
	float vertical = float((data >> 4) & 15) / 15.0;
	vec3 result = mix(vec3(0.0, 1.0, 0.0), vec3(1.0, 0.0, 1.0), vertical);
	return normalize(result) * intensity;
}

vec3 getWind(vec3 worldPos, vec4 properties) {
	float windTimeV = uTime * 6.2831853 * 7.0;
	float windTimeH = uTime * 40.0;
	vec3 intensity = getWindIntensity(properties);
	vec2 windPos1 = vec2((worldPos.x + worldPos.z) * 0.01, (worldPos.z - worldPos.x) * 0.01 + windTimeV);
	vec2 windPos2 = vec2((worldPos.x - worldPos.z) * 0.01, (worldPos.z + worldPos.x) * 0.01 + windTimeH);
	vec2 hor = getWindHorizontal(windPos1) * 20.0;
	float ver = texture(uWindMap, windPos2).r * 3.0 - 1.5;
	return vec3(hor.x, ver, hor.y) * intensity;
}

int getBlockID() {
	int a = int(gl_MultiTexCoord1.t);
	int b = int(gl_MultiTexCoord1.s);
	return a << 16 | b;
}

vec4 getFog() {
	float fogDensity = 0.0;
	if (uFogParams.y == 0.0) {
		fogDensity = 1.0 - 1.0 / exp(vScreenPos.z * uFogParams.x);
	}
	else {
		fogDensity = clamp((vScreenPos.z - uFogParams.x) / (uFogParams.y - uFogParams.x), 0.0, 1.0);
	}
	return vec4(uFogColor, fogDensity);
}

struct VertexData {
	vec3 worldPos;
	vec3 localPos;
	vec4 properties;
};

// INJECT_BLOCK_FUNCTIONS

void main() {
	vec4 properties = texture(uProperties, gl_MultiTexCoord0.st);
	vec3 worldPos = gl_Vertex.xyz + uWorldPos;
	vec3 wind = getWind(worldPos, properties);

	vTextureUV = gl_MultiTexCoord0.st;
	vVertexColor = gl_Color;

	VertexData data = VertexData(worldPos, gl_Vertex.xyz, properties);
	vec3 vertex = gl_Vertex.xyz + wind;
	int blockID = getBlockID();

	// INJECT_BLOCK_SWITCH

	gl_Position = uProjection * uModelView * vec4(vertex + uWorldPos - uCamepaPos, 1.0);
	vScreenPos = gl_Position.xyz;
	vLocalOffset = vertex;
	vBlockID = blockID;
	vFog = getFog();
}