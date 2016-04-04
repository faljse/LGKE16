package org.omilab.robot.test;

import java.io.File;

import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.bodyworld.EnumServoAngle;

public class Test {
	public void testBody (Body Body, String system) {
		if (Body == null) {
			System.out.println("System not initiated");
			return;
		}
		
		switch (system) {
		case "death":
			System.out.println("Killing the robot ...");
			Body.death();
			break;
			
		case "actAudioCaptureMic":
			System.out.println("Capturing 5 second wav file on robot ...");
	        Body.actAudioCaptureMic((short) 5);
	      
			break;
		
		case "senseAudioMic":
			System.out.println("Transfering wav file from robot to webservice ...");
	        System.out.println("Sound capture file create in " + Body.senseAudioMic() + "!");
	        
	        break;
	        
		case "actAudioStreamMic":
			System.out.println("Enabling audio stream to 131.130.244.35:1027 ...");
	        System.out.println("Open stream in vlc with rtp://@:1029/");
	        
	        Body.actAudioStreamMic(true, "131.130.244.35:1029");
	        
	        System.out.println("Sleeping 10 seconds ...");
	        try {
	        	  Thread.sleep(10000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Disabling audio stream ...");
	        
	        Body.actAudioStreamMic(false, "");
	        
	        break;
			
		case "senseColor":
			System.out.println("Enabling front color sensor ...");
	        Body.actColorSensors((short) 0);
	        System.out.println("Waiting for sensor ...");
	        System.out.println("Front color " + Body.senseColor()[0] + " " + Body.senseColor()[1] + " " + Body.senseColor()[2] + " " + Body.senseColor()[3] + "!");

	        System.out.println("Enabling back color sensor ...");
	        Body.actColorSensors((short) 1);
	        System.out.println("Waiting for sensor ...");
	        System.out.println("Back color " + Body.senseColor()[0] + " " + Body.senseColor()[1] + " " + Body.senseColor()[2] + " " + Body.senseColor()[3] + "!");
	        
	        break;
	    
		case "actDisplay":
			System.out.println("Displaying 8x8 matrix ...");
	        short[][] display = {
	        		{1,0,0,0,0,0,0,1},
	        		{0,1,0,0,0,0,1,0},
	        		{0,0,1,0,0,1,0,0},
	        		{0,0,0,1,1,0,0,0},
	        		{0,0,0,1,1,0,0,0},
	        		{0,0,1,0,0,1,0,0},
	        		{0,1,0,0,0,0,1,0},
	        		{1,0,0,0,0,0,0,1}};
	        
	        Body.actDisplay(display);
	        
	        break;
	        
		case "senseDistance":
			System.out.println("Sending distance measurement ping ...");
	        Body.actDistance();
	        System.out.println("Distance is " + Body.senseDistance() + "!");
			
			break;
			
		case "actLaser":
			System.out.println("Turning laser on ...");
		    Body.actLaser(true);
		    System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(500);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
		    System.out.println("Turning laser of ...");
		    Body.actLaser(false);
			
			break;
			
		case "actLCD":
	        System.out.println("Writing to 2x16 display ...");
	        short[] color = { 1, 1, 1 };
	        Body.actLCD("TEST            ", color);

			break;

		case "actMotor":
			System.out.println("Starting motor 1!");
	        Body.actMotor((short) 1, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        
	        System.out.println("Starting motor 2 ...");
	        Body.actMotor((short) 2, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Starting motor 3 ...");
	        
	        Body.actMotor((short) 3, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        
	        System.out.println("Starting motor 4 ...");
	        Body.actMotor((short) 4, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
			
			break;
			
		case "actNoise":
			System.out.println("Buzzing ...");
	        Body.actNoise((short) 5000);
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        
	        Body.actNoise((short) 0);

			break;
			
		case "actPlaySound":
			System.out.println("Transfering Sound ...");
	        System.out.println("Playing Sound ...");
	        Body.actPlaySound("sounds/received.wav");
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        
	        System.out.println("Playing Sound ...");
	        Body.actPlaySound();
	        
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
			
			break;
			
		case "senseDigitalLineSensors":
			System.out.println("Digital line Sensors " + Body.senseDigitalLineSensors()[0] + ", " + Body.senseDigitalLineSensors()[1] + ", " + Body.senseDigitalLineSensors()[2] + ", " + Body.senseDigitalLineSensors()[3] + "!");
			
			break;
			
		case "senseAnalogLineSensors":
			System.out.println("Analog line Sensors " + Body.senseAnalogLineSensors()[0] + ", " + Body.senseAnalogLineSensors()[1] + ", " + Body.senseAnalogLineSensors()[2] + ", " + Body.senseAnalogLineSensors()[3] + "!");
			
			break;
			
		case "senseAnalogWheelRotationSensors":
			System.out.println("Wheel Rotations " + Body.senseAnalogWheelRotationSensors().toString() + "!");
			
			break;
			
		case "senseDigitalNoise":
			System.out.println("Digital Noise " + Body.senseDigitalNoise() + "!");
			
			break;
			
		case "senseAnalogNoise": {
			int i = (Body.senseAnalogNoise() & 0xffff);
			System.out.println("Analog Noise " + i + "!");
			
			break;
		}
			
		case "senseHeadingMag":
			System.out.println("Heading " + Body.senseHeadingMag()[0] +":" + Body.senseHeadingMag()[1] + "!");
			
			break;
			
		case "senseRotGyrAcc":
			System.out.println("RotGyrAcc " + Body.senseRotGyrAcc()[0]/1000 + "," + Body.senseRotGyrAcc()[1]/1000 + "," + Body.senseRotGyrAcc()[2]/1000 + "," + Body.senseRotGyrAcc()[3]/1000 + "," + Body.senseRotGyrAcc()[4]/1000 + "," + Body.senseRotGyrAcc()[5]/1000 + "," + Body.senseRotGyrAcc()[6]/1000 + "," + Body.senseRotGyrAcc()[7]/1000 + "!");
			
			break;
			
		case "senseTemp": {
			float f = (Body.senseTemp() & 0xffff) / 1000;
			System.out.println("Temperature " + f + "!");
			
			break;
		}
			
		case "actServo":
			System.out.println("Servo rotation ..."); 
	        Body.actServo((short) 0, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo height ..."); 
	        Body.actServo((short) 1, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo forward ..."); 
	        Body.actServo((short) 2, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo grip ..."); 
	        Body.actServo((short) 3, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo flag ..."); 
	        Body.actServo((short) 15, EnumServoAngle.NEUTRAL);
			
			break;
			
		case "actVisionStreamCamera":
	        System.out.println("Enabling Video Stream");
	        System.out.println("Open in VLC with udp/h264://@:1027/");
	        Body.actVisionStreamCamera(true, "131.130.244.35:1027");
	        
	        System.out.println("Sleeping 10 seconds ...");
	        try {
	      	  Thread.sleep(10000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        
	        System.out.println("Disabling Video Stream");
	        Body.actVisionStreamCamera(false, "");
			
			break;
		
		case "actVisionCaptureCamera":
			System.out.println("Capturing Image ...");
	        Body.actVisionCaptureCamera();
	        
			break;

		case "senseVisionCamera":
			System.out.println("Transfering jpg file from robot to webservice ...");
	        System.out.println("Image capture file create in "+Body.senseVisionCamera());
			
			break;
			
		default:
	        System.out.println("Capturing 5 second wav file on robot ...");
			Body.actAudioCaptureMic((short) 5);
			System.out.println("Sleeping 5.5 seconds ...");
	        try {
	        	  Thread.sleep(5500);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
			        
	        System.out.println("Transfering wav file from robot to webservice ...");
	        System.out.println("Sound capture file create in " + Body.senseAudioMic() + "!");
	        
	        System.out.println("Enabling audio stream ...");
	        System.out.println("Corresponding sdp file:");
	        System.out.println("v=0");
	        System.out.println("o=pi IN IP4 localhost");
	        System.out.println("c=IN IP4 localhost");
	        System.out.println("s=STREAM");
	        System.out.println("m=audio 1209 /AVP 10");
	        System.out.println("a=rtpmap:10 S16LE/44100/2");
	        Body.actAudioStreamMic(true, "");
	        System.out.println("Sleeping 10 seconds ...");
	        try {
	        	  Thread.sleep(10000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Disabling audio stream ...");
	        Body.actAudioStreamMic(false, "");
	        
	        System.out.println("Enabling front color sensor ...");
	        Body.actColorSensors((short) 0);
	        System.out.println("Waiting for sensor ...");
	        System.out.println("Front color " + Body.senseColor().toString() + "!");
	
	        System.out.println("Enabling back color sensor ...");
	        Body.actColorSensors((short) 1);
	        System.out.println("Waiting for sensor ...");
	        System.out.println("Back color " + Body.senseColor().toString() + "!");
	        
	        System.out.println("Displaying 8x8 matrix ...");
	        short[][] disp = {
	        		{1,0,0,0,0,0,0,1},
	        		{0,1,0,0,0,0,1,0},
	        		{0,0,1,0,0,1,0,0},
	        		{0,0,0,1,1,0,0,0},
	        		{0,0,0,1,1,0,0,0},
	        		{0,0,1,0,0,1,0,0},
	        		{0,1,0,0,0,0,1,0},
	        		{1,0,0,0,0,0,0,1}};
	        Body.actDisplay(disp);
	        
	        System.out.println("Sending distance measurement ping ...");
	        Body.actDistance();
	        System.out.println("Distance is " + Body.senseDistance() + "!");
	        
	        System.out.println("Turning laser on ...");
	        Body.actLaser(true);
	        
	        System.out.println("Writing to 2x16 display ...");
	        short[] clr = { 1, 1, 1 };
	        Body.actLCD("TEST", clr);
	        
	        System.out.println("Starting motor 1!");
	        Body.actMotor((short) 1, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Starting motor 1 ...");
	        Body.actMotor((short) 2, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Starting motor 1 ...");
	        Body.actMotor((short) 3, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        System.out.println("Starting motor 1 ...");
	        Body.actMotor((short) 4, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
	        System.out.println("Sleeping 1 second ...");
	        try {
	        	  Thread.sleep(1000);
	        	} catch (InterruptedException ie) {
	        	    //Handle exception
	        	}
	        
	        System.out.println("Buzzing ...");
	        Body.actNoise((short) 5000);
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        Body.actNoise((short) 0);
	        
	        System.out.println("Transfering Sound ...");
	        System.out.println("Playing Sound ...");
	        Body.actPlaySound("sounds/received.wav");
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        System.out.println("Playing Sound ...");
	        Body.actPlaySound();
	        System.out.println("Sleeping 1 second ...");
	        try {
	      	  Thread.sleep(1000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        
	        System.out.println("Digital line Sensors " + Body.senseDigitalLineSensors().toString() + "!");
	        System.out.println("Analog line Sensors " + Body.senseAnalogLineSensors().toString() + "!");
	        System.out.println("Wheel Rotations " + Body.senseAnalogWheelRotationSensors().toString() + "!");
	        System.out.println("Digital Noise " + Body.senseDigitalNoise() + "!");
	        System.out.println("Heading " + Body.senseHeadingMag().toString() + "!");
	        System.out.println("RotGyrAcc " + Body.senseRotGyrAcc().toString() + "!");
	        System.out.println("Temperature " + Body.senseTemp() + "!");
	        
	        System.out.println("Servo rotation ..."); 
	        Body.actServo((short) 0, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo height ..."); 
	        Body.actServo((short) 1, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo forward ..."); 
	        Body.actServo((short) 2, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo grip ..."); 
	        Body.actServo((short) 3, EnumServoAngle.NEUTRAL);
	        System.out.println("Servo flag ..."); 
	        Body.actServo((short) 15, EnumServoAngle.NEUTRAL);
	        
	        System.out.println("Enabling Video Stream");
	        Body.actVisionStreamCamera(true, "");
	        System.out.println("Sleeping 10 seconds ...");
	        try {
	      	  Thread.sleep(10000);
	      	} catch (InterruptedException ie) {
	      	    //Handle exception
	      	}
	        System.out.println("Disabling Video Stream");
	        Body.actVisionStreamCamera(false, "");
	
	        System.out.println("Capturing Image ...");
	        Body.actVisionCaptureCamera();
	        System.out.println("Transfering jpg file from robot to webservice ...");
	        System.out.println("Image capture file create in "+Body.senseVisionCamera());
	        
	        break;
	        
		}
	}
}
