package org.omilab.robot.world;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SocketClient {
    private static final Logger log = Logger.getLogger(SocketClient.class.getName());

    public String host = "";
    public int port = 1025;

    static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        while (len != -1) {
            out.write(buf, 0, len);
            len = in.read(buf);
        }
    }

    private short[] socketCommunicationShorts(byte command, short[] inputs, int countOutputShorts) {
        short[] ret = new short[countOutputShorts];
        ByteBuffer bb = null;
        byte[] b = null;

        if (inputs != null) {
            bb = ByteBuffer.allocate(2 * inputs.length);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            for (short s : inputs)
                bb.putShort(s);
            b = new byte[2 * inputs.length];
            bb.position(0);
            bb.get(b, 0, 2 * inputs.length);
        }

        if (bb != null)
            bb.clear();
        bb = ByteBuffer.wrap(socketCommunicationBytes(command, b, countOutputShorts * 2), 0, countOutputShorts * 2);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < countOutputShorts; i++) {
            ret[i] = bb.getShort(i * 2);
        }

        return ret;
    }

    private short[] socketCommunicationBytesinputShortsoutput(byte command, byte[] inputs, int countOutputShorts) {
        short[] ret = new short[countOutputShorts];
        ByteBuffer bb = null;

        bb = ByteBuffer.wrap(socketCommunicationBytes(command, inputs, countOutputShorts * 2), 0, countOutputShorts * 2);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < countOutputShorts; i++)
            ret[i] = bb.getShort(i * 2);

        return ret;
    }

    private byte[] socketCommunicationBytes(byte command, byte[] inputs, int countOutputBytes) {
        Socket soc = null;
        byte[] ret = new byte[countOutputBytes];

        try {
            soc = new Socket(host, port);

            //send command
            soc.getOutputStream().write(command);
            soc.getOutputStream().flush();

            //send command inputs
            if (inputs != null) {
                for (int i = 0; i < inputs.length; i++)
                    soc.getOutputStream().write(inputs[i]);
                soc.getOutputStream().flush();
            }

            //wait for command outputs
            if (countOutputBytes != 0) {

                int i = soc.getInputStream().read(ret, 0, countOutputBytes);
                while (i < countOutputBytes)
                    i += soc.getInputStream().read(ret, i, countOutputBytes - i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Server receives a file from the client
     *
     * @param escapedPathAndFilename
     * @param mode
     */
    private void receiveFile(String escapedPathAndFilename, byte mode, int portoffset) throws IOException {
        Socket soc = null;
        Socket soc2 = null;

        soc = new Socket(host, port);
        byte[] b = {(byte) 'f'};
        soc.getOutputStream().write(b);
        soc.getOutputStream().flush();

        soc.getOutputStream().write(mode);
        soc.getOutputStream().flush();

        //wait for thread to spawn on server
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        soc2 = new Socket(host, port + portoffset); //Is the 2nd port necessary?
        File file = new File(escapedPathAndFilename);
        InputStream in = new FileInputStream(file);

        byte[] buffer = new byte[16 * 1024];
        int count = in.read(buffer);
        while (count > 0) {
            soc2.getOutputStream().write(buffer, 0, count);
            count = in.read(buffer);
        }
        soc2.getOutputStream().flush();

        if (soc.getInputStream().read() == 0) //Why?
            log.severe("Error in sendFile command, check server status!");
        in.close();
    }

    /**
     * Server sends a file to the client
     *
     * @param path
     * @param mode
     * @param portoffset
     * @return
     */
    private void sendFile(String path, byte mode, int portoffset) {
        Socket soc = null;
        Socket soc2 = null;

        try {
            soc = new Socket(host, port);
            byte[] b = {(byte) 'F'};
            soc.getOutputStream().write(b);
            soc.getOutputStream().flush();

            soc.getOutputStream().write(mode);
            soc.getOutputStream().flush();

            //wait for thread to spawn on server
            Thread.sleep(100);

            soc2 = new Socket(host, port + portoffset);
            OutputStream fout = new FileOutputStream(path);
            copy(soc2.getInputStream(), fout);
            fout.flush();
            fout.close();

            if (soc.getInputStream().read() == 0)
                log.severe("Error in receiveImage command, check server status!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                soc2.close();
            } catch (Exception e) {
            }
            try {
                soc.close();
            } catch (Exception e) {
            }
        }
    }

    private byte[] addressToBytes(String address) {
        byte[] b = null;

        if (address != null && address.length() != 0) {
            b = new byte[6];
            short s;

            String ipport[] = address.split(":");
            String ip[] = ipport[0].split("\\.");

            s = Short.parseShort(ip[0]);
            b[0] = (byte) s;
            s = Short.parseShort(ip[1]);
            b[1] = (byte) s;
            s = Short.parseShort(ip[2]);
            b[2] = (byte) s;
            s = Short.parseShort(ip[3]);
            b[3] = (byte) s;
            s = Short.parseShort(ipport[1]);
            b[4] = (byte) s;
            b[5] = (byte) (s >> 8);
        } else {
            b = new byte[1];
            b[0] = (byte) 0;
        }

        return b;
    }

    public void connect() {
        if (socketCommunicationBytes((byte) 'c', null, 1)[0] != 0)
            System.out.println("Connection Successful!");
        else
            System.out.println("Error in connect command, check server status!");
    }

    public void disableAudioStream(String address) {
        if (socketCommunicationBytes((byte) 'a', addressToBytes(address), 1)[0] == 0)
            System.out.println("Error in disableAudioStream command, check server status!");
    }

    public void disableVideoStream(String address) {
        if (socketCommunicationBytes((byte) 'v', addressToBytes(address), 1)[0] == 0)
            System.out.println("Error in disableVideoStream command, check server status!");
    }

    public void drive(short spd1, short spd2, short spd3, short spd4, short dir1, short dir2, short dir3, short dir4) {
        if (socketCommunicationBytes((byte) 'D', new byte[]{(byte) spd1, (byte) spd2, (byte) spd3, (byte) spd4, (byte) dir1, (byte) dir2, (byte) dir3, (byte) dir4}, 1)[0] == 0)
            System.out.println("Error in drive command, check server status!");
    }

    public void enableAudioStream(String address) {
        if (socketCommunicationBytes((byte) 'A', addressToBytes(address), 1)[0] == 0)
            System.out.println("Error in enableAudioStream command, check server status!");
    }

    public void enableVideoStream(String address) {
        if (socketCommunicationBytes((byte) 'V', addressToBytes(address), 1)[0] == 0)
            System.out.println("Error in enableStream command, check server status!");
    }

    public short[] getADCAllChannelValues(char ADCIndentifier) {
        return socketCommunicationShorts((byte) ADCIndentifier, null, 4);
    }

    public short getADCsingleChannelValue(char ADCIndentifier, short number) {
        return socketCommunicationBytesinputShortsoutput((byte) ADCIndentifier, new byte[]{(byte) number}, 1)[0];
    }

    public short getDistance() {
        return socketCommunicationShorts((byte) 'T', null, 1)[0];
    }

    public short[] getColorSensorValues() {
        return socketCommunicationShorts((byte) 'R', null, 6);
    }

    public short[] getHeading() {
        return socketCommunicationShorts((byte) 'H', null, 2);
    }

    public short[] getRotationGyroAccelerometer() {
        return socketCommunicationShorts((byte) 'G', null, 8);
    }

    public void initiate() {
        if (socketCommunicationBytes((byte) 'i', new byte[]{(byte) 0, (byte) 0}, 1)[0] != 0)
            System.out.println("Socket Server Initialization Successful!");
        else
            System.out.println("Error in initiate command, check server status!");
    }

    public void muxI2C(short number) {
        if (socketCommunicationBytes((byte) 'X', new byte[]{(byte) number}, 1)[0] == 0)
            System.out.println("Error in muxI2C command, check server status!");
    }

    public void quit() {
        if (socketCommunicationBytes((byte) 'q', null, 1)[0] != 0)
            System.out.println("Server Shutdown!");
        else
            System.out.println("Error in initiate command, check server status!");
    }

    public void playWav() {
        if (socketCommunicationBytes((byte) 'P', null, 1)[0] == 0)
            System.out.println("Error in playWav command, check server status!");
    }

    /**
     * Server sends an jpg to the client
     *
     * @param imagePath File to be sent
     */
    public void sendImage(String imagePath) {
        sendFile(imagePath, (byte) 'I', 1);
    }

    /**
     * Server sends wav file to the client
     *
     * @param soundPath File to be sent
     */
    public void sendSound(String soundPath) {
        sendFile(soundPath, (byte) 'A', 3);
    }

    /**
     * Server receives wav file from the client
     *
     * @param imagePath Path and filename of the file on the client
     */
    public void receiveSound(String imagePath) throws IOException {
        receiveFile(imagePath, (byte) 'A', 5);
    }

    public void recordCameraJPG() {
        if (socketCommunicationBytes((byte) 'I', null, 1)[0] == 0)
            System.out.println("Error in recordCameraJPG command, check server status!");
    }

    public void recordMicWAV(short durationSeconds) {
        if (socketCommunicationBytes((byte) 'S', new byte[]{(byte) durationSeconds, (byte) (durationSeconds >> 8)}, 1)[0] == 0)
            System.out.println("Error in recordMicWAV command, check server status!");
    }

    public void setLCDDisplay(String text, short[] color) {
        byte[] inputs = new byte[3 + 32];

        inputs[0] = (byte) color[0];
        inputs[1] = (byte) color[1];
        inputs[2] = (byte) color[2];

        byte[] bytes = text.getBytes(StandardCharsets.US_ASCII);

        for (int i = 0; i < 32; i++)
            if (bytes.length > i)
                inputs[3 + i] = bytes[i];
            else
                inputs[3 + i] = 0x00;

        if (socketCommunicationBytes((byte) 'W', inputs, 1)[0] == 0)
            System.out.println("Error in setLCDDisplay command, check server status!");
    }

    public void setRGBMatrix(short[][] matrix) {
        byte[] inputs = new byte[8 * 8];

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                inputs[i * 8 + j] = (byte) matrix[i][j];
            }

        if (socketCommunicationBytes((byte) 'M', inputs, 1)[0] == 0)
            System.out.println("Error in setRGBMatrix command, check server status!");
    }

    public void servo(short channel, short value) {
        if (socketCommunicationBytes((byte) 'E', new byte[]{(byte) channel, (byte) value, (byte) (value >> 8)}, 1)[0] == 0)
            System.out.println("Error in servo command, check server status!");
    }

    public void laser(boolean on) {
        if (socketCommunicationBytes((byte) 'L', new byte[]{(byte) (on ? 1 : 0)}, 1)[0] == 0)
            System.out.println("Error in laser command, check server status!");
    }

    public boolean digitalNoise() {
        if (socketCommunicationBytes((byte) 'N', null, 1)[0] != 0)
            return true;
        return false;
    }

    public short analogNoise() {
        return socketCommunicationBytesinputShortsoutput((byte) '8', new byte[]{(byte) 0}, 1)[0];
    }

    public void buzz(short hz) {
        if (socketCommunicationBytes((byte) 'B', new byte[]{(byte) hz, (byte) (hz >> 8)}, 1)[0] == 0)
            System.out.println("Error in buzz command, check server status!");
    }

    public short temp() {
        return socketCommunicationShorts((byte) 'C', null, 1)[0];
    }
}
