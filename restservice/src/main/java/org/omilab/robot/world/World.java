package org.omilab.robot.world;

import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.bodyworld.BodyWorldInterface;
import org.omilab.robot.interfaces.bodyworld.EnumMotorDirection;
import org.omilab.robot.interfaces.bodyworld.EnumServoAngle;
import org.omilab.robot.interfaces.bodyworld.EnumWorldAccessMethod;

public class World implements BodyWorldInterface {
	private EnumWorldAccessMethod accessmethod;
	private CSVReadTool csvReadTool;
	private SocketClient robot;
	private Body backdoorAccess;
	
	boolean pufferMotorCommand;
	int[][] motorparam;
	int dlsLowValueThreshold;
	
	public String copyImagePath = "images/image.jpg";
	public String copySoundPath = "sounds/sound.wav";
	public int imageNumber = 1;
	public int soundNumber = 1;

	public World() {
		pufferMotorCommand = false;
		motorparam = new int[][] {{255,4}, {255,4}, {255,4}, {255,4}};
	}
	
	@Override
	public void actAudioCaptureMic(short seconds) {
		switch(accessmethod) {
		case SOCKET:
			robot.recordMicWAV(seconds);
			break;
		
		case TESTDATA:
			System.out.println("> Create wav file on server!");
			break;
			
		}
	}

	@Override
	public void actAudioStreamMic(boolean on, String address) {
		switch(accessmethod) {
		case SOCKET:
			if (on == true)
				robot.enableAudioStream(address);
			else
				robot.disableAudioStream(address);
			break;
		
		case TESTDATA:
			if (on == true)
				System.out.println("> Enable audio stream from server!");
			else
				System.out.println("> Disable audio stream from server!");
			break;
			
		}
	}

	public void actColorSensors(short number) {
		switch(accessmethod) {
		case SOCKET:
			if (number == 0)
				robot.muxI2C((short) 0x80);
			else if (number == 1)
				robot.muxI2C((short) 0x40);
			else
				System.out.println("> Cannot translate number to MUX address!");
			break;
		
		case TESTDATA:
			System.out.println("> MUX color sensor i2c on server to " + Short.toString(number) + "!");
			break;
			
		}
	}

	@Override
	public void actDisplay(short[][] matrix) {
		switch(accessmethod) {
		case SOCKET:
			robot.setRGBMatrix(matrix);
			break;
			
		case TESTDATA:
			System.out.println("> Set the 8x8 matrix on the server to: ");
			for (int i=0; i < 8; i++) {
				System.out.print(">> ");
				for (int j=0; j < 8; j++) 
					System.out.print(matrix[i][j]);
				System.out.println();
			}
			break;
			
		}
	}

	@Override
	public void actDistance() {
		switch(accessmethod) {
		case SOCKET:
			//Would be required if, e.g., pings were aggregated or if the the application were really, really time critical.
			//Would issue a ping and the echo would be detected in senseDistance
			break;
		
		case TESTDATA:
		System.out.println("> Starting distance measurement on the server!");
			break;
			
		}
	}
	
	@Override
	public void actLCD(String text, short[] color) {
		switch(accessmethod) {
		case SOCKET:
			robot.setLCDDisplay(text, color);
			break;
		
		case TESTDATA:
			System.out.println("> Set the LCD on the server to '" + text + "'!");
			break;
			
		}
	}

	@Override
	public void actMotor(short number, EnumMotorDirection Direction, short speed) {
		switch(accessmethod) {
		case SOCKET:
			if (pufferMotorCommand) {
				motorparam[number-1][0] = speed;
				motorparam[number-1][1] = Direction.ordinal() + 1;
			}
			else {
				motorparam[number-1][0] = speed;
				motorparam[number-1][1] = Direction.ordinal() + 1;
				robot.drive((short) motorparam[0][0], (short)motorparam[0][1], (short)motorparam[1][0], (short)motorparam[1][1], (short)motorparam[2][0], (short)motorparam[2][1], (short)motorparam[3][0], (short)motorparam[3][1]);
			}
			break;
			
		case TESTDATA:
			System.out.println("> Issue Motor Command on Server: Motor " + Integer.toString(number) + ", Direction " + Direction.toString() + ", Speed " + Integer.toString(speed)+"!");
			break;
		}
	}

	@Override
	public void actNoise(short hz) {
		switch(accessmethod) {
		case SOCKET:
			robot.buzz(hz);
			break;
			
		case TESTDATA:
			System.out.println("> Buzz on Server with Freq "+Integer.toString(hz)+"!");
			break;
			
		}
	}

	@Override
	public void actVisionCaptureCamera() {
		switch(accessmethod) {
		case SOCKET:
			robot.recordCameraJPG();
			break;
			
		case TESTDATA:
			System.out.println("> Create jpg file on server");
			break;
			
		}
	}

	@Override
	public void actVisionStreamCamera(boolean on, String address) {
		switch(accessmethod) {
		case SOCKET:
			if (on == true)
				robot.enableVideoStream(address);
			else
				robot.disableVideoStream(address);
			break;
			
		case TESTDATA:
			if (on == true)
				System.out.println("> Enable video stream on server");
			else
				System.out.println("> Disable video stream on server");
			break;
		}	
	}
	
	public boolean isPufferMotorCommand() {
		return pufferMotorCommand;
	}

	@Override
	public short[] senseRotGyrAcc() {
		short[] ret = {0,0,0,0,0,0,0,0};
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.getRotationGyroAccelerometer();
			break; 
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.print(Element+" -> RotGyrAcc");
	
				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++)
					ret[i] = (short) Integer.parseInt(Values[i]);
				
			}
			System.out.println();
			break;
			
		}
		return ret;
		
	}

	@Override
	public short[] senseAnalogLineSensors() {
		short[] ret = {0,0,0,0};
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.getADCAllChannelValues('3');
			break;

		case TESTDATA:
			System.out.print("> ");

			String Element = csvReadTool.readLineElement();
	
			if (Element != null) {
				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++) {
					System.out.print(Values[i]+" -> ALS"+Integer.toString(i)+" ");
					ret[i] = Short.parseShort(Values[i]);
				}
			}
			
			System.out.println();
			break;
			
		}
		
		return ret;
	}

	@Override
	public short[] senseAnalogWheelRotationSensors() {
		short[] ret = {0,0,0,0};
		
		switch(accessmethod) {
		case SOCKET:
			// TODO adc1
			break;
			
		case TESTDATA:
			System.out.print("> ");

			String Element = csvReadTool.readLineElement();
	
			if (Element != null) {
				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++) {
					System.out.print(Values[i]+" -> WRS"+Integer.toString(i)+" ");
					ret[i] = Short.parseShort(Values[i]);
				}
			}
			
			System.out.println();
			break;
		}
			
		return ret;
	}

	@Override
	public String senseAudioMic() {
		StringBuilder sb = new StringBuilder();
		sb.append(copySoundPath);
		sb.insert(copySoundPath.indexOf('.'), Integer.toString(soundNumber++));
		
		switch(accessmethod) {
		case SOCKET:
			robot.sendSound(sb.toString());
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.println(Element+" -> AudioMic");
			}
			break;

		}
		
		return sb.toString();
	}

	@Override
	public short[] senseColor() {
		short[] ret = {0,0,0,0,0,0};
		
		switch(accessmethod) {
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.print(Element+" -> CS");

				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++)
					ret[i] = Short.parseShort(Values[i]);
				
			}
			System.out.println();
			break;
			
		case SOCKET:
			ret = robot.getColorSensorValues();
			break;
		}
		
		return ret;	
	}

	@Override
	public boolean[] senseDigitalLineSensors() {
		short[] values = {0,0,0,0};
		boolean[] ret = {false,false,false,false};
		
		switch(accessmethod) {
		case SOCKET:
			values = robot.getADCAllChannelValues('2');
			for (int number=0; number < 4; number++) {
				if (values[number] > dlsLowValueThreshold) // High reflection Value = no black line detected
					ret[number] = false; // no line detected
				else
					ret[number] = true; //line detected
			}
			break;
			
		case TESTDATA:
			System.out.print("> ");

			String Element = csvReadTool.readLineElement();
			
			if (Element != null) {
				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++) {
					System.out.print(Values[i]+" -> ALS"+Integer.toString(i)+" ");
					ret[i] = Values[i].compareTo("true") == 0;
				}
			}
			
			System.out.println();
			break;
			
		}
		
		return ret;
	}

	@Override
	public short senseDistance() {
		short ret = -1;
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.getDistance();
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.println(Element+" -> DS");
				ret = Short.parseShort(Element);
			}
			break;
			
		}
		
		return ret;
	}

	@Override
	public short[] senseHeadingMag() {
		short[] ret = {-1,-1,-1,-1,-1,-1};
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.getHeading();
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.print(Element+" -> Heading");
	
				String[] Values = Element.split(",");
				for (int i = 0; i < Values.length; i++)
					ret[i] = (short) Integer.parseInt(Values[i]);
				
			}
			System.out.println();
			break;
		}
		
		return ret;
	}

	@Override
	public boolean senseDigitalNoise() {
		boolean ret = false;
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.digitalNoise();
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.println(Element+" -> Noise Sensor");
				if (Element.compareTo("true") == 0)
					ret = true;
			}
			break;
		}
		
		return ret;
	}

	@Override
	public String senseVisionCamera() {
		StringBuilder sb = new StringBuilder();
		sb.append(copyImagePath);
		sb.insert(copyImagePath.indexOf('.'), Integer.toString(imageNumber++));
		
		switch(accessmethod) {
		case SOCKET:
			robot.sendImage(sb.toString());
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) 
				System.out.print(Element+" -> Vision ");
			System.out.println();
			break;
		}
		
		return sb.toString();
	}

	public void setAccessmethod(EnumWorldAccessMethod AccessMethod) {
		System.out.println("Initiating Client ...");
		this.accessmethod = AccessMethod;
		System.out.println("Access Method is " + this.accessmethod.toString() + " ...");
		
		motorparam = new int[][] {{0,0}, {0,0}, {0,0}, {0,0}};

		switch(AccessMethod) {
		case SOCKET:
			System.out.println("Initiating Server ...");
			//pysocket.py should be running at this point
			dlsLowValueThreshold = 1000;
			robot = new SocketClient();
			robot.initiate();
			break;
		
		case TESTDATA:
			System.out.println("Initiating Test Environment ...");
			csvReadTool = new CSVReadTool();
			if (!csvReadTool.openFile("testdata.csv"))
				System.out.println("Problem opening testdata file " + getClass().getResourceAsStream("/testdata.csv") + "!");
			break;
			
		case COMMANDLINE:
		case COMMANDLINEPRECOMPILED:
			//Commandline execution of python scrips is disabled for performance reasons
		
		case JYTHON:
			//JYTHON is not enabled as i could not find out how to import e.g., ioctl easily
		}

		System.out.println("Done!");
	}

	public void setPufferMotorCommand(boolean pufferMotorCommand) {
		this.pufferMotorCommand = pufferMotorCommand;
	}

	@Override
	public void actServo(short channel, EnumServoAngle ServoAngle) {
		switch(accessmethod) {
		case SOCKET:
			robot.servo(channel, (short) ServoAngle.getValue());
			break;	
			
		case TESTDATA:
			System.out.println("> Move servo on server: Servo " + Integer.toString(channel) + ", Rotation " + ServoAngle.name()+"!");
			break;
		}
	}

	@Override
	public void actPlaySound() {
		switch(accessmethod) {
		case SOCKET:
			robot.playWav();
			break;
			
		case TESTDATA:
			System.out.println("> Playing wav file");
			break;
		}
	}

	@Override
	public void actPlaySound(String escapedPathAndFilename) {
		switch(accessmethod) {
		case SOCKET:
			robot.receiveSound(escapedPathAndFilename);
			robot.playWav();
			break;
			
		case TESTDATA:
			System.out.println("> Copying wav file");
			System.out.println("> Playing wav file");
			break;
		
		}
	}

	@Override
	public void actLaser(boolean on) {
		switch(accessmethod) {
		case SOCKET:
			robot.laser(on);
			break;
			
		case TESTDATA:
			System.out.println("> Enabling laser on server");
			break;
			
		}
		
	}

	@Override
	public short senseTemp() {
		short ret = -1;
		
		switch(accessmethod) {
		case SOCKET:
			ret = robot.temp();
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.println(Element+" -> Temperature Sensor");
				ret = Short.parseShort(Element);
			}
			break;
			
		}
		
		return ret;
	}

	@Override
	public void death() {
		switch(accessmethod) {
		case SOCKET:
			robot.quit();
			break;
			
		case TESTDATA:
			System.out.print("> Shutting Down!");
			break;
			
		}
		
	}

	@Override
	public short senseAnalogNoise() {
		short ret = -1;
				
		switch(accessmethod) {
		case SOCKET:
			ret = robot.analogNoise();
			break;
			
		case TESTDATA:
			System.out.print("> ");
			String Element = csvReadTool.readLineElement();
			if (Element != null) {
				System.out.println(Element+" -> Noise Sensor");
				ret = Short.parseShort(Element);
			}
			break;
		}
		
		return ret;
	}
	
}
