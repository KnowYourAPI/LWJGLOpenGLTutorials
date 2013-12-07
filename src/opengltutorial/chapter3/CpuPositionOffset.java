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
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import opengltutorial.TutorialProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class CpuPositionOffset extends TutorialProgram {
	
	private int positionBufferObject;
	private int program;
	
	private long elapsedTime;
	
	private FloatBuffer newData;
	
	private final float[] vertexDataAry = new float[] {
		0.25f, 0.25f, 0.0f, 1.0f,
		0.25f, -0.25f, 0.0f, 1.0f,
		-0.25f, -0.25f, 0.0f, 1.0f,
	}; 
	
	public CpuPositionOffset() {
		elapsedTime = 0;
		newData = BufferUtils.createFloatBuffer(vertexDataAry.length);
		try {
			Display.setDisplayMode(new DisplayMode(500,500));
			Display.setResizable(true);
			Display.setTitle("CPU Position Offset");
			
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
		
		glBindVertexArray(glGenVertexArrays());
		
		return positionBufferObject;
	}
	
	private int initializeProgram() {
		
		int vertexShader = loadShader("Standard.vert");		
		int fragmentShader = loadShader("Standard.frag"); 
		
		int program = createProgram(vertexShader, fragmentShader);
		
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
		adjustVertexData(xOffset, yOffset);

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
	
	public float[] computePositionOffsets(float xOffset, float yOffset, long deltaTime) {
		final float loopDuration = 5;
		final float scale = (float)Math.PI * 2 / loopDuration;
		
		elapsedTime += deltaTime;
		
		float currentTimeThroughLoop = (elapsedTime/(float)1e9) % loopDuration;
		
		float newXOffset = (float)Math.cos(currentTimeThroughLoop * scale) * 0.5f;
		float newYOffset = (float)Math.sin(currentTimeThroughLoop * scale) * 0.5f;
		
		return new float[] { newXOffset, newYOffset };
	}
	
	public void adjustVertexData(float xOffset, float yOffset) {
		
		newData.clear();
		
		for(int i = 0; i < 12; i += 4) {
			newData.put(vertexDataAry[i] + xOffset);		// new x
			newData.put(vertexDataAry[i + 1] + yOffset);	// new y
			newData.put(0);									// new z
			newData.put(1);									// new w (clip space value)
		}
		
		newData.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
		glBufferSubData(GL_ARRAY_BUFFER, 0, newData);
		glBindBuffer(GL_ARRAY_BUFFER, 0);		
	}
	
	public static void main(String[] args) {
		CpuPositionOffset cpuPositionOffset = new CpuPositionOffset();
		cpuPositionOffset.execute();
	}

}
