package org.omilab.robot.body;
import org.omilab.robot.interfaces.bodyworld.*;
import org.omilab.robot.interfaces.mindbody.*;
import org.omilab.robot.mind.Mind;
import org.omilab.robot.world.World;


public class Body implements BodyWorldInterface, MindBodyInterface {
	private ProximityModelBody ProximityModelBody;
	private MoveModelBody MoveModelBody;
	private VisionModelBody VisionModelBody;
	private AudioModelBody AudioModelBody;
	private ExpressionModelBody ExpressionModelBody;
	private World World;
	private Mind MindBackdoorAccess;

	public Body() {
		ProximityModelBody = new ProximityModelBody();
		VisionModelBody = new VisionModelBody();
		AudioModelBody = new AudioModelBody();
		MoveModelBody = new MoveModelBody();
		ExpressionModelBody = new ExpressionModelBody();
		World = new World();
	}
	
	public Body(EnumWorldAccessMethod WorldAccessMethod) {
		this();
		World.setAccessMethod(WorldAccessMethod);
	}
	
	// 
	// *********** Body - World Interface ***********
	//
	
	@Override
	public void actAudioCaptureMic(short seconds) {
		World.actAudioCaptureMic(seconds);
	}
	
	@Override
	public void actAudioStreamMic(boolean on, String address) {
		World.actAudioStreamMic(on, address);	
	}

	@Override
	public void actColorSensors(short number) {
		World.actColorSensors(number);
	}
	
	@Override
	public void actDisplay(short[][] s) {
		World.actDisplay(s);
	}
	
	@Override
	public void actDistance() {
		World.actDistance();
	}
	
	@Override
	public void actLaser(boolean on) {
		World.actLaser(on);
	}

	@Override
	public void actLCD(String text, short[] color) {
		World.actLCD(text, color);
	}

	@Override
	public void actMotor(short number, EnumMotorDirection Direction, short speed) {
		World.actMotor(number, Direction, speed);
	}

	@Override
	public void actNoise(short hz) {
		World.actNoise(hz);
	}

	@Override
	public void actPlaySound() {
		World.actPlaySound();
	}

	public void actPlaySound(String escapedpathandfilename) {
		World.actPlaySound(escapedpathandfilename);
	}
	
	@Override
	public void actServo(short channel, EnumServoAngle ServoAngle) {
		World.actServo(channel, ServoAngle);	
	}
	
	@Override
	public void actVisionCaptureCamera() {
		World.actVisionCaptureCamera();
	}
	
	@Override
	public void actVisionStreamCamera(boolean streaming, String address) {
		World.actVisionStreamCamera(streaming, address);
	}
	
	@Override
	public short[] senseAnalogLineSensors() {
		return World.senseAnalogLineSensors();
	}

	@Override
	public short[] senseAnalogWheelRotationSensors() {
		return World.senseAnalogWheelRotationSensors();
	}

	@Override
	public String senseAudioMic() {
		return World.senseAudioMic();
	}

	@Override
	public short[] senseColor() {
		return World.senseColor();
	}

	@Override
	public boolean[] senseDigitalLineSensors() {
		return World.senseDigitalLineSensors();
	}

	@Override
	public boolean senseDigitalNoise() {
		return World.senseDigitalNoise();
	}

	@Override
	public short senseDistance() {
		return World.senseDistance();
	}

	@Override
	public short[] senseHeadingMag() {
		return World.senseHeadingMag();
	}

	@Override
	public short[] senseRotGyrAcc() {
		return World.senseRotGyrAcc();
	}

	@Override
	public short senseTemp() {
		return World.senseTemp();
	}

	@Override
	public String senseVisionCamera() {
		return World.senseVisionCamera();
	}
	
	// 
	// *********** Mind - Body Interface ***********
	//
	
	public void setMind(Mind Mind) {
		MindBackdoorAccess = Mind;
	}
	
	public AudioModelBody getAudioModelBody() {
		return AudioModelBody;
	}

	public MoveModelBody getMoveModelBody() {
		return MoveModelBody;
	}

	public ProximityModelBody getProximityModelBody() {
		return ProximityModelBody;
	}

	public VisionModelBody getVisionModelBody() {
		return VisionModelBody;
	}
	
	public void pufferMotorCommand(boolean on) {
		World.setPufferMotorCommand(on);
	}
	
	@Override
	public void desymbolizeAudioModel() {
		if (!MindBackdoorAccess.getAudioModelMind().isStreaming()) {
			if (AudioModelBody.isStreaming()) {
				actAudioStreamMic(false, "");
				AudioModelBody.setStreaming(false);
			}
			else
				actAudioCaptureMic((short) 10);
		}
		else
			if (!AudioModelBody.isStreaming()) {
				actAudioStreamMic(true, "");
				AudioModelBody.setStreaming(true);
			}
	}
	
	@Override
	public void desymbolizeExpressionModel() {
		if (MindBackdoorAccess.getExpressionModelMind().isSetDisplay()) {
			short[][] s = MindBackdoorAccess.getExpressionModelMind().getDisplay();
			short[][] sFlipped = new short[8][8];

			if (MindBackdoorAccess.getExpressionModelMind().getDisplayColor() < 4)
				for(int i=0; i < s.length; i++) 
			        for(int j=0; j < s[i].length; j++) {
			        	if (s[i][j] > 0)
			        		s[i][j] = (short) (s[i][j] + MindBackdoorAccess.getExpressionModelMind().getDisplayColor() - 1);
			        	sFlipped[7-i][7-j] = s[i][j];
			        }
			if (MindBackdoorAccess.getExpressionModelMind().isFlippedDisplay())
				actDisplay(sFlipped);
			else
				actDisplay(s);
		}
		
		if (MindBackdoorAccess.getExpressionModelMind().isSetLCD()) {
			short[] color = new short[3];
			color[0] = (short) (MindBackdoorAccess.getExpressionModelMind().getLCDColor()[0] / 255);
			color[1] = (short) (MindBackdoorAccess.getExpressionModelMind().getLCDColor()[1] / 255);
			color[2] = (short) (MindBackdoorAccess.getExpressionModelMind().getLCDColor()[2] / 255);
			actLCD(MindBackdoorAccess.getExpressionModelMind().getLCD(), color);
		}
		
		if (MindBackdoorAccess.getExpressionModelMind().isSetNoise()) {
			//TODO Buzz
		}
	}

	@Override
	public void desymbolizeMoveModel() {
		short speed = (short) MindBackdoorAccess.getMoveModelMind().getSpeed();
		switch (MindBackdoorAccess.getMoveModelMind().getDirection()) {
		case WD4FORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.FORWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD4BACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.BACKWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD4STOP:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD4LEFTTURN:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.FORWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD4RIGHTTURN:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.BACKWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2LEFTSIDEFORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2LEFTSIDEBACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2RIGHTSIDEFORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.FORWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2RIGHTSIDEBACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.BACKWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2FRONTFORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2FRONTBACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2BACKFORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.FORWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.FORWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD2BACKBACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.BACKWARD);
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.BACKWARD);
			World.setPufferMotorCommand(true);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			World.setPufferMotorCommand(false);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
			
		case WD1LEFTFRONTFORWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.FORWARD);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			break;
		case WD1LEFTFRONTBACKWARD:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.BACKWARD);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			break;
		case WD1RIGHTFRONTFORWARD:
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.FORWARD);
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			break;
		case WD1RIGHTFRONTBACKWARD:
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.BACKWARD);
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			break;
		case WD1LEFTBACKFORWARD:
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.FORWARD);
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			break;
		case WD1LEFTBACKBACKWARD:
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.BACKWARD);
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			break;
		case WD1RIGHTBACKFORWARD:
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.FORWARD);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		case WD1RIGHTBACKBACKWARD:
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.BACKWARD);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
			
		case WD1LEFTFRONTSTOP:
			MoveModelBody.setSpd1(speed);
			MoveModelBody.setDir1(EnumMotorDirection.STOP);
			World.actMotor((short) 1, MoveModelBody.getDir1(), MoveModelBody.getSpd1());
			break;
		case WD1LEFTBACKSTOP:
			MoveModelBody.setSpd2(speed);
			MoveModelBody.setDir2(EnumMotorDirection.STOP);
			World.actMotor((short) 2, MoveModelBody.getDir2(), MoveModelBody.getSpd2());
			break;
		case WD1RIGHTFRONTSTOP:
			MoveModelBody.setSpd3(speed);
			MoveModelBody.setDir3(EnumMotorDirection.STOP);
			World.actMotor((short) 3, MoveModelBody.getDir3(), MoveModelBody.getSpd3());
			break;
		case WD1RIGHTBACKSTOP:
			MoveModelBody.setSpd4(speed);
			MoveModelBody.setDir4(EnumMotorDirection.STOP);
			World.actMotor((short) 4, MoveModelBody.getDir4(), MoveModelBody.getSpd4());
			break;
		}	
		
		if (MindBackdoorAccess.getMoveModelMind().getGesture() != null) {
			switch (MindBackdoorAccess.getMoveModelMind().getGesture()) {
			case RIGHT:
				MoveModelBody.setServ1(EnumServoAngle.MINUS30);
				break;
			case LEFT:
				MoveModelBody.setServ1(EnumServoAngle.PLUS30);
				break;
			case FORWARD:
				MoveModelBody.setServ2(EnumServoAngle.PLUS30);
				break;
			case BACKWARD:
				MoveModelBody.setServ2(EnumServoAngle.MINUS30);
				break;
			case UP:
				MoveModelBody.setServ3(EnumServoAngle.MINUS30);
				break;
			case DOWN:
				MoveModelBody.setServ3(EnumServoAngle.PLUS30);
				break;
			case OPEN:
				MoveModelBody.setServ4(EnumServoAngle.MINUS30);
				break;
			case CLOSE:
				MoveModelBody.setServ4(EnumServoAngle.PLUS30);
				break;
			}
			
			if (MindBackdoorAccess.getMoveModelMind().getGesture() != EnumMoveModelArm.IDLE) {
				World.actServo((short) 0, MoveModelBody.getServ1());
				World.actServo((short) 1, MoveModelBody.getServ2());
				World.actServo((short) 2, MoveModelBody.getServ3());
				World.actServo((short) 3, MoveModelBody.getServ4());
				World.actServo((short) 14, MoveModelBody.getServ15());
				
				MindBackdoorAccess.getMoveModelMind().setGesture(EnumMoveModelArm.IDLE);
			}
		}

	}

	@Override
	public void desymbolizeProximityModel() {
		ProximityModelBody.setActiveRgb(MindBackdoorAccess.getProximityModelMind().getActiveRgb());
		if (ProximityModelBody.getActiveRgb() == 1)
			actColorSensors((short) 7);
		else if (ProximityModelBody.getActiveRgb() == 2)
			actColorSensors((short) 6);
		
		actDistance();
		// TODO Auto-generated method stub
		//here we set Magnetic Measurement Declination and Scale
		//here we set RGB Measurement Time
		
	}
	
	@Override
	public void desymbolizeVisionModel() {
		if (!MindBackdoorAccess.getVisionModelMind().isStreaming()) {
			if (VisionModelBody.isStreaming()) {
				actVisionStreamCamera(false, "");
				VisionModelBody.setStreaming(false);
			}
			else
				actVisionCaptureCamera();
		}
		else
			if (!VisionModelBody.isStreaming()) {
				actVisionStreamCamera(true, "");
				VisionModelBody.setStreaming(true);
			}
	}

	@Override
	public void symbolizeAudioModel() {
		if (!AudioModelBody.isStreaming())
			AudioModelBody.setPathToWavFile(senseAudioMic());
		// else	
			// TODO Here we would process a audio stream
	}

	@Override
	public void symbolizeExpressionModel() {
		ExpressionModelBody.setNoiselevel(senseDigitalNoise() ? 1 : 0);
	}
	
	@Override
	public void symbolizeMoveModel() {
		short[] s = senseAnalogWheelRotationSensors();
		MoveModelBody.setW1(s[0]);
		MoveModelBody.setW2(s[1]);
		MoveModelBody.setW3(s[2]);
		MoveModelBody.setW4(s[3]);
	}

	@Override
	public void symbolizeProximityModel() {
		boolean[] b = senseDigitalLineSensors();
		ProximityModelBody.setLeftOuterReflection(b[0]);
		ProximityModelBody.setLeftInnerReflection(b[1]);
		ProximityModelBody.setRightOuterReflection(b[2]);
		ProximityModelBody.setRightInnerReflection(b[3]);
		
		short[] s = senseAnalogLineSensors();
		ProximityModelBody.setLeftOuterReflectionValue(s[0]);
		ProximityModelBody.setLeftInnerReflectionValue(s[1]);
		ProximityModelBody.setRightOuterReflectionValue(s[2]);
		ProximityModelBody.setRightInnerReflectionValue(s[3]);
		
		if (ProximityModelBody.getActiveRgb() != -1) {
			short[] rgb;
			rgb = senseColor();
			ProximityModelBody.setRgb(rgb[0], rgb[1], rgb[2], rgb[3], rgb[4], rgb[5]);
		}
		
		ProximityModelBody.setDistance(senseDistance());
		
		s = this.senseRotGyrAcc();
		ProximityModelBody.setRotGyrAcc(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
		
		s = this.senseHeadingMag();
		ProximityModelBody.setHeadingMin(s[0]);
		ProximityModelBody.setHeadingSec(s[1]);
	}

	@Override
	public void symbolizeVisionModel() {
		if (!VisionModelBody.isStreaming())
			VisionModelBody.setPathToJpgFile(senseVisionCamera());
		// else	
			// TODO Here we would process a video stream
	}

	@Override
	public void death() {
		World.death();
	}

	@Override
	public short senseAnalogNoise() {
		return World.senseAnalogNoise();
	}
	
}
