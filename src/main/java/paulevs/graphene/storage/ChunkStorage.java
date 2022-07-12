package paulevs.graphene.storage;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import paulevs.bhcore.storage.section.SectionDataHandler;
import paulevs.bhcore.storage.section.arrays.IntArraySectionData;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;
import paulevs.graphene.Graphene;

public class ChunkStorage {
	private static final int LIGHT_DATA = SectionDataHandler.register(Graphene.id("color"), () -> new IntArraySectionData(4096));
	private static final Vec3F[] INTERPOLATION_CELL = new Vec3F[8];
	private static IntArraySectionData lastData;
	private static Vec3I lastPos = new Vec3I();
	
	static {
		for (byte i = 0; i < 8; i++) INTERPOLATION_CELL[i] = new Vec3F();
	}
	
	public static IntArraySectionData getSectionLight(ChunkSection section) {
		return SectionDataHandler.getData(section, LIGHT_DATA);
	}
	
	public static void getLight(Vec3F out, Level level, double x, double y, double z) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		int z1 = MathHelper.floor(z);
		float dx = (float) (x - x1);
		float dy = (float) (y - y1);
		float dz = (float) (z - z1);
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;
		
		getLight(INTERPOLATION_CELL[0], level, x1, y1, z1);
		getLight(INTERPOLATION_CELL[1], level, x2, y1, z1);
		getLight(INTERPOLATION_CELL[2], level, x1, y1, z2);
		getLight(INTERPOLATION_CELL[3], level, x2, y1, z2);
		getLight(INTERPOLATION_CELL[4], level, x1, y2, z1);
		getLight(INTERPOLATION_CELL[5], level, x2, y2, z1);
		getLight(INTERPOLATION_CELL[6], level, x1, y2, z2);
		getLight(INTERPOLATION_CELL[7], level, x2, y2, z2);
		
		INTERPOLATION_CELL[0].lerp(INTERPOLATION_CELL[1], dx);
		INTERPOLATION_CELL[2].lerp(INTERPOLATION_CELL[3], dx);
		INTERPOLATION_CELL[4].lerp(INTERPOLATION_CELL[5], dx);
		INTERPOLATION_CELL[6].lerp(INTERPOLATION_CELL[7], dx);
		
		INTERPOLATION_CELL[0].lerp(INTERPOLATION_CELL[2], dz);
		INTERPOLATION_CELL[4].lerp(INTERPOLATION_CELL[6], dz);
		
		out.set(INTERPOLATION_CELL[0].lerp(INTERPOLATION_CELL[4], dy));
	}
	
	public static void getLight(Vec3F out, Level level, int x, int y, int z) {
		int rgb = getLight(level, x, y, z);
		out.x = (rgb >> 16 & 255) / 255F;
		out.y = (rgb >> 8 & 255) / 255F;
		out.z = (rgb & 255) / 255F;
	}
	
	public static int getLight(Level level, int x, int y, int z) {
		HeightLimitView limit = (HeightLimitView) level;
		int sectionY = (y - limit.getBottomY()) >> 4;
		if (sectionY < 0) return 0;
		int sectionX = x >> 4;
		int sectionZ = z >> 4;
		
		if (lastData == null || sectionX != lastPos.x || sectionX != lastPos.y || sectionX != lastPos.z) {
			Chunk chunk = level.getChunkFromCache(sectionX, sectionZ);
			ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
			ChunkSection section = sections[sectionY];
			if (section == null) return 0;
			lastData = ChunkStorage.getSectionLight(section);
			lastPos.set(sectionX, sectionY, sectionZ);
		}
		
		return lastData.getData(MathUtil.getIndex16(x & 15, y & 15, z & 15));
	}
	
	public static void setLight(Level level, int x, int y, int z, int light) {
		HeightLimitView limit = (HeightLimitView) level;
		int sectionY = (y - limit.getBottomY()) >> 4;
		if (sectionY < 0) return;
		int sectionX = x >> 4;
		int sectionZ = z >> 4;
		
		if (lastData == null || sectionX != lastPos.x || sectionX != lastPos.y || sectionX != lastPos.z) {
			Chunk chunk = level.getChunkFromCache(sectionX, sectionZ);
			ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
			ChunkSection section = sections[sectionY];
			if (section == null) {
				section = new ChunkSection(y >> 4);
				sections[sectionY] = section;
			}
			lastData = ChunkStorage.getSectionLight(section);
			lastPos.set(sectionX, sectionY, sectionZ);
		}
		
		lastData.setData(MathUtil.getIndex16(x & 15, y & 15, z & 15), light);
	}
	
	public static void init() {}
}
