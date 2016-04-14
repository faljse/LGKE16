package org.omilab.robot.mind;

public class AudioModelMind {
	private String pathToWavFile;
	private boolean streaming;
	
	public AudioModelMind() {
		streaming=false;
	}
	public void setPathToWavFile(String pathToFile) {
		pathToWavFile = pathToFile;
	}

	public String getPathToWavFile() {
		return pathToWavFile;
	}

	public boolean isStreaming() {
		return streaming;
	}

	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
	
}
