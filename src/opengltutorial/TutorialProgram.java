package opengltutorial;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.lwjgl.opengl.Display;

public abstract class TutorialProgram {
	
	protected void destroy(){
		Display.destroy();
		System.exit(0);
	}
	
	// Load a shader of the type type from the file sourceFile into OpenGL
	protected int loadShader(String sourceFile) {
		int shaderType;
		
		String[] splitFile = sourceFile.split("\\.");
		switch(splitFile[splitFile.length-1]) {
			case "vert": shaderType = GL_VERTEX_SHADER; break;
			case "frag": shaderType = GL_FRAGMENT_SHADER; break;
			default: shaderType = -1;
		}
		
		int shaderId = glCreateShader(shaderType);
		glShaderSource(shaderId, readFromFile(sourceFile));
		
		glCompileShader(shaderId);
		
		if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("An error occured, while compiling the shader \"" + sourceFile + "\"");
			System.err.println("ErrorLog:");
			System.err.println(glGetShaderInfoLog(shaderId, GL_INFO_LOG_LENGTH));
		}
		
		return shaderId;
	}
	
	// Bind multiple shaders to a program
	protected int createProgram(int... shaderIds) {
		int programId = glCreateProgram();
		
		for(int i = 0; i < shaderIds.length; i++) {
			glAttachShader(programId, shaderIds[i]);
		}
		
		glLinkProgram(programId);
		
		for(int i = 0; i < shaderIds.length; i++) {
			glDetachShader(programId, shaderIds[i]);
			glDeleteShader(shaderIds[i]);
		}
		
		return programId;
	}
	
	// Used to read shaders from files and return their content as a String
	protected String readFromFile(String file) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file),"UTF-8"))) {
			StringBuilder s = new StringBuilder();
			String l;
			
			while((l = reader.readLine()) != null)
				s.append(l).append('\n');
			
			return s.toString();
		}
		catch(Exception exc) {
			throw new RuntimeException("Failure reading file: " + file,exc);
		}
	}

}
