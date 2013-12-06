package opengltutorial;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.lwjgl.opengl.Display;

public abstract class TutorialProgram {
	
	protected void destroy(){
		Display.destroy();
		System.exit(0);
	}
	
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
