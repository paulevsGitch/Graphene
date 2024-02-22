package paulevs.graphene.rendering.shaders.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import paulevs.graphene.rendering.disposing.Disposable;
import paulevs.graphene.rendering.disposing.DisposeUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

@Environment(EnvType.CLIENT)
public class Texture2D implements Disposable {
	private final int target;
	private boolean disposed;
	private int width;
	private int height;
	
	public Texture2D() {
		target = GL11.glGenTextures();
		setWrap(GL12.GL_CLAMP_TO_EDGE);
		setFilter(GL11.GL_NEAREST);
		DisposeUtil.add(this);
	}
	
	public void bind() {
		bind(0);
	}
	
	public void bind(int layer) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + layer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, target);
	}
	
	public int getTarget() {
		return target;
	}
	
	public void setFilter(int filter) {
		bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
	}
	
	public void setWrap(int wrap) {
		bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap);
	}
	
	public void setData(int[] data, int width, int height) {
		this.width = width;
		this.height = height;
		bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, IntBuffer.wrap(data));
	}
	
	public void setData(int[] data, int x, int y, int width, int height) {
		bind();
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, IntBuffer.wrap(data));
	}
	
	public void setData(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] data;
		
		if (img.getType() == BufferedImage.TYPE_INT_ARGB) {
			data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		}
		else {
			data = new int[width * height];
			img.getRGB(0, 0, width, height, data, 0, width);
		}
		
		setData(data, width, height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public void dispose() {
		if (disposed) return;
		GL11.glDeleteTextures(target);
		disposed = true;
	}
}
