#version 330

uniform sampler2D textureAtlas;

in vec2 textureUV;
in vec4 vertexColor;
in vec3 worldPos;

out vec4 color;

void main() {
	vec4 textureColor = texture(textureAtlas, textureUV);
	color = textureColor * vertexColor;
	color.rgb = worldPos / 16.0;
}