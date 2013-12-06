package opengltutorial.chapter1;

import java.nio.FloatBuffer;

import opengltutorial.TutorialProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class HelloTriangle extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	
	public HelloTriangle() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setResizable(true);
			Display.setTitle("Hello Triangle!");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		this.positionBufferObject = initializeVertexBuffer();
		this.program = initializeProgram();
	}
	
	private int initializeVertexBuffer() {
		int positionBufferObject = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
		
		final float[] vertexPositionsAry = new float[] {
				0.75f, 0.75f, 0.0f, 1.0f,
				0.75f, -0.75f, 0.0f, 1.0f,
				-0.75f, -0.85f, 0.0f, 1.0f
		}; 
		
		FloatBuffer vertexPositions = BufferUtils.createFloatBuffer(vertexPositionsAry.length)
												 .put(vertexPositionsAry);
		vertexPositions.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexPositions, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(glGenVertexArrays());
		
		return positionBufferObject;
	}
	
	private int initializeProgram() {
		
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, readFromFile("vertex.glsl"));
		
		glCompileShader(vertexShader);
		
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, readFromFile("fragment.glsl"));
		
		glCompileShader(fragmentShader);
		
		int program = glCreateProgram();
		
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		
		glLinkProgram(program);
		
		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);
		
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		return program;
	}
	
	public void execute() {			
		while (!Display.isCloseRequested())
			display();
		
		Display.destroy();
	}
	
	public void display() {
		
			if(Display.wasResized())
				resize();

			glClearColor(0, 0, 0, 0);
			glClear(GL_COLOR_BUFFER_BIT);
			
			glUseProgram(program);
			
			glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
			
			glDrawArrays(GL_TRIANGLES, 0, 3);
			
			glDisableVertexAttribArray(0);
			glUseProgram(0);

			Display.update(); // calls (among other things) swapBuffers()
	}
	
	public void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public static void main(String[] argv) {
		HelloTriangle helloTriangle = new HelloTriangle();
		helloTriangle.execute();
	}
}