package org.omilab.robot.interfaces.bodyworld;

public enum EnumServoAngle {
	NEUTRAL(250),
	MINUS10(240),
	MINUS20(230),
	MINUS30(220),
	MINUS40(210),
	MINUS50(200),
	PLUS10(260),
	PLUS20(270),
	PLUS30(280),
	PLUS40(290),
	PLUS50(300);
	
	private int value;
	
	private EnumServoAngle(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
