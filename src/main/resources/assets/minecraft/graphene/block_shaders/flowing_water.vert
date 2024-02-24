void main(VertexData data, inout vec3 vertex) {
	if (fract(data.localPos.y) < 0.01) {
		vertex = data.localPos;
	}
}