vec2 random2(vec2 pos) {
	vec2 s = vec2(
		dot(pos, vec2(127.1, 311.7)),
		dot(pos, vec2(269.5, 183.3))
	);
	return -1.0 + 2.0 * fract(sin(s) * 43758.5453123);
}

vec4 gradientNoise(vec2 pos) {
	vec2 i = floor(pos);
	vec2 f = fract(pos);

	float f11 = dot(random2(i + vec2(0.0, 0.0)), f - vec2(0.0, 0.0));
	float f12 = dot(random2(i + vec2(0.0, 1.0)), f - vec2(0.0, 1.0));
	float f21 = dot(random2(i + vec2(1.0, 0.0)), f - vec2(1.0, 0.0));
	float f22 = dot(random2(i + vec2(1.0, 1.0)), f - vec2(1.0, 1.0));

	return vec4(f11, f12, f21, f22);
}

float interpolate(float t) {
	return t * t * t * (10.0 + t * (6.0 * t - 15.0));
}

float noise(vec2 pos) {
	vec4 v = gradientNoise(pos);
	vec2 f = fract(pos);
	float t = interpolate(f.x);
	float u = interpolate(f.y);
	return mix(mix(v.x, v.z, t), mix(v.y, v.w, t), u) * 0.5 + 0.5;
}

void main(FragmentData data, inout vec4 color) {
	vec2 wPos = floor(data.worldPos.xz * 16.0) / 16.0;
	float n = noise(wPos);
	n += noise(wPos.yx * 3.0) * 0.3;
	n += noise(wPos * 7.0) * 0.1;
	n = clamp(floor(n * 10.0) / 10.0, 0.0, 1.0);
	color = vec4(mix(vec3(0.2), vec3(0.8), n), 1.0);
	color.rgb = mix(color.rgb * data.vertexColor.rgb, data.fog.rgb, data.fog.a);
}