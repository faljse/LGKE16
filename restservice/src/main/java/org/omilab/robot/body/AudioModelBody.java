package org.omilab.robot.body;

public class AudioModelBody {
	private String pathToWavFile;
	private boolean streaming;
	
	public AudioModelBody() {
		streaming=false;
	}
	
	public String getPathToWavFile() {
		return pathToWavFile;
	}
	public void setPathToWavFile(String pathToWavFile) {
		this.pathToWavFile = pathToWavFile;
	}
	public boolean isStreaming() {
		return streaming;
	}
	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
}
