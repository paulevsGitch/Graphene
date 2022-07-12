package paulevs.graphene.storage;

import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vector4f;
import paulevs.bhcore.storage.vector.Vec2F;
import paulevs.bhcore.storage.vector.Vec3F;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class QuadData {
	private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(32 << 2).order(ByteOrder.nativeOrder());
	private static final VertexInfo[] VERTEX = new VertexInfo[] { new VertexInfo(), new VertexInfo(), new VertexInfo(), new VertexInfo() };
	private static final Vector4f VECTOR4F = new Vector4f();
	private static boolean apply;
	
	public static VertexInfo getInfo(int vertex) {
		return VERTEX[vertex];
	}
	
	public static boolean canApply() {
		boolean a = apply;
		apply = false;
		return a;
	}
	
	public static void fillData(int[] vertexData, Direction face) {
		BUFFER.rewind();
		for (int i: vertexData) BUFFER.putInt(i);
		for (byte i = 0; i < 4; i++) {
			Vec3F position = VERTEX[i].position;
			short index = (short) (i << 5);
			position.x = BUFFER.getFloat(index);
			position.y = BUFFER.getFloat(index | 4);
			position.z = BUFFER.getFloat(index | 8);
			
			Vec2F uv = VERTEX[i].uv;
			uv.x = BUFFER.getFloat(index | 16);
			uv.y = BUFFER.getFloat(index | 20);
			
			VERTEX[i].normal.set(face.vector.x, face.vector.y, face.vector.z);
		}
		apply = true;
	}
	
	public static Vector4f getPos4F(int index) {
		Vec3F position = getInfo(index).position;
		VECTOR4F.set(position.x, position.y, position.z, 1.0F);
		return VECTOR4F;
	}
	
	public static class VertexInfo {
		public final Vec3F position = new Vec3F();
		public final Vec3F light = new Vec3F();
		public final Vec3F normal = new Vec3F();
		public final Vec2F uv = new Vec2F();
	}
}
