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

// In this class I experimented a little with drawing a quad instead of a triangle
// I also played with the x and y values of the vertices

public class HelloQuad extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	
	public HelloQuad() {
		try {
			Display.setDisplayMode(new DisplayMode(600,600));
			Display.setResizable(true);
			Display.setTitle("Hello Quad!");
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
				0.1f, 0.1f, 0.0f, 1.0f,
				0.1f, -0.1f, 0.0f, 1.0f,
				-0.1f, -0.1f, 0.0f, 1.0f,
				-0.1f, 0.1f, 0.0f, 1.0f
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
		
		int vertexShader = loadShader("vertex.glsl", GL_VERTEX_SHADER);		
		int fragmentShader = loadShader("fragment.glsl", GL_FRAGMENT_SHADER);
		
		int program = createProgram(vertexShader, fragmentShader);
		
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
		
		// Change the draw mode to GL_QUAD and tell openGL that we now have 4 vertices in our array
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableVertexAttribArray(0);
		glUseProgram(0);

		Display.update(); // calls (among other things) swapBuffers()
	}
	
	public void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public static void main(String[] argv) {
		HelloQuad helloTriangle = new HelloQuad();
		helloTriangle.execute();
	}
}