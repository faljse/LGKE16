package org.omilab.robot.test;

import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.bodyworld.EnumServoAngle;

import java.io.IOException;

public class Test {
    public void testBody(Body body, String system) {
        if (body == null) {
            System.out.println("System not initiated");
            return;
        }

        switch (system) {
            case "death":
                System.out.println("Killing the robot ...");
                body.death();
                break;

            case "actAudioCaptureMic":
                System.out.println("Capturing 5 second wav file on robot ...");
                body.actAudioCaptureMic((short) 5);

                break;

            case "senseAudioMic":
                System.out.println("Transfering wav file from robot to webservice ...");
                try {
                    System.out.println("Sound capture file create in " + body.senseAudioMic() + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "actAudioStreamMic":
                System.out.println("Enabling audio stream to 131.130.244.35:1027 ...");
                System.out.println("Open stream in vlc with rtp://@:1029/");

                body.actAudioStreamMic(true, "131.130.244.35:1029");

                System.out.println("Sleeping 10 seconds ...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }
                System.out.println("Disabling audio stream ...");

                body.actAudioStreamMic(false, "");

                break;

            case "senseColor":
                try {
                    System.out.println("Enabling front color sensor ...");
                    body.actColorSensors((short) 0);
                    System.out.println("Waiting for sensor ...");

                    System.out.println("Front color " + body.senseColor()[0] + " " + body.senseColor()[1] + " " + body.senseColor()[2] + " " + body.senseColor()[3] + "!");


                    System.out.println("Enabling back color sensor ...");
                    body.actColorSensors((short) 1);
                    System.out.println("Waiting for sensor ...");
                    System.out.println("Back color " + body.senseColor()[0] + " " + body.senseColor()[1] + " " + body.senseColor()[2] + " " + body.senseColor()[3] + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "actDisplay":
                System.out.println("Displaying 8x8 matrix ...");
                short[][] display = {
                        {1, 0, 0, 0, 0, 0, 0, 1},
                        {0, 1, 0, 0, 0, 0, 1, 0},
                        {0, 0, 1, 0, 0, 1, 0, 0},
                        {0, 0, 0, 1, 1, 0, 0, 0},
                        {0, 0, 0, 1, 1, 0, 0, 0},
                        {0, 0, 1, 0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 0, 0, 0, 1}};

                body.actDisplay(display);

                break;

            case "senseDistance":
                System.out.println("Sending distance measurement ping ...");
                body.actDistance();
                try {
                    System.out.println("Distance is " + body.senseDistance() + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "actLaser":
                System.out.println("Turning laser on ...");
                body.actLaser(true);
                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    //Handle exception
                }
                System.out.println("Turning laser of ...");
                body.actLaser(false);

                break;

            case "actLCD":
                System.out.println("Writing to 2x16 display ...");
                short[] color = {1, 1, 1};
                body.actLCD("TEST            ", color);

                break;

            case "actMotor":
                System.out.println("Starting motor 1!");
                body.actMotor((short) 1, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                System.out.println("Starting motor 2 ...");
                body.actMotor((short) 2, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }
                System.out.println("Starting motor 3 ...");

                body.actMotor((short) 3, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                System.out.println("Starting motor 4 ...");
                body.actMotor((short) 4, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                break;

            case "actNoise":
                System.out.println("Buzzing ...");
                body.actNoise((short) 5000);

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                body.actNoise((short) 0);

                break;

            case "actPlaySound":
                System.out.println("Transfering Sound ...");
                System.out.println("Playing Sound ...");
                try {
                    body.actPlaySound("sounds/received.wav");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                System.out.println("Playing Sound ...");
                body.actPlaySound();

                System.out.println("Sleeping 1 second ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                break;

            case "senseDigitalLineSensors":
                try {
                    System.out.println("Digital line Sensors " + body.senseDigitalLineSensors()[0] + ", " + body.senseDigitalLineSensors()[1] + ", " + body.senseDigitalLineSensors()[2] + ", " + body.senseDigitalLineSensors()[3] + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "senseAnalogLineSensors":
                try {
                    System.out.println("Analog line Sensors " + body.senseAnalogLineSensors()[0] + ", " + body.senseAnalogLineSensors()[1] + ", " + body.senseAnalogLineSensors()[2] + ", " + body.senseAnalogLineSensors()[3] + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "senseAnalogWheelRotationSensors":
                try {
                    System.out.println("Wheel Rotations " + body.senseAnalogWheelRotationSensors().toString() + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "senseDigitalNoise":
                try {
                    System.out.println("Digital Noise " + body.senseDigitalNoise() + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "senseAnalogNoise": {
                int i = 0;
                try {
                    i = (body.senseAnalogNoise() & 0xffff);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Analog Noise " + i + "!");

                break;
            }

            case "senseHeadingMag":
                try {
                    System.out.println("Heading " + body.senseHeadingMag()[0] + ":" + body.senseHeadingMag()[1] + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "senseRotGyrAcc":
                try {
                    System.out.println("RotGyrAcc " + body.senseRotGyrAcc()[0] / 1000 + "," + body.senseRotGyrAcc()[1] / 1000 + "," + body.senseRotGyrAcc()[2] / 1000 + "," + body.senseRotGyrAcc()[3] / 1000 + "," + body.senseRotGyrAcc()[4] / 1000 + "," + body.senseRotGyrAcc()[5] / 1000 + "," + body.senseRotGyrAcc()[6] / 1000 + "," + body.senseRotGyrAcc()[7] / 1000 + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "senseTemp": {
                float f = 0;
                try {
                    f = (body.senseTemp() & 0xffff) / 1000;
                    System.out.println("Temperature " + f + "!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "actServo":
                System.out.println("Servo rotation ...");
                body.actServo((short) 0, EnumServoAngle.NEUTRAL);
                System.out.println("Servo height ...");
                body.actServo((short) 1, EnumServoAngle.NEUTRAL);
                System.out.println("Servo forward ...");
                body.actServo((short) 2, EnumServoAngle.NEUTRAL);
                System.out.println("Servo grip ...");
                body.actServo((short) 3, EnumServoAngle.NEUTRAL);
                System.out.println("Servo flag ...");
                body.actServo((short) 15, EnumServoAngle.NEUTRAL);

                break;

            case "actVisionStreamCamera":
                System.out.println("Enabling Video Stream");
                System.out.println("Open in VLC with udp/h264://@:1027/");
                body.actVisionStreamCamera(true, "131.130.244.35:1027");

                System.out.println("Sleeping 10 seconds ...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                System.out.println("Disabling Video Stream");
                body.actVisionStreamCamera(false, "");

                break;

            case "actVisionCaptureCamera":
                System.out.println("Capturing Image ...");
                body.actVisionCaptureCamera();

                break;

            case "senseVisionCamera":
                System.out.println("Transfering jpg file from robot to webservice ...");
                try {
                    System.out.println("Image capture file create in " + body.senseVisionCamera());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                try {
                    System.out.println("Capturing 5 second wav file on robot ...");
                    body.actAudioCaptureMic((short) 5);
                    System.out.println("Sleeping 5.5 seconds ...");
                    Thread.sleep(5500);

                    System.out.println("Transfering wav file from robot to webservice ...");
                    System.out.println("Sound capture file create in " + body.senseAudioMic() + "!");

                    System.out.println("Enabling audio stream ...");
                    System.out.println("Corresponding sdp file:");
                    System.out.println("v=0");
                    System.out.println("o=pi IN IP4 localhost");
                    System.out.println("c=IN IP4 localhost");
                    System.out.println("s=STREAM");
                    System.out.println("m=audio 1209 /AVP 10");
                    System.out.println("a=rtpmap:10 S16LE/44100/2");
                    body.actAudioStreamMic(true, "");
                    System.out.println("Sleeping 10 seconds ...");
                    Thread.sleep(10000);
                    System.out.println("Disabling audio stream ...");
                    body.actAudioStreamMic(false, "");

                    System.out.println("Enabling front color sensor ...");
                    body.actColorSensors((short) 0);
                    System.out.println("Waiting for sensor ...");
                    System.out.println("Front color " + body.senseColor().toString() + "!");

                    System.out.println("Enabling back color sensor ...");
                    body.actColorSensors((short) 1);
                    System.out.println("Waiting for sensor ...");
                    System.out.println("Back color " + body.senseColor().toString() + "!");

                    System.out.println("Displaying 8x8 matrix ...");
                    short[][] disp = {
                            {1, 0, 0, 0, 0, 0, 0, 1},
                            {0, 1, 0, 0, 0, 0, 1, 0},
                            {0, 0, 1, 0, 0, 1, 0, 0},
                            {0, 0, 0, 1, 1, 0, 0, 0},
                            {0, 0, 0, 1, 1, 0, 0, 0},
                            {0, 0, 1, 0, 0, 1, 0, 0},
                            {0, 1, 0, 0, 0, 0, 1, 0},
                            {1, 0, 0, 0, 0, 0, 0, 1}};
                    body.actDisplay(disp);

                    System.out.println("Sending distance measurement ping ...");
                    body.actDistance();
                    System.out.println("Distance is " + body.senseDistance() + "!");

                    System.out.println("Turning laser on ...");
                    body.actLaser(true);

                    System.out.println("Writing to 2x16 display ...");
                    short[] clr = {1, 1, 1};
                    body.actLCD("TEST", clr);

                    System.out.println("Starting motor 1!");
                    body.actMotor((short) 1, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    System.out.println("Starting motor 1 ...");
                    body.actMotor((short) 2, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    System.out.println("Starting motor 1 ...");
                    body.actMotor((short) 3, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    System.out.println("Starting motor 1 ...");
                    body.actMotor((short) 4, org.omilab.robot.interfaces.bodyworld.EnumMotorDirection.FORWARD, (short) 255);
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    System.out.println("Buzzing ...");
                    body.actNoise((short) 5000);
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    body.actNoise((short) 0);

                    System.out.println("Transfering Sound ...");
                    System.out.println("Playing Sound ...");
                    body.actPlaySound("sounds/received.wav");
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);
                    System.out.println("Playing Sound ...");
                    body.actPlaySound();
                    System.out.println("Sleeping 1 second ...");
                    Thread.sleep(1000);

                    System.out.println("Digital line Sensors " + body.senseDigitalLineSensors().toString() + "!");
                    System.out.println("Analog line Sensors " + body.senseAnalogLineSensors().toString() + "!");
                    System.out.println("Wheel Rotations " + body.senseAnalogWheelRotationSensors().toString() + "!");
                    System.out.println("Digital Noise " + body.senseDigitalNoise() + "!");
                    System.out.println("Heading " + body.senseHeadingMag().toString() + "!");
                    System.out.println("RotGyrAcc " + body.senseRotGyrAcc().toString() + "!");
                    System.out.println("Temperature " + body.senseTemp() + "!");

                    System.out.println("Servo rotation ...");
                    body.actServo((short) 0, EnumServoAngle.NEUTRAL);
                    System.out.println("Servo height ...");
                    body.actServo((short) 1, EnumServoAngle.NEUTRAL);
                    System.out.println("Servo forward ...");
                    body.actServo((short) 2, EnumServoAngle.NEUTRAL);
                    System.out.println("Servo grip ...");
                    body.actServo((short) 3, EnumServoAngle.NEUTRAL);
                    System.out.println("Servo flag ...");
                    body.actServo((short) 15, EnumServoAngle.NEUTRAL);

                    System.out.println("Enabling Video Stream");
                    body.actVisionStreamCamera(true, "");
                    System.out.println("Sleeping 10 seconds ...");
                    Thread.sleep(10000);
                    System.out.println("Disabling Video Stream");
                    body.actVisionStreamCamera(false, "");

                    System.out.println("Capturing Image ...");
                    body.actVisionCaptureCamera();
                    System.out.println("Transfering jpg file from robot to webservice ...");
                    System.out.println("Image capture file create in " + body.senseVisionCamera());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
