package org.omilab.robot.interfaces.mindbody;

import java.io.IOException;

public interface MindBodyInterface {
	//desymbolize logic is in body - should be done somewhere else, e.g. in the interface class which would need its own data classes
	void desymbolizeExpressionModel() throws IOException;
	void desymbolizeMoveModel() throws IOException;
	void desymbolizeProximityModel() throws IOException;
	void desymbolizeAudioModel() throws IOException;
	void desymbolizeVisionModel() throws IOException;
	
	//symbolize logic is in mind - should be done somewhere else, e.g. in the interface class which would need its own data classes
	void symbolizeExpressionModel() throws IOException;
	void symbolizeMoveModel() throws IOException;
	void symbolizeProximityModel() throws IOException;
	void symbolizeAudioModel() throws IOException;
	void symbolizeVisionModel() throws IOException;
}