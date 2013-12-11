package opengltutorial.chapter1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import opengltutorial.TutorialProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class HelloTriangle extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	
	public HelloTriangle() {
		try {
			Display.setDisplayMode(new DisplayMode(500,500));
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
		
		return positionBufferObject;
	}
	
	private int initializeProgram() {
		
		int vertexShader = loadShader("vertex.vert");		
		int fragmentShader = loadShader("fragment.frag");
		
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