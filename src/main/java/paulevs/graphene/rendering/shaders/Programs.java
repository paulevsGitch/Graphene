package paulevs.graphene.rendering.shaders;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.graphene.rendering.shaders.uniforms.FloatUniform;
import paulevs.graphene.rendering.shaders.uniforms.TextureUniform;
import paulevs.graphene.rendering.shaders.uniforms.Vec2FUniform;
import paulevs.graphene.rendering.shaders.uniforms.Vec3FUniform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Programs {
	public static final ShaderProgram TERRAIN_PROGRAM = loadTerrainProgram();
	
	public static final FloatUniform TERRAIN_TIME = TERRAIN_PROGRAM.getUniform("uTime", FloatUniform::new);
	public static final Vec3FUniform TERRAIN_POS = TERRAIN_PROGRAM.getUniform("uWorldPos", Vec3FUniform::new);
	public static final TextureUniform TERRAIN_PROPERTIES = TERRAIN_PROGRAM.getUniform("uProperties", TextureUniform::new);
	public static final TextureUniform TERRAIN_WIND = TERRAIN_PROGRAM.getUniform("uWindMap", TextureUniform::new);
	public static final Vec2FUniform TERRAIN_FOG_PARAMS = TERRAIN_PROGRAM.getUniform("uFogParams", Vec2FUniform::new);
	public static final Vec3FUniform TERRAIN_FOG_COLOR = TERRAIN_PROGRAM.getUniform("uFogColor", Vec3FUniform::new);
	
	public static void init() {}
	
	private static ShaderProgram loadTerrainProgram() {
		List<String> blockShaders = new ArrayList<>();
		List<String> switchStates = new ArrayList<>();
		
		switchStates.add("\tswitch (blockID) {");
		BlockRegistry.INSTANCE.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id == null) return;
			String pathVert = "/assets/" + id.namespace + "/graphene/block_shaders/" + id.path + ".vert";
			InputStream streamVert = Programs.class.getResourceAsStream(pathVert);
			if (streamVert == null) return;
			List<String> blockShader = load(streamVert);
			String funcName = id.toString().replace(":", "_") + "_main";
			for (int i = 0; i < blockShader.size(); i++) {
				if (!blockShader.get(i).contains(" main(")) continue;
				String line = blockShader.get(i).replace(" main(", " " + funcName + "(");
				blockShader.set(i, line);
				break;
			}
			blockShaders.addAll(blockShader);
			switchStates.add("\t\tcase " + block.id + ": " + funcName + "(data, vertex); break;");
		});
		switchStates.add("\t}");
		
		System.out.println("Loaded " + (switchStates.size() - 2) + " block vertex shaders");
		
		String path = "/assets/graphene/shaders/terrain.vert";
		InputStream stream = Programs.class.getResourceAsStream(path);
		List<String> shaderCode = load(stream);
		
		if (switchStates.size() > 2) {
			for (int i = 0; i < shaderCode.size(); i++) {
				if (!shaderCode.get(i).trim().equals("// INJECT_BLOCK_FUNCTIONS")) continue;
				shaderCode.addAll(i + 1, blockShaders);
				break;
			}
		
			for (int i = 0; i < shaderCode.size(); i++) {
				if (!shaderCode.get(i).trim().equals("// INJECT_BLOCK_SWITCH")) continue;
				shaderCode.addAll(i + 1, switchStates);
				break;
			}
		}
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			System.out.println("Generated Vertex");
			System.out.println("=========");
			shaderCode.forEach(System.out::println);
			System.out.println("=========");
		}
		
		Shader vertex = new Shader(listToString(shaderCode), ShaderType.VERTEX);
		
		blockShaders.clear();
		switchStates.clear();
		
		switchStates.add("\tswitch (vBlockID) {");
		BlockRegistry.INSTANCE.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id == null) return;
			String pathFrag = "/assets/" + id.namespace + "/graphene/block_shaders/" + id.path + ".frag";
			InputStream streamFrag = Programs.class.getResourceAsStream(pathFrag);
			if (streamFrag == null) return;
			List<String> blockShader = load(streamFrag);
			String funcName = id.toString().replace(":", "_") + "_main";
			for (int i = 0; i < blockShader.size(); i++) {
				if (!blockShader.get(i).contains(" main(")) continue;
				String line = blockShader.get(i).replace(" main(", " " + funcName + "(");
				blockShader.set(i, line);
				break;
			}
			blockShaders.addAll(blockShader);
			switchStates.add("\t\tcase " + block.id + ": " + funcName + "(data, blockColor); break;");
		});
		switchStates.add("\t}");
		
		System.out.println("Loaded " + (switchStates.size() - 2) + " block fragment shaders");
		
		path = "/assets/graphene/shaders/terrain.frag";
		stream = Programs.class.getResourceAsStream(path);
		shaderCode = load(stream);
		
		if (switchStates.size() > 2) {
			for (int i = 0; i < shaderCode.size(); i++) {
				if (!shaderCode.get(i).trim().equals("// INJECT_BLOCK_FUNCTIONS")) continue;
				shaderCode.addAll(i + 1, blockShaders);
				break;
			}
			
			for (int i = 0; i < shaderCode.size(); i++) {
				if (!shaderCode.get(i).trim().equals("// INJECT_BLOCK_SWITCH")) continue;
				shaderCode.addAll(i + 1, switchStates);
				break;
			}
		}
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			System.out.println("Generated Fragment");
			System.out.println("=========");
			shaderCode.forEach(System.out::println);
			System.out.println("=========");
		}
		
		Shader fragment = new Shader(listToString(shaderCode), ShaderType.FRAGMENT);
		
		return new ShaderProgram(vertex, fragment);
	}
	
	private static String listToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (String line : list) {
			builder.append(line);
			builder.append('\n');
		}
		return builder.toString();
	}
	
	private static List<String> load(InputStream stream) {
		List<String> list = new ArrayList<>();
		String line;
		
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		
		try {
			while ((line = bufferedReader.readLine()) != null) list.add(line);
			bufferedReader.close();
			streamReader.close();
			stream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return list;
	}
}