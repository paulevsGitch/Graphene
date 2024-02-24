package paulevs.graphene.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import paulevs.graphene.Graphene;
import paulevs.graphene.rendering.disposing.DisposeUtil;
import paulevs.graphene.rendering.shaders.Programs;
import paulevs.graphene.rendering.shaders.ShaderProgram;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

@Environment(EnvType.CLIENT)
public class PropertyAtlas {
	private static BufferedImage atlasImage;
	private static Graphics graphics;
	private static int texture = 0;
	
	public static void init(int width, int height) {
		atlasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = atlasImage.getGraphics();
	}
	
	public static void addSprite(Identifier id, Sprite sprite) {
		String path = "/assets/" + id.namespace + "/stationapi/textures/" + id.path + "_properties.png";
		BufferedImage texture;
		try {
			InputStream stream = PropertyAtlas.class.getResourceAsStream(path);
			if (stream == null) return;
			texture = ImageIO.read(stream);
			stream.close();
		}
		catch (IOException e) {
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
			return;
		}
		int x = sprite.getX();
		int y = sprite.getY();
		int w = sprite.getContents().getWidth();
		int h = sprite.getContents().getHeight();
		graphics.drawImage(texture, x, y, w, h, null);
	}
	
	public static void upload() {
		if (texture == 0) {
			texture = GL11.glGenTextures();
			DisposeUtil.add(() -> GL11.glDeleteTextures(texture));
		}
		Programs.TERRAIN_PROGRAM.bind();
		Programs.TERRAIN_PROPERTIES.setTexture(texture);
		Programs.TERRAIN_PROPERTIES.bind();
		int[] pixels = ((DataBufferInt) atlasImage.getRaster().getDataBuffer()).getData();
		IntBuffer buffer = ByteBuffer.allocateDirect(pixels.length << 4).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
		buffer.put(pixels);
		buffer.position(0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(
			GL11.GL_TEXTURE_2D,
			0,
			GL11.GL_RGBA,
			atlasImage.getWidth(),
			atlasImage.getHeight(),
			0,
			GL12.GL_BGRA,
			GL11.GL_UNSIGNED_BYTE,
			buffer
		);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		int texture = Graphene.getMinecraft().textureManager.getTextureId("/assets/graphene/textures/wind_map.png");
		Programs.TERRAIN_WIND.setTexture(texture);
		Programs.TERRAIN_WIND.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		ShaderProgram.unbind();
	}
}
