package paulevs.graphene.rendering.fbo;

public class ShadowMap /*implements Disposable*/ {
	/*private static final int LIGHTMAP_SCALE = 16; // side pixels per tile
	private final Vec2I minChunk = new Vec2I();
	private final Vec2I maxChunk = new Vec2I();
	private final Vec2I minPos = new Vec2I();
	private final Vec2I maxPos = new Vec2I();
	private final Vec2I size = new Vec2I();
	private final Texture2D texture;
	private final Texture2D light;
	private final int fbo;
	
	private boolean disposed;
	
	public ShadowMap() {
		texture = new Texture2D();
		texture.setData(null, 1, 1);
		texture.setFilter(GL11.GL_LINEAR);
		fbo = GL30.glGenFramebuffers();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getTarget(), 0);
		
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Can't create a FrameBuffer for a Light Map: " + status);
		}
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		DisposeUtil.add(this);
		
		light = new Texture2D();
		int[] lightPixels = new int[4096];
		for (byte x = 0; x < 64; x++) {
			float x2 = x - 31.5F;
			x2 *= x2;
			for (byte y = 0; y < 64; y++) {
				float y2 = y - 31.5F;
				y2 *= y2;
				float d = x2 + y2;
				if (d > 1024) continue;
				d = 1F - MathHelper.sqrt(d) / 31.5F;
				d = MathHelper.clamp(d, 0F, 1F);
				int rgb = MathHelper.floor(d * 255);
				lightPixels[y << 6 | x] = ColorHelper.pack(255, rgb, rgb, rgb);
			}
		}
		light.setData(lightPixels, 64, 64);
		light.setFilter(GL11.GL_LINEAR);
	}
	
	public void start(Vec2I minChunk, Vec2I maxChunk, Camera camera, GUIElement screen) {
		if (!minChunk.equals(this.minChunk) || !maxChunk.equals(this.maxChunk)) {
			this.minChunk.set(minChunk);
			this.maxChunk.set(maxChunk);
			minPos.set(minChunk.x << 3, minChunk.y << 3);//.sub(8, 8);
			maxPos.set(maxChunk.x << 3, maxChunk.y << 3).add(8, 8);//.add(16, 16);
			size.set(maxPos).sub(minPos);
			int dx = maxPos.x - minPos.x;
			int dy = maxPos.y - minPos.y;
			texture.setData(null, dx * LIGHTMAP_SCALE, dy * LIGHTMAP_SCALE);
		}
		
		float scale = 0.5F / camera.getZoom();
		double px = camera.getPosX() - screen.getWidth() * scale;
		double py = camera.getPosY() - screen.getHeight() * scale;
		RenderHelper.lightmapMin.setValue(getPosX(px), getPosY(py));
		
		px = camera.getPosX() + screen.getWidth() * scale;
		py = camera.getPosY() + screen.getHeight() * scale;
		RenderHelper.lightmapMax.setValue(getPosX(px), getPosY(py));
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		RenderHelper.initOrtho(texture.getWidth(), texture.getHeight());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL15.glBlendEquation(GL15.GL_MAX);
		light.bind();
		RenderHelper.defaultProgram.bind();
		GL11.glBegin(GL11.GL_QUADS);
	}
	
	public void end() {
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public Texture2D getTexture() {
		return texture;
	}
	
	public float getPosX(double x) {
		return (float) (x - minPos.x) / size.x;
	}
	
	public float getPosY(double y) {
		return 1.0F - (float) (y - minPos.y) / size.y;
	}
	
	public void renderLight(double x, double y, LightInfo info) {
		int diameter = ((info.getRadius() << 1) | 1) * LIGHTMAP_SCALE;
		int dr = info.getRadius() * LIGHTMAP_SCALE;
		int rgb = info.getColor();
		GL11.glColor3f(
			ColorHelper.getR(rgb) / 255F,
			ColorHelper.getG(rgb) / 255F,
			ColorHelper.getB(rgb) / 255F
		);
		RenderHelper.quadSequence(
			MathHelper.round((float) (x - minPos.x) * LIGHTMAP_SCALE - dr),
			MathHelper.round((float) (y - minPos.y) * LIGHTMAP_SCALE - dr),
			0, diameter, diameter, 0, 0, 1, 1
		);
	}
	
	@Override
	public void dispose() {
		if (disposed) return;
		GL30.glDeleteFramebuffers(fbo);
		disposed = true;
	}*/
}
