package org.omilab.robot.mind;

/**
 * 4 analog reflection sensors
 * 4 digital reflection sensors 
 * 1 analog distance sensor
 * 1 gyroscope 
 * 1 magnetometer
 * 4 color sensors
 */
public class ProximityModelMind {
	private boolean leftOuterReflection;
	private boolean leftInnerReflection;
	private boolean rightOuterReflection;
	private boolean rightInnerReflection;
	
	private float leftOuterReflectionValue; //value from 0-3.3v
	private float leftInnerReflectionValue; //value from 0-3.3v
	private float rightOuterReflectionValue; //value from 0-3.3v
	private float rightInnerReflectionValue; //value from 0-3.3v
	
	private class RotGyrAcc {
		private float xrot;
		private float yrot;
		private float xgyr;
		private float ygyr;
		private float zgyr;
		private float xacc;
		private float yacc;
		private float zacc;
	}
	
	private class Heading {
		private int min;
		private int sec;
	}
	
	private class Rgb {
		private int c;
		private int r;
		private int g;
		private int b;
		private int lux;
		private int colortemp;
	}
	
	public static int NONE = -1;
	public static int RGBFRONT = 1;
	public static int RGBBACK = 2;
	public static int RGBLEFT = 3;
	public static int RGBRIGHT = 4;
	
	private int activeRgb;
	private Rgb RgbFront;
	private Rgb RgbBack;
	private Rgb RgbLeft;
	private Rgb RgbRight;
	private RotGyrAcc RotGyrAcc;
	private Heading Heading;
	
	private float distance; //value in cm
	
	public ProximityModelMind () {
		RgbFront  = new Rgb();
		RgbBack  = new Rgb();
		RgbLeft  = new Rgb();
		RgbRight  = new Rgb();
		RotGyrAcc = new RotGyrAcc();
		Heading = new Heading();
		setActiveRgb(1);
	}
	
	public int[] getHeading() {
		int ret[] = new int[2];
		ret[0] = Heading.min;
		ret[1] = Heading.sec;
		return ret;
	}

	public void setHeading(int min, int sec) {
		Heading.min = min;
		Heading.sec = sec;
	}

	public boolean hasLeftOuterReflection() {
		return leftOuterReflection;
	}
	public void setLeftOuterReflection(boolean leftOuterReflection) {
		this.leftOuterReflection = leftOuterReflection;
	}
	public boolean hasLeftInnerReflection() {
		return leftInnerReflection;
	}
	public void setLeftInnerReflection(boolean leftInnerReflection) {
		this.leftInnerReflection = leftInnerReflection;
	}
	public boolean hasRightOuterReflection() {
		return rightOuterReflection;
	}
	public void setRightOuterReflection(boolean rightOuterReflection) {
		this.rightOuterReflection = rightOuterReflection;
	}
	public boolean hasRightInnerReflection() {
		return rightInnerReflection;
	}
	public void setRightInnerReflection(boolean rightInnerReflection) {
		this.rightInnerReflection = rightInnerReflection;
	}
	public float getLeftOuterReflectionValue() {
		return leftOuterReflectionValue;
	}
	public void setLeftOuterReflectionValue(float leftOuterReflectionValue) {
		this.leftOuterReflectionValue = leftOuterReflectionValue;
	}
	public float getLeftInnerReflectionValue() {
		return leftInnerReflectionValue;
	}
	public void setLeftInnerReflectionValue(float leftInnerReflectionValue) {
		this.leftInnerReflectionValue = leftInnerReflectionValue;
	}
	public float getRightOuterReflectionValue() {
		return rightOuterReflectionValue;
	}
	public void setRightOuterReflectionValue(float rightOuterReflectionValue) {
		this.rightOuterReflectionValue = rightOuterReflectionValue;
	}
	public float getRightInnerReflectionValue() {
		return rightInnerReflectionValue;
	}
	public void setRightInnerReflectionValue(float rightInnerReflectionValue) {
		this.rightInnerReflectionValue = rightInnerReflectionValue;
	}
	public int[] getRgb() {
		Rgb Rgb = null;
		switch(activeRgb) {
			case 1: Rgb = RgbFront; break;
			case 2: Rgb = RgbBack; break;
			case 3: Rgb = RgbLeft; break;
			case 4: Rgb = RgbRight; break;
		}
		int ret[] = new int[5];
		ret[0] = Rgb.c;
		ret[1] = Rgb.r;
		ret[2] = Rgb.g;
		ret[3] = Rgb.b;
		ret[4] = Rgb.lux;
		ret[5] = Rgb.colortemp;
		return ret;
	}
	public void setRgb(int c, int r, int g, int b, int lux, int colortemp) {
		Rgb Rgb = null;
		switch(activeRgb) {
			case 1: Rgb = RgbFront; break;
			case 2: Rgb = RgbBack; break;
			case 3: Rgb = RgbLeft; break;
			case 4: Rgb = RgbRight; break;
		}
		Rgb.c = c;
		Rgb.r = r;
		Rgb.g = g;
		Rgb.b = b;
		Rgb.lux = lux;
		Rgb.colortemp = colortemp;
	} 
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public int getActiveRgb() {
		return activeRgb;
	}
	public void setActiveRgb(int activeRgb) {
		this.activeRgb = activeRgb;
	}
	
	public void setRotGyrAcc(float xrot, float yrot, float xgyr, float ygyr, float zgyr, float xacc, float yacc, float zacc) {
		RotGyrAcc.xrot = xrot;
		RotGyrAcc.yrot = yrot;
		RotGyrAcc.xgyr = xgyr;
		RotGyrAcc.ygyr = ygyr;
		RotGyrAcc.zgyr = zgyr;
		RotGyrAcc.xacc = xacc;
		RotGyrAcc.yacc = yacc;
		RotGyrAcc.zacc = zacc;
	}
	public float[] getRotGyrAcc() {
		float ret[] = new float[8];
		
		ret[0] = RotGyrAcc.xrot;
		ret[1] = RotGyrAcc.yrot;
		ret[2] = RotGyrAcc.xgyr;
		ret[3] = RotGyrAcc.ygyr;
		ret[4] = RotGyrAcc.zgyr;
		ret[5] = RotGyrAcc.xacc;
		ret[6] = RotGyrAcc.yacc;
		ret[7] = RotGyrAcc.zacc;
		
		return ret;
	}
	
}
