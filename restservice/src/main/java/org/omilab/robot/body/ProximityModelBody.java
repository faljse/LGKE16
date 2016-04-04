package org.omilab.robot.body;

/**
 * 4 analog reflection sensors
 * 4 digital reflection sensors 
 * 1 analog distance sensor
 * 1 gyroscope 
 * 1 magnetometer
 * 4 color sensors
 */
public class ProximityModelBody {
	private class Rgb {
		private short c;
		private short r;
		private short g;
		private short b;
		private short lux;
		private short colortemp;
	}
	
	private class RotGyrAcc {
		private short xrot;
		private short yrot;
		private short xgyr;
		private short ygyr;
		private short zgyr;
		private short xacc;
		private short yacc;
		private short zacc;
	}
	
	private boolean leftOuterReflection;
	private boolean leftInnerReflection;
	private boolean rightOuterReflection;
	private boolean rightInnerReflection;
	private short leftOuterReflectionValue; //value from 0-3.3v
	private short leftInnerReflectionValue; //value from 0-3.3v
	private short rightOuterReflectionValue; //value from 0-3.3v
	private short rightInnerReflectionValue; //value from 0-3.3v
	
	private int activeRgb;
	private Rgb RgbFront;
	private Rgb RgbBack;
	private Rgb RgbLeft;
	private Rgb RgbRight;
	private RotGyrAcc RotGyrAcc;
	private int HeadingMin;
	private int HeadingSec;
	
	private short distance; //value in cm
	
	public ProximityModelBody () {
		RgbFront = new Rgb();
		RgbBack = new Rgb();
		RgbLeft = new Rgb();
		RgbRight = new Rgb();
		RotGyrAcc = new RotGyrAcc();
	}
	
	public int getActiveRgb() {
		return activeRgb;
	}
	public short getDistance() {
		return distance;
	}
	public int getHeadingMin() {
		return HeadingMin;
	}
	public int getHeadingSec() {
		return HeadingSec;
	}
	public short getLeftInnerReflectionValue() {
		return leftInnerReflectionValue;
	}
	public short getLeftOuterReflectionValue() {
		return leftOuterReflectionValue;
	}
	public short[] getRgb() {
		Rgb Rgb = null;
		switch(activeRgb) {
			case 1: Rgb = RgbFront; break;
			case 2: Rgb = RgbBack; break;
			case 3: Rgb = RgbLeft; break;
			case 4: Rgb = RgbRight; break;
		}
		short ret[] = new short[6];
		ret[0] = Rgb.c;
		ret[1] = Rgb.r;
		ret[2] = Rgb.g;
		ret[3] = Rgb.b;
		ret[4] = Rgb.lux;
		ret[5] = Rgb.colortemp;
		return ret;
	}
	public short getRightInnerReflectionValue() {
		return rightInnerReflectionValue;
	}
	public short getRightOuterReflectionValue() {
		return rightOuterReflectionValue;
	}
	public short[] getRotGyrAcc() {
		short ret[] = new short[8];
		
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
	public boolean hasLeftInnerReflection() {
		return leftInnerReflection;
	}
	public boolean hasLeftOuterReflection() {
		return leftOuterReflection;
	}
	public boolean hasRightInnerReflection() {
		return rightInnerReflection;
	}
	public boolean hasRightOuterReflection() {
		return rightOuterReflection;
	}
	public void setActiveRgb(int activeRgb) {
		this.activeRgb = activeRgb;
	}
	public void setDistance(short distance) {
		this.distance = distance;
	}
	public void setHeadingMin(int headingMin) {
		HeadingMin = headingMin;
	}
	public void setHeadingSec(int headingSec) {
		HeadingSec = headingSec;
	}
	public void setLeftInnerReflection(boolean leftInnerReflection) {
		this.leftInnerReflection = leftInnerReflection;
	}
	public void setLeftInnerReflectionValue(short leftInnerReflectionValue) {
		this.leftInnerReflectionValue = leftInnerReflectionValue;
	}
	public void setLeftOuterReflection(boolean leftOuterReflection) {
		this.leftOuterReflection = leftOuterReflection;
	}
	public void setLeftOuterReflectionValue(short leftOuterReflectionValue) {
		this.leftOuterReflectionValue = leftOuterReflectionValue;
	} 
	public void setRgb(short c, short r, short g, short b, short lux, short colortemp) {
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
	public void setRightInnerReflection(boolean rightInnerReflection) {
		this.rightInnerReflection = rightInnerReflection;
	}
	public void setRightInnerReflectionValue(short rightInnerReflectionValue) {
		this.rightInnerReflectionValue = rightInnerReflectionValue;
	}
	public void setRightOuterReflection(boolean rightOuterReflection) {
		this.rightOuterReflection = rightOuterReflection;
	}

	public void setRightOuterReflectionValue(short rightOuterReflectionValue) {
		this.rightOuterReflectionValue = rightOuterReflectionValue;
	}
	public void setRotGyrAcc(short xrot, short yrot, short xgyr, short ygyr, short zgyr, short xacc, short yacc, short zacc) {
		RotGyrAcc.xrot = xrot;
		RotGyrAcc.yrot = yrot;
		RotGyrAcc.xgyr = xgyr;
		RotGyrAcc.ygyr = ygyr;
		RotGyrAcc.zgyr = zgyr;
		RotGyrAcc.xacc = xacc;
		RotGyrAcc.yacc = yacc;
		RotGyrAcc.zacc = zacc;
	}
	
}
