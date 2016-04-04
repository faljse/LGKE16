package org.omilab.robot.body;

public class VisionModelBody {
	private String pathToJpgFile;
	private boolean streaming;
	
	public VisionModelBody() {
		streaming=false;
	}
	
	public String getPathToJpgFile() {
		return pathToJpgFile;
	}
	public void setPathToJpgFile(String pathToImageFile) {
		this.pathToJpgFile = pathToImageFile;
	}
	public boolean isStreaming() {
		return streaming;
	}
	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
}
