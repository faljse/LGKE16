package org.omilab.robot.mind;

public class VisionModelMind {
	private String PathToJpgFile;
	private boolean streaming;
	
	public VisionModelMind() {
		streaming=false;
	}
	public void setPathToJpgFile(String pathToFile) {
		PathToJpgFile = pathToFile;
	}

	public String getPathToJpgFile() {
		return PathToJpgFile;
	}

	public boolean isStreaming() {
		return streaming;
	}

	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
	
}
