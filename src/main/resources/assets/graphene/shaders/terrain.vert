#version 130

uniform sampler2D uProperties;

uniform float uTime;
uniform vec3 uWorldPos;

varying vec2 vTextureUV;
varying vec4 vVertexColor;
varying vec3 vLocalOffset;

const vec3[] colors = vec3[4] (
	vec3(1.0, 0.0, 0.0),
	vec3(0.0, 1.0, 0.0),
	vec3(0.0, 0.0, 1.0),
	vec3(1.0, 0.0, 1.0)
);

vec3 getWindIntensity(vec4 properties) {
	int data = int(properties.r * 255.0);
	float intensity = float(data & 15) / 15.0 * 0.5;
	float vertical = float((data >> 4) & 15) / 15.0;
	vec3 result = mix(vec3(0.0, 1.0, 0.0), vec3(1.0, 0.0, 1.0), vertical);
	return normalize(result) * intensity;
}

vec3 getWind(vec3 worldPos, vec4 properties) {
	float windTime = uTime / 24000.0 * 6.2831853 * 1000.0;
	return sin(worldPos.yzx + windTime) * getWindIntensity(properties);
}

void main() {
	vLocalOffset = gl_Vertex.xyz;
	vec4 properties = texture(uProperties, gl_MultiTexCoord0.st);
	vec3 worldPos = vLocalOffset + uWorldPos;
	vec3 wind = getWind(worldPos, properties);
	gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + vec4(wind, 0.0));
	vTextureUV = gl_MultiTexCoord0.st;
	vVertexColor = gl_Color;
}