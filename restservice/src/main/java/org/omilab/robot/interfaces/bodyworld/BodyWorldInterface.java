package org.omilab.robot.interfaces.bodyworld;

import java.io.IOException;

public interface BodyWorldInterface {
	//void actVisionCaptureAreal(); //TODO
	//void actVisionStreamAreal(); //TODO
	//void actAudioCaptureAreal(); //TODO
	//void actAudioStreamAreal(); //TODO
	//void actVisionProjector(String file); //TODO
	
	/**
	 * Starts recording a wav file
	 * 
	 * @param seconds
	 *            Recording time in milliseconds
	 */
	void actAudioCaptureMic(short seconds);

	/**
	 * Turns audio streaming on or off
	 * 
	 * @param on
	 * @param address ip:port to broadcast to
	 */
	void actAudioStreamMic(boolean on, String address);

	/**
	 * MUX the color sensors
	 * 
	 * @param number
	 *            Sensor to be activated
	 */
	void actColorSensors(short number);

	/**
	 * Displays an image on the 8x8 bicolor LED matrix
	 * 
	 * @param matrix
	 *            8x8 x (0 - 3, 0 := Off, 1 := Green, 2 := Red, 3 :=Yellow)
	 */
	void actDisplay(short[][] matrix);

	/**
	 * Sends distance measurement pings for one measurement
	 */
	void actDistance();
	
	/**
	 * Enables or disables the laser
	 * 
	 * @param on 
	 */
	void actLaser(boolean on);

	/**
	 * Writes text to the LCD Display
	 * 
	 * @param text
	 *            Text to be displayed
	 * @param color
	 *            RGB 3 x (0 or 1)
	 */
	void actLCD(String text, short[] color);

	/**
	 * Controls the DC/Stepper Motors
	 * 
	 * @param number
	 *            Number of Motor (1 := front left, 2 := front right, 3 := back
	 *            left, 4 := back right)
	 * @param Direction
	 *            (1 := forward, 2 := backward, 4 := stop)
	 * @param speed
	 *            (0-255)
	 */
	void actMotor(short number, EnumMotorDirection Direction, short speed);

	/**
	 * Activates or deactivates the buzz sensor
	 * 
	 * @param herz
	 *            Herz
	 */
	void actNoise(short herz);

	/**
	 * Plays a WAV file
	 */
	void actPlaySound();

	/**
	 * Transfers a WAV file and plays it
	 */
	void actPlaySound(String escapedPathAndFilename) throws IOException;

	/**
	 * Controls the Servo Motors
	 * 
	 * @param channel
	 *            Number of Servo
	 * @param ServoAngle
	 *            Rotation of Servo in pulse duration
	 */
	void actServo(short channel, EnumServoAngle ServoAngle);

	/**
	 * Takes a picture with the raspberry pi camera
	 */
	void actVisionCaptureCamera();

	/**
	 * Turns video streaming on or off
	 * 
	 * @param on
	 * @param address ip:port to broadcast to
	 */
	void actVisionStreamCamera(boolean on, String address);
	
	/**
	 * Disables the robot by turning off the socket server
	 * 
	 */
	void death();

	/**
	 * Senses 4 line/reflection sensors
	 * 
	 * @return Data from 4 line/reflection sensors (left to right)
	 */
	short[] senseAnalogLineSensors() throws IOException;
	
	/**
	 * Senses Noise
	 * 
	 * @return Unsigned short noise value
	 */
	short senseAnalogNoise() throws IOException;

	/**
	 * Senses 4 line/reflection sensors
	 * 
	 * @return Data from 4 line/reflection sensors (left to right)
	 */
	short[] senseAnalogWheelRotationSensors() throws IOException;

	/**
	 * Senses the wav file from the last actAudioCaptureMic() call
	 * 
	 * @return Path to file
	 */
	String senseAudioMic() throws IOException;

	/**
	 * Senses the color sensor selected by actColorSensors(short number); Sensor
	 * performs better with longer measurement duration.
	 *
	 * @return [c,r,g,b,kelvin,lux] Kelvin & Lux are unsigned shorts
	 */
	short[] senseColor() throws IOException;

	/**
	 * Senses 4 line/reflection sensors
	 * 
	 * @return Data from 4 line/reflection sensors (left to right, true := line
	 *         detected, no reflection)
	 */
	boolean[] senseDigitalLineSensors() throws IOException;

	/**
	 * Senses distance data since last ActDistance() call
	 * 
	 * @return
	 */
	short senseDistance() throws IOException;

	/**
	 * Senses Magnetometer Data
	 * 
	 * @return
	 */
	short[] senseHeadingMag() throws IOException;

	/**
	 * Senses Noise
	 * 
	 * @return
	 */
	boolean senseDigitalNoise() throws IOException;

	/**
	 * Senses accelerometer
	 * 
	 * @return Data from accelerometer: xrot, yrot, xgyr, ygyr, zgyr, xacc,
	 *         yacc, zacc
	 */
	short[] senseRotGyrAcc() throws IOException;
	
	/**
	 * Senses Temperature
	 * 
	 * @return Unsigned short value in Celsius
	 */
	short senseTemp() throws IOException;

	/**
	 * Senses the picture from the last actVisionCaptureCamera() call
	 * 
	 * @return Path to file
	 */
	String senseVisionCamera() throws IOException;
}
