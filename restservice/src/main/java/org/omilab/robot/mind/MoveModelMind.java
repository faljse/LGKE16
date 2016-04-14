package org.omilab.robot.mind;

import org.omilab.robot.interfaces.mindbody.EnumMoveModelArm;
import org.omilab.robot.interfaces.mindbody.EnumMoveModelDirection;

public class MoveModelMind {
	private EnumMoveModelDirection direction;
	private int speed;
	
	private int w1dist;
	private int w2dist;
	private int w3dist;
	private int w4dist;
	
	private EnumMoveModelArm gesture;
	
	public EnumMoveModelArm getGesture() {
		return gesture;
	}
	public void setGesture(EnumMoveModelArm gesture) {
		this.gesture = gesture;
	}
	public EnumMoveModelDirection getDirection() {
		return direction;
	}
	public void setDirection(EnumMoveModelDirection direction) {
		this.direction = direction;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getW1dist() {
		return w1dist;
	}
	public void setW1dist(int w1dist) {
		this.w1dist = w1dist;
	}
	public int getW2dist() {
		return w2dist;
	}
	public void setW2dist(int w2dist) {
		this.w2dist = w2dist;
	}
	public int getW3dist() {
		return w3dist;
	}
	public void setW3dist(int w3dist) {
		this.w3dist = w3dist;
	}
	public int getW4dist() {
		return w4dist;
	}
	public void setW4dist(int w4dist) {
		this.w4dist = w4dist;
	}
	
}
