package org.omilab.robot.body;

import org.omilab.robot.interfaces.bodyworld.*;
import org.omilab.robot.interfaces.mindbody.*;
import org.omilab.robot.mind.Mind;
import org.omilab.robot.world.World;

import java.io.IOException;
import java.util.logging.Logger;


public class Body implements BodyWorldInterface, MindBodyInterface {
    private static final Logger log = Logger.getLogger(Body.class.getName());
    private ProximityModelBody proximityModelBody;
    private MoveModelBody moveModelBody;
    private VisionModelBody visionModelBody;
    private AudioModelBody audioModelBody;
    private ExpressionModelBody expressionModelBody;
    private World world;
    private Mind mindBackdoorAccess;

    public Body() {
        proximityModelBody = new ProximityModelBody();
        visionModelBody = new VisionModelBody();
        audioModelBody = new AudioModelBody();
        moveModelBody = new MoveModelBody();
        expressionModelBody = new ExpressionModelBody();
        world = new World();
    }

    public Body(EnumWorldAccessMethod WorldAccessMethod) {
        this();
        world.setAccessmethod(WorldAccessMethod);
    }

    //
    // *********** Body - world Interface ***********
    //

    @Override
    public void actAudioCaptureMic(short seconds) {
        world.actAudioCaptureMic(seconds);
    }

    @Override
    public void actAudioStreamMic(boolean on, String address) {
        world.actAudioStreamMic(on, address);
    }

    @Override
    public void actColorSensors(short number) {
        world.actColorSensors(number);
    }

    @Override
    public void actDisplay(short[][] s) {
        world.actDisplay(s);
    }

    @Override
    public void actDistance() {
        world.actDistance();
    }

    @Override
    public void actLaser(boolean on) {
        world.actLaser(on);
    }

    @Override
    public void actLCD(String text, short[] color) {
        world.actLCD(text, color);
    }

    @Override
    public void actMotor(short number, EnumMotorDirection Direction, short speed) {
        world.actMotor(number, Direction, speed);
    }

    @Override
    public void actNoise(short hz) {
        world.actNoise(hz);
    }

    @Override
    public void actPlaySound() {
        world.actPlaySound();
    }

    public void actPlaySound(String escapedpathandfilename) throws IOException {
        world.actPlaySound(escapedpathandfilename);
    }

    @Override
    public void actServo(short channel, EnumServoAngle ServoAngle) {
        world.actServo(channel, ServoAngle);
    }

    @Override
    public void actVisionCaptureCamera() {
        world.actVisionCaptureCamera();
    }

    @Override
    public void actVisionStreamCamera(boolean streaming, String address) {
        world.actVisionStreamCamera(streaming, address);
    }

    @Override
    public short[] senseAnalogLineSensors() throws IOException {
        return world.senseAnalogLineSensors();
    }

    @Override
    public short[] senseAnalogWheelRotationSensors() throws IOException {
        return world.senseAnalogWheelRotationSensors();
    }

    @Override
    public String senseAudioMic() throws IOException {
        return world.senseAudioMic();
    }

    @Override
    public short[] senseColor() throws IOException {
        return world.senseColor();
    }

    @Override
    public boolean[] senseDigitalLineSensors() throws IOException {
        return world.senseDigitalLineSensors();
    }

    @Override
    public boolean senseDigitalNoise() throws IOException {
        return world.senseDigitalNoise();
    }

    @Override
    public short senseDistance() throws IOException {
        return world.senseDistance();
    }

    @Override
    public short[] senseHeadingMag() throws IOException {
        return world.senseHeadingMag();
    }

    @Override
    public short[] senseRotGyrAcc() throws IOException {
        return world.senseRotGyrAcc();
    }

    @Override
    public short senseTemp() throws IOException {
        return world.senseTemp();
    }

    @Override
    public String senseVisionCamera() throws IOException {
        return world.senseVisionCamera();
    }

    //
    // *********** Mind - Body Interface ***********
    //

    public void setMind(Mind Mind) {
        mindBackdoorAccess = Mind;
    }

    public AudioModelBody getAudioModelBody() {
        return audioModelBody;
    }

    public MoveModelBody getMoveModelBody() {
        return moveModelBody;
    }

    public ProximityModelBody getProximityModelBody() {
        return proximityModelBody;
    }

    public VisionModelBody getVisionModelBody() {
        return visionModelBody;
    }

    public void pufferMotorCommand(boolean on) {
        world.setPufferMotorCommand(on);
    }

    @Override
    public void desymbolizeAudioModel() {
        if (!mindBackdoorAccess.getAudioModelMind().isStreaming()) {
            if (audioModelBody.isStreaming()) {
                actAudioStreamMic(false, "");
                audioModelBody.setStreaming(false);
            } else
                actAudioCaptureMic((short) 10);
        } else if (!audioModelBody.isStreaming()) {
            actAudioStreamMic(true, "");
            audioModelBody.setStreaming(true);
        }
    }

    @Override
    public void desymbolizeExpressionModel() {
        if (mindBackdoorAccess.getExpressionModelMind().isSetDisplay()) {
            short[][] s = mindBackdoorAccess.getExpressionModelMind().getDisplay();
            short[][] sFlipped = new short[8][8];

            if (mindBackdoorAccess.getExpressionModelMind().getDisplayColor() < 4)
                for (int i = 0; i < s.length; i++)
                    for (int j = 0; j < s[i].length; j++) {
                        if (s[i][j] > 0)
                            s[i][j] = (short) (s[i][j] + mindBackdoorAccess.getExpressionModelMind().getDisplayColor() - 1);
                        sFlipped[7 - i][7 - j] = s[i][j];
                    }
            if (mindBackdoorAccess.getExpressionModelMind().isFlippedDisplay())
                actDisplay(sFlipped);
            else
                actDisplay(s);
        }

        if (mindBackdoorAccess.getExpressionModelMind().isSetLCD()) {
            short[] color = new short[3];
            color[0] = (short) (mindBackdoorAccess.getExpressionModelMind().getLcdColor()[0] / 255);
            color[1] = (short) (mindBackdoorAccess.getExpressionModelMind().getLcdColor()[1] / 255);
            color[2] = (short) (mindBackdoorAccess.getExpressionModelMind().getLcdColor()[2] / 255);
            actLCD(mindBackdoorAccess.getExpressionModelMind().getLcd(), color);
        }

        if (mindBackdoorAccess.getExpressionModelMind().isSetNoise()) {
            //TODO Buzz
        }
    }

    @Override
    public void desymbolizeMoveModel() {
        short speed = (short) mindBackdoorAccess.getMoveModelMind().getSpeed();
        switch (mindBackdoorAccess.getMoveModelMind().getDirection()) {
            case WD4FORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.FORWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD4BACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.BACKWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD4STOP:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD4LEFTTURN:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.FORWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD4RIGHTTURN:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.BACKWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2LEFTSIDEFORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2LEFTSIDEBACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2RIGHTSIDEFORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.FORWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2RIGHTSIDEBACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.BACKWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2FRONTFORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2FRONTBACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2BACKFORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.FORWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.FORWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD2BACKBACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.BACKWARD);
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.BACKWARD);
                world.setPufferMotorCommand(true);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                world.setPufferMotorCommand(false);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;

            case WD1LEFTFRONTFORWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.FORWARD);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                break;
            case WD1LEFTFRONTBACKWARD:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.BACKWARD);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                break;
            case WD1RIGHTFRONTFORWARD:
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.FORWARD);
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                break;
            case WD1RIGHTFRONTBACKWARD:
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.BACKWARD);
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                break;
            case WD1LEFTBACKFORWARD:
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.FORWARD);
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                break;
            case WD1LEFTBACKBACKWARD:
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.BACKWARD);
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                break;
            case WD1RIGHTBACKFORWARD:
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.FORWARD);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
            case WD1RIGHTBACKBACKWARD:
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.BACKWARD);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;

            case WD1LEFTFRONTSTOP:
                moveModelBody.setSpd1(speed);
                moveModelBody.setDir1(EnumMotorDirection.STOP);
                world.actMotor((short) 1, moveModelBody.getDir1(), moveModelBody.getSpd1());
                break;
            case WD1LEFTBACKSTOP:
                moveModelBody.setSpd2(speed);
                moveModelBody.setDir2(EnumMotorDirection.STOP);
                world.actMotor((short) 2, moveModelBody.getDir2(), moveModelBody.getSpd2());
                break;
            case WD1RIGHTFRONTSTOP:
                moveModelBody.setSpd3(speed);
                moveModelBody.setDir3(EnumMotorDirection.STOP);
                world.actMotor((short) 3, moveModelBody.getDir3(), moveModelBody.getSpd3());
                break;
            case WD1RIGHTBACKSTOP:
                moveModelBody.setSpd4(speed);
                moveModelBody.setDir4(EnumMotorDirection.STOP);
                world.actMotor((short) 4, moveModelBody.getDir4(), moveModelBody.getSpd4());
                break;
        }

        if (mindBackdoorAccess.getMoveModelMind().getGesture() != null) {
            switch (mindBackdoorAccess.getMoveModelMind().getGesture()) {
                case RIGHT:
                    moveModelBody.setServ1(EnumServoAngle.MINUS30);
                    break;
                case LEFT:
                    moveModelBody.setServ1(EnumServoAngle.PLUS30);
                    break;
                case FORWARD:
                    moveModelBody.setServ2(EnumServoAngle.PLUS30);
                    break;
                case BACKWARD:
                    moveModelBody.setServ2(EnumServoAngle.MINUS30);
                    break;
                case UP:
                    moveModelBody.setServ3(EnumServoAngle.MINUS30);
                    break;
                case DOWN:
                    moveModelBody.setServ3(EnumServoAngle.PLUS30);
                    break;
                case OPEN:
                    moveModelBody.setServ4(EnumServoAngle.MINUS30);
                    break;
                case CLOSE:
                    moveModelBody.setServ4(EnumServoAngle.PLUS30);
                    break;
            }

            if (mindBackdoorAccess.getMoveModelMind().getGesture() != EnumMoveModelArm.IDLE) {
                world.actServo((short) 0, moveModelBody.getServ1());
                world.actServo((short) 1, moveModelBody.getServ2());
                world.actServo((short) 2, moveModelBody.getServ3());
                world.actServo((short) 3, moveModelBody.getServ4());
                world.actServo((short) 14, moveModelBody.getServ15());

                mindBackdoorAccess.getMoveModelMind().setGesture(EnumMoveModelArm.IDLE);
            }
        }

    }

    @Override
    public void desymbolizeProximityModel() {
        proximityModelBody.setActiveRgb(mindBackdoorAccess.getProximityModelMind().getActiveRgb());
        if (proximityModelBody.getActiveRgb() == 1)
            actColorSensors((short) 7);
        else if (proximityModelBody.getActiveRgb() == 2)
            actColorSensors((short) 6);

        actDistance();
        // TODO Auto-generated method stub
        //here we set Magnetic Measurement Declination and Scale
        //here we set RGB Measurement Time

    }

    @Override
    public void desymbolizeVisionModel() {
        if (!mindBackdoorAccess.getVisionModelMind().isStreaming()) {
            if (visionModelBody.isStreaming()) {
                actVisionStreamCamera(false, "");
                visionModelBody.setStreaming(false);
            } else
                actVisionCaptureCamera();
        } else if (!visionModelBody.isStreaming()) {
            actVisionStreamCamera(true, "");
            visionModelBody.setStreaming(true);
        }
    }

    @Override
    public void symbolizeAudioModel() throws IOException {
        if (!audioModelBody.isStreaming())
            audioModelBody.setPathToWavFile(senseAudioMic());
        // else
        // TODO Here we would process a audio stream
    }

    @Override
    public void symbolizeExpressionModel() throws IOException {
        expressionModelBody.setNoiselevel(senseDigitalNoise() ? 1 : 0);
    }

    @Override
    public void symbolizeMoveModel() throws IOException {
        short[] s = senseAnalogWheelRotationSensors();
        moveModelBody.setW1(s[0]);
        moveModelBody.setW2(s[1]);
        moveModelBody.setW3(s[2]);
        moveModelBody.setW4(s[3]);
    }

    @Override
    public void symbolizeProximityModel() throws IOException {
        boolean[] b = senseDigitalLineSensors();
        proximityModelBody.setLeftOuterReflection(b[0]);
        proximityModelBody.setLeftInnerReflection(b[1]);
        proximityModelBody.setRightOuterReflection(b[2]);
        proximityModelBody.setRightInnerReflection(b[3]);

        short[] s = senseAnalogLineSensors();
        proximityModelBody.setLeftOuterReflectionValue(s[0]);
        proximityModelBody.setLeftInnerReflectionValue(s[1]);
        proximityModelBody.setRightOuterReflectionValue(s[2]);
        proximityModelBody.setRightInnerReflectionValue(s[3]);

        if (proximityModelBody.getActiveRgb() != -1) {
            short[] rgb;
            rgb = senseColor();
            proximityModelBody.setRgb(rgb[0], rgb[1], rgb[2], rgb[3], rgb[4], rgb[5]);
        }

        proximityModelBody.setDistance(senseDistance());

        s = this.senseRotGyrAcc();
        proximityModelBody.setRotGyrAcc(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]);

        s = this.senseHeadingMag();
        proximityModelBody.setHeadingMin(s[0]);
        proximityModelBody.setHeadingSec(s[1]);
    }

    @Override
    public void symbolizeVisionModel() throws IOException {
        if (!visionModelBody.isStreaming())
            visionModelBody.setPathToJpgFile(senseVisionCamera());
        // else
        // TODO Here we would process a video stream
    }

    @Override
    public void death() {
        world.death();
    }

    @Override
    public short senseAnalogNoise() throws IOException {
        return world.senseAnalogNoise();
    }

}
