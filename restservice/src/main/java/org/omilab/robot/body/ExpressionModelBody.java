package org.omilab.robot.body;

public class ExpressionModelBody {
	private boolean buzzing;
	private int noiselevel;
	private boolean displaying;
	
	public boolean isBuzzing() {
		return buzzing;
	}
	public void setBuzzing(boolean buzzing) {
		this.buzzing = buzzing;
	}
	public boolean isDisplaying() {
		return displaying;
	}
	public void setDisplaying(boolean displaying) {
		this.displaying = displaying;
	}
	public int getNoiselevel() {
		return noiselevel;
	}
	public void setNoiselevel(int noiselevel) {
		this.noiselevel = noiselevel;
	}
}