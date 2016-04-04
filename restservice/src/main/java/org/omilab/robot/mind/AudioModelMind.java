package org.omilab.robot.mind;

public class AudioModelMind {
	private String PathToWavFile;
	private boolean streaming;
	
	public AudioModelMind() {
		streaming=false;
	}
	public void setPathToWavFile(String pathToFile) {
		PathToWavFile = pathToFile;
	}

	public String getPathToWavFile() {
		return PathToWavFile;
	}

	public boolean isStreaming() {
		return streaming;
	}

	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}
	
}
