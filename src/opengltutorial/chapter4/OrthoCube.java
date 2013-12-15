package opengltutorial.chapter4;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import opengltutorial.TutorialProgram;

public class OrthoCube extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	private int offsetLocation;
	
	private final float[] vertexDataAry = new float[] {
			// Vertex Positions
			 0.25f,  0.25f, 0.75f, 1.0f,
			 0.25f, -0.25f, 0.75f, 1.0f,
			-0.25f,  0.25f, 0.75f, 1.0f,

			 0.25f, -0.25f, 0.75f, 1.0f,
			-0.25f, -0.25f, 0.75f, 1.0f,
			-0.25f,  0.25f, 0.75f, 1.0f,

			 0.25f,  0.25f, -0.75f, 1.0f,
			-0.25f,  0.25f, -0.75f, 1.0f,
			 0.25f, -0.25f, -0.75f, 1.0f,

			 0.25f, -0.25f, -0.75f, 1.0f,
			-0.25f,  0.25f, -0.75f, 1.0f,
			-0.25f, -0.25f, -0.75f, 1.0f,

			-0.25f,  0.25f,  0.75f, 1.0f,
			-0.25f, -0.25f,  0.75f, 1.0f,
			-0.25f, -0.25f, -0.75f, 1.0f,

			-0.25f,  0.25f,  0.75f, 1.0f,
			-0.25f, -0.25f, -0.75f, 1.0f,
			-0.25f,  0.25f, -0.75f, 1.0f,

			 0.25f,  0.25f,  0.75f, 1.0f,
			 0.25f, -0.25f, -0.75f, 1.0f,
			 0.25f, -0.25f,  0.75f, 1.0f,

			 0.25f,  0.25f,  0.75f, 1.0f,
			 0.25f,  0.25f, -0.75f, 1.0f,
			 0.25f, -0.25f, -0.75f, 1.0f,

			 0.25f,  0.25f, -0.75f, 1.0f,
			 0.25f,  0.25f,  0.75f, 1.0f,
			-0.25f,  0.25f,  0.75f, 1.0f,

			 0.25f,  0.25f, -0.75f, 1.0f,
			-0.25f,  0.25f,  0.75f, 1.0f,
			-0.25f,  0.25f, -0.75f, 1.0f,

			 0.25f, -0.25f, -0.75f, 1.0f,
			-0.25f, -0.25f,  0.75f, 1.0f,
			 0.25f, -0.25f,  0.75f, 1.0f,

			 0.25f, -0.25f, -0.75f, 1.0f,
			-0.25f, -0.25f, -0.75f, 1.0f,
			-0.25f, -0.25f,  0.75f, 1.0f,

			// Vertex Colors

			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,

			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,

			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,

			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,
			0.8f, 0.8f, 0.8f, 1.0f,

			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,

			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,

			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,

			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,
			0.5f, 0.5f, 0.0f, 1.0f,

			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,

			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,

			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,

			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f,
	}; 
	
	public OrthoCube() {

		try {
			Display.setDisplayMode(new DisplayMode(500,500));
			Display.setResizable(true);
			Display.setTitle("OrthoCube");
			
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		this.positionBufferObject = initializeVertexBuffer();
		this.program = initializeProgram();
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glFrontFace(GL_CW);
	}
	
	private int initializeVertexBuffer() {
		int positionBufferObject = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexDataAry.length)
											.put(vertexDataAry);
		vertexData.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return positionBufferObject;
	}
	
	private int initializeProgram() {
		
		int vertexShader = loadShader("shaders/OrthoWithOffset.vert");	
		int fragmentShader = loadShader("shaders/StandardColors.frag");
		
		int program = createProgram(vertexShader, fragmentShader);
		
		offsetLocation = glGetUniformLocation(program, "offset");
		
		return program;
	}
	
	public void execute() {
		long lastTime = System.nanoTime();
		while (!Display.isCloseRequested()) {
			long deltaTime = System.nanoTime() - lastTime;
			lastTime += deltaTime;
			display(deltaTime);
		}
		
		Display.destroy();
	}
	
	public void display(long deltaTime) {
		Display.sync(60);
		
		if(Display.wasResized())
			resize();

		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUseProgram(program);
		
		glUniform2f(offsetLocation, 0.5f, 0.25f);
		
		glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, (4 * 4 * 36));
		
		glDrawArrays(GL_TRIANGLES, 0, 36);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glUseProgram(0);

		Display.update(); // calls (among other things) swapBuffers()
	}
	
	public void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public static void main(String[] args) {
		OrthoCube orthoCube = new OrthoCube();
		orthoCube.execute();
	}

}
