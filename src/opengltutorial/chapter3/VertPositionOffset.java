package opengltutorial.chapter3;

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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;

import java.nio.FloatBuffer;

import opengltutorial.TutorialProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class VertPositionOffset extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	private int offsetLocation;
	
	private long elapsedTime;
	
	private final float[] vertexDataAry = new float[] {
		0.25f, 0.25f, 0.0f, 1.0f,
		0.25f, -0.25f, 0.0f, 1.0f,
		-0.25f, -0.25f, 0.0f, 1.0f,
	}; 
	
	public VertPositionOffset() {
		elapsedTime = 0;

		try {
			Display.setDisplayMode(new DisplayMode(500,500));
			Display.setResizable(true);
			Display.setTitle("Vertex Shader Position Offset");
			
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
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexDataAry.length)
											.put(vertexDataAry);
		vertexData.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return positionBufferObject;
	}
	
	private int initializeProgram() {
		
		int vertexShader = loadShader("shaders/PositionOffset.vert");		
		int fragmentShader = loadShader("shaders/Standard.frag"); 
		
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
		elapsedTime += deltaTime;
		
		if(Display.wasResized())
			resize();
		
		float[] offsets = computePositionOffsets(0, 0, deltaTime);
		float xOffset = offsets[0];
		float yOffset = offsets[1];

		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUseProgram(program);
		
		glUniform2f(offsetLocation, xOffset, yOffset);
		
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
	
	public float[] computePositionOffsets(float xOffset, float yOffset, long deltaTime) {
		final float loopDuration = 5;
		final float scale = (float)Math.PI * 2 / loopDuration;
		
		float currentTimeThroughLoop = (elapsedTime/(float)1e9) % loopDuration;
		
		float newXOffset = (float)Math.cos(currentTimeThroughLoop * scale) * 0.5f;
		float newYOffset = (float)Math.sin(currentTimeThroughLoop * scale) * 0.5f;
		
		return new float[] { newXOffset, newYOffset };
	}
	
	public static void main(String[] args) {
		VertPositionOffset vertPositionOffset = new VertPositionOffset();
		vertPositionOffset.execute();
	}

}
