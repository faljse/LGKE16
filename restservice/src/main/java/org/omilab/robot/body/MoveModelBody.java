package org.omilab.robot.body;

import org.omilab.robot.interfaces.bodyworld.EnumMotorDirection;
import org.omilab.robot.interfaces.bodyworld.EnumServoAngle;

public class MoveModelBody {
	private EnumMotorDirection dir1;
	private EnumMotorDirection dir2;
	private EnumMotorDirection dir3;
	private EnumMotorDirection dir4;

	private short spd1;
	private short spd2;
	private short spd3;
	private short spd4;

	private short w1;
	private short w2;
	private short w3;
	private short w4;

	private EnumServoAngle serv1;
	private EnumServoAngle serv2;
	private EnumServoAngle serv3;
	private EnumServoAngle serv4;
	private EnumServoAngle serv5;
	private EnumServoAngle serv15;

	public MoveModelBody() {
		dir1 = EnumMotorDirection.STOP;
		dir2 = EnumMotorDirection.STOP;
		dir3 = EnumMotorDirection.STOP;
		dir4 = EnumMotorDirection.STOP;

		spd1 = 0;
		spd2 = 0;
		spd3 = 0;
		spd4 = 0;

		w1 = 0;
		w2 = 0;
		w3 = 0;
		w4 = 0;

		serv1 = EnumServoAngle.NEUTRAL;
		serv2 = EnumServoAngle.NEUTRAL;
		serv3 = EnumServoAngle.NEUTRAL;
		serv4 = EnumServoAngle.NEUTRAL;
		serv5 = EnumServoAngle.NEUTRAL;
		serv15 = EnumServoAngle.NEUTRAL;
	}

	public EnumMotorDirection getDir1() {
		return dir1;
	}

	public EnumMotorDirection getDir2() {
		return dir2;
	}

	public EnumMotorDirection getDir3() {
		return dir3;
	}

	public EnumMotorDirection getDir4() {
		return dir4;
	}

	public EnumServoAngle getServ1() {
		return serv1;
	}

	public EnumServoAngle getServ15() {
		return serv15;
	}

	public EnumServoAngle getServ2() {
		return serv2;
	}
	public EnumServoAngle getServ3() {
		return serv3;
	}
	public EnumServoAngle getServ4() {
		return serv4;
	}
	public EnumServoAngle getServ5() {
		return serv5;
	}
	public short getSpd1() {
		return spd1;
	}

	public short getSpd2() {
		return spd2;
	}

	public short getSpd3() {
		return spd3;
	}

	public short getSpd4() {
		return spd4;
	}

	public short getW1() {
		return w1;
	}

	public short getW2() {
		return w2;
	}

	public short getW3() {
		return w3;
	}

	public short getW4() {
		return w4;
	}

	public void setDir1(EnumMotorDirection dir1) {
		this.dir1 = dir1;
	}

	public void setDir2(EnumMotorDirection dir2) {
		this.dir2 = dir2;
	}

	public void setDir3(EnumMotorDirection dir3) {
		this.dir3 = dir3;
	}

	public void setDir4(EnumMotorDirection dir4) {
		this.dir4 = dir4;
	}

	public void setServ1(EnumServoAngle serv1) {
		this.serv1 = serv1;
	}

	public void setServ15(EnumServoAngle serv15) {
		this.serv15 = serv15;
	}

	public void setServ2(EnumServoAngle serv2) {
		this.serv2 = serv2;
	}

	public void setServ3(EnumServoAngle serv3) {
		this.serv3 = serv3;
	}

	public void setServ4(EnumServoAngle serv4) {
		this.serv4 = serv4;
	}

	public void setServ5(EnumServoAngle serv5) {
		this.serv5 = serv5;
	}

	public void setSpd1(short spd1) {
		this.spd1 = spd1;
	}

	public void setSpd2(short spd2) {
		this.spd2 = spd2;
	}

	public void setSpd3(short spd3) {
		this.spd3 = spd3;
	}

	public void setSpd4(short spd4) {
		this.spd4 = spd4;
	}

	public void setW1(short w1) {
		this.w1 = w1;
	}

	public void setW2(short w2) {
		this.w2 = w2;
	}

	public void setW3(short w3) {
		this.w3 = w3;
	}

	public void setW4(short w4) {
		this.w4 = w4;
	}

}
