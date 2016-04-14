package org.omilab.robot.mind;

public class VisionModelMind {
	private String pathToJpgFile;
	private boolean streaming;
	
	public VisionModelMind() {
		streaming=false;
	}
	public void setPathToJpgFile(String pathToFile) {
		pathToJpgFile = pathToFile;
	}

	public String getPathToJpgFile() {
		return pathToJpgFile;
	}

	public boolean isStreaming() {
		return streaming;
	}

	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
	
}
