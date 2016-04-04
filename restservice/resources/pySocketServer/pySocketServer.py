import smbus
import socket
import thread
import threading
import time
import atexit
import array
import struct
from sendfile import sendfile
import io
import os
import sys
import glob

import picamera
import pyaudio
import wave  
from gi.repository import GObject
from gi.repository import Gst

GObject.threads_init()
Gst.init(None)

from Adafruit_MotorHAT import Adafruit_MotorHAT, Adafruit_DCMotor
from Adafruit_PWM_Servo_Driver import PWM
import Adafruit_CharLCD 
from Adafruit_LED_Backpack import BicolorMatrix8x8
from Adafruit_ADS1x15 import ADS1x15
from Adafruit_TCS34725 import TCS34725
from Adafruit_MPU6050 import MPU6050
from Adafruit_HMC5883L import HMC5883L
from Adafruit_Trinket_Ping import Trinket_Ping

# Pseudo Support for 1:n Server:Client Connection
# Note the Global Interpreter Lock. Spawning processes is a bit slower than spawning threads. Plus, this runs on a raspberry pi 2

HOST = ''
PORT = 1025
# PORT+1 copy image capture
# PORT+2 video stream unless specified otherwise
# PORT+3 copy sound capture
# PORT+4 audio stream unless specified otherwise
# PORT+5 receive WAV File
bus = smbus.SMBus(1)

import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT) # Laser
GPIO.setup(23, GPIO.OUT) # Buzzer
GPIO.setup(24, GPIO.IN) # Terature
GPIO.setup(25, GPIO.IN) # Digital Noise

# Analog Digital Converter (values can be overwritten in initialize)
# gain relates bits to mv 
# The sample rate can be used to lower the noise (low sps) or to lower the power consumption (high sps) by duty cycling
ADC1gain = [4096, 4096, 4096, 4096]
ADC2gain = [4096, 4096, 4096, 4096]
ADC3gain = [4096, 4096, 4096, 4096]
ADC4gain = [4096, 4096, 4096, 4096]
ADC1sps = [250, 250, 250, 250]
ADC2sps = [250, 250, 250, 250]
ADC3sps = [250, 250, 250, 250]
ADC4sps = [250, 250, 250, 250]

# RGB (valuees can be overwritten in initialize)
# __TCS34725_INTEGRATIONTIME_2_4MS  = 0xFF   #  2.4ms - 1 cycle
# __TCS34725_INTEGRATIONTIME_24MS   = 0xF6   # 24ms  - 10 cycles
# __TCS34725_INTEGRATIONTIME_50MS   = 0xEB   #  50ms  - 20 cycles
# __TCS34725_INTEGRATIONTIME_101MS  = 0xD5   #  101ms - 42 cycles
# __TCS34725_INTEGRATIONTIME_154MS  = 0xC0   #  154ms - 64 cycles
# __TCS34725_INTEGRATIONTIME_700MS  = 0x00   #  700ms - 256 cycles
# __TCS34725_GAIN_1X                  = 0x00   #  No gain
# __TCS34725_GAIN_4X                  = 0x01   #  2x gain
# __TCS34725_GAIN_16X                 = 0x02   #  16x gain
# __TCS34725_GAIN_60X                 = 0x03   #  60x gain
RGB1gain = 0x00
RGB2gain = 0x00
RGB1integrationtime = 0xC0
RGB2integrationtime = 0xC0

os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')
base_dir = '/sys/bus/w1/devices/'
device_folder = glob.glob(base_dir + '28*')[0]
device_file = device_folder + '/w1_slave'

def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
        
    equals_pos = lines[1].find('t=')
    
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string)
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c, temp_f

class StoppableThreadStreamVideo(threading.Thread):
    def __init__(self, name='StoppableThread'):
        """ constructor, setting initial variables """
        self._stopevent = threading.Event(  )
        self._pauseevent = threading.Event(  )
        self._pauseperiod = 1.0
        self._streamsleepperiod = 0.1
        self.sink = ""

        self._pauseevent.set(  )
        self._stopevent.clear(  )

        threading.Thread.__init__(self, name=name)

    def setsink(self, s):
        self.sink = s

    def run(self):
        while (not self._stopevent.isSet(  )):
            if (self._pauseevent.isSet(  )):
                self._stopevent.wait(timeout=self._pauseperiod)
            else:
                with picamera.PiCamera() as camera:
                    camera.resolution = (640, 480)
                    camera.framerate = 24
                    camera.hflip = True
                    camera.vflip = True

                    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                    s2.connect((self.sink.split(":")[0], int(self.sink.split(":")[1])))
                    
                    # Accept a single connection and make a file-like object out of it
                    c2 = s2.makefile('wb')
                    camera.start_recording(c2, format='h264')
                    # Open in VLC with tcp/h264://HOST:PORT+2/
                    while (not self._pauseevent.isSet(  )):
                        try:
                            camera.wait_recording(1)
                            self._pauseevent.wait(self._streamsleepperiod)
                        except:
                            self._pauseevent.set(  )

                    try:
                        camera.stop_recording()
                        c2.close()
                        s2.close()
                    except:
                        pass

    def pause(self):
        self._pauseevent.set(  )

    def stream(self):
        self._pauseevent.clear(  )

    def join(self, timeout=None):
        """ Stop the thread. """
        self._pauseevent.set(  )
        self._stopevent.set(  )
        threading.Thread.join(self, timeout)

class StoppableThreadStreamAudio(threading.Thread):
    def __init__(self, name='StoppableThread'):
        """ constructor, setting initial variables """
        self._stopevent = threading.Event(  )
        self._pauseevent = threading.Event(  )
        self._pauseperiod = 1.0
        self._streamsleepperiod = 1.0
        self.sink = ""

        self._pauseevent.set(  )
        self._stopevent.clear(  )

        threading.Thread.__init__(self, name=name)

    def setsink(self, s):
        self.sink = s
        
    def run(self):
        pipeline = Gst.Pipeline("recordstream")

        audiosrc = Gst.ElementFactory.make("alsasrc", "mic")
        audiosrc.set_property("device", "hw:1")

        audioconvert = Gst.ElementFactory.make("audioconvert", "audioconvert")

        codec = Gst.ElementFactory.make("rtpL16pay", "codec")
        codec.set_property("pt", 10)

        caps = Gst.ElementFactory.make("capsfilter", "codeccaps")
        capsfilter = Gst.Caps.from_string("application/x-rtp,pt=10,encoding-name=L16,payload=10,clock-rate=44100,channels=2")
        caps.set_property("caps", capsfilter)

        audiosink = Gst.ElementFactory.make("udpsink", "audiosink")  

        pipeline.add(audiosrc, audioconvert, codec, caps, audiosink)

        audiosrc.link(audioconvert)
        audioconvert.link(codec)
        codec.link(caps)
        caps.link(audiosink)
                    
        while (not self._stopevent.isSet(  )):
            if (self._pauseevent.isSet(  )):
                self._stopevent.wait(timeout=self._pauseperiod)
            else:
                audiosink.set_property("host", self.sink.split(":")[0])
                audiosink.set_property("port", int(self.sink.split(":")[1]))

                pipeline.set_state(Gst.State.READY)
                pipeline.set_state(Gst.State.PLAYING)
                
                # Open in VLC with rtp://@:PORT+4/ (possibly requires vpn tunnel)
                # *.sdp
                # v=0
                # o=pi IN IP4 localhost
                # c=IN IP4 localhost
                # s=STREAM
                # m=audio 5555 RTP/AVP 10
                # a=rtpmap:10 S16LE/44100/2

                while (not self._pauseevent.isSet(  )):
                    self._pauseevent.wait(self._streamsleepperiod)
                
                pipeline.set_state(Gst.State.READY)
                pipeline.set_state(Gst.State.NULL)
        
    def pause(self):
        self._pauseevent.set(  )

    def stream(self):
        self._pauseevent.clear(  )

    def join(self, timeout=None):
        """ Stop the thread. """
        self._pauseevent.set(  )
        self._stopevent.set(  ) 
        threading.Thread.join(self, timeout)

class StoppableThreadSoundCapture(threading.Thread):

    def __init__(self, name='StoppableThread'):
        """ constructor, setting initial variables """
        self._stopevent = threading.Event(  )
        self._pauseevent = threading.Event(  )
        self._pauseperiod = 1.0
        self._recsleepperiod = 0.1

        self._pauseevent.set(  )
        self._stopevent.clear(  )

        threading.Thread.__init__(self, name=name)

    def run(self):
        FORMAT = pyaudio.paInt16
        CHANNELS = 1
        RATE = 48000
        CHUNK = 8192
        
        a = pyaudio.PyAudio()
        
        while (not self._stopevent.isSet(  )):
            if (self._pauseevent.isSet(  )):
                self._stopevent.wait(timeout=self._pauseperiod)
            else:
                wf = wave.open("sounds/sound.wav", 'wb')
                wf.setnchannels(CHANNELS)
                wf.setsampwidth(a.get_sample_size(FORMAT))
                wf.setframerate(RATE)

                frames = []
                # start Recording
                stream = a.open(format=FORMAT, channels=CHANNELS,
                                rate=RATE, input=True,
                                frames_per_buffer=CHUNK)

                while (not self._pauseevent.isSet(  )):
                    try:
                        data = stream.read(CHUNK)
                        frames.append(data)
                        self._pauseevent.wait(self._recsleepperiod)
                    except:
                        pass

                # stop Recording
                stream.stop_stream()
                stream.close()
                wf.writeframes(b''.join(frames))
                wf.close()
        a.terminate()

    def pause(self):
        self._pauseevent.set(  )

    def rec(self):
        self._pauseevent.clear(  )

    def join(self, timeout=None):
        """ Stop the thread. """
        self._pauseevent.set(  )
        self._stopevent.set(  ) 
        threading.Thread.join(self, timeout)
def ThreadSoundCapture(durationSeconds, *args):
        soundCapture.rec()
        time.sleep(durationSeconds)
        soundCapture.pause()
        thread.exit()
        
def ThreadTransferAudioCaptureResult():
        s4 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s4.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        try:
            s4.bind((HOST, PORT+3))
        except socket.error as msg:
            print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
            sys.exit()
        s4.listen(5)
        conn4, addr4 = s4.accept()
        
        file = open("sounds/sound.wav", "rb")
        blocksize = os.path.getsize("sounds/sound.wav")
        
        offset = 0
        while True:
            sent = sendfile(conn4.fileno(), file.fileno(), offset, blocksize)
            if sent == 0:
                break  # EOF
            offset += sent

        file.close()
        conn4.close()
        s4.close()
        thread.exit()
	
def ThreadCameraCapture():
        camera = picamera.PiCamera()
        camera.hflip = True
        camera.vflip = True
        camera.capture('images/image.jpg')
        camera.close()
        thread.exit()
        
def ThreadTransferCameraCaptureResult():
        s1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s1.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        try:
            s1.bind((HOST, PORT+1))
        except socket.error as msg:
            print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
            sys.exit()
        s1.listen(5)
        conn1, addr1 = s1.accept()
            
        file = open("images/image.jpg", "rb")
        blocksize = os.path.getsize("images/image.jpg")

        offset = 0
        while True:
            sent = sendfile(conn1.fileno(), file.fileno(), offset, blocksize)
            if sent == 0:
                break  # EOF
            offset += sent

        file.close()    
        conn1.close()
        s1.close()
        thread.exit()

def ThreadReceiveWAV():
    s5 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s5.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        s5.bind((HOST, PORT+5))
    except socket.error as msg:
            print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
            sys.exit()
    s5.listen(5)
    conn5, addr5 = s5.accept()

    fname = open('./sounds/received.wav', 'wb')
    buffer = conn5.recv(1024) 
    while buffer:
        fname.write(buffer)
        buffer = conn5.recv(1024)
       
    fname.close()
    s5.close()
    thread.exit()

def ThreadPlayReceivedWAV():
    def _connect_decoder(element, pad):
        pad.link(alsasink.get_static_pad("sink"))
    
    pipeline = Gst.Pipeline("playfile")

    filesrc = Gst.ElementFactory.make("filesrc", "filesrc")
    filesrc.set_property("location", "./sounds/received.wav")

    decodebin= Gst.ElementFactory.make("decodebin", "decodebin")

    alsasink = Gst.ElementFactory.make("alsasink", "alsasink")
    alsasink.set_property("device", "hw:0,0")

    if not filesrc or not decodebin or not alsasink:
        print("Not all elements could be created.")

    pipeline.add(filesrc, decodebin, alsasink)

    filesrc.link(decodebin)
    decodebin.connect('pad-added', _connect_decoder)

    if pipeline.set_state(Gst.State.READY) == Gst.StateChangeReturn.FAILURE:
            print("Unable to set the pipeline to the ready state.")
            
    if pipeline.set_state(Gst.State.PLAYING) == Gst.StateChangeReturn.FAILURE:
            print("Unable to set the pipeline to the playing state.")

    pbus = pipeline.get_bus()
    msg = pbus.timed_pop_filtered(
        Gst.CLOCK_TIME_NONE, Gst.MessageType.ERROR | Gst.MessageType.EOS)

    pipeline.set_state(Gst.State.READY)
    pipeline.set_state(Gst.State.NULL)
    thread.exit()

def ThreadRGBSensor(): 
    tcs.setInterrupt(False)
    rgb = tcs.getRawData()
    
    conn.sendall(struct.pack("<h", rgb['c']))
    conn.sendall(struct.pack("<h", rgb['r']))
    conn.sendall(struct.pack("<h", rgb['g']))
    conn.sendall(struct.pack("<h", rgb['b']))
    conn.sendall(struct.pack("<H", max(tcs.calculateColorTemperature(rgb), 65535))) # Kelvin
    conn.sendall(struct.pack("<H", max(tcs.calculateLux(rgb), 65535))) # Lux
    tcs.setInterrupt(True)
    thread.exit()

# recommended for auto-disabling motors on shutdown! Registers in Initiate
def turnOffMotors():
	mh.getMotor(1).run(Adafruit_MotorHAT.RELEASE)
	mh.getMotor(2).run(Adafruit_MotorHAT.RELEASE)
	mh.getMotor(3).run(Adafruit_MotorHAT.RELEASE)
	mh.getMotor(4).run(Adafruit_MotorHAT.RELEASE)

videoStream = StoppableThreadStreamVideo()
videoStream.start()
audioStream = StoppableThreadStreamAudio()
audioStream.start()
soundCapture = StoppableThreadSoundCapture()
soundCapture.start()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')
 
#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print('Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1])
    sys.exit()
print('Socket bind complete')
 
#now keep talking with the client
q = 1
initialized = False
while q:
    #Start listening on socket
    s.listen(5)

    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    #print('Connected with ' + addr[0] + ':' + str(addr[1]))
    #print("Connection from: " + conn.getpeername()[0] + ":" + str(conn.getpeername()[1]))
    
    msg = conn.recv(1)
    #print (msg)
    if (msg == b'1'): # ACD1
        conn.sendall(struct.pack("<hhhh", adc1.readADCSingleEnded(0, ADC1gain[0], ADC1sps[0]), adc1.readADCSingleEnded(1, ADC1gain[1], ADC1sps[1]), adc1.readADCSingleEnded(2, ADC1gain[2], ADC1sps[2]), adc1.readADCSingleEnded(3, ADC1gain[3], ADC1sps[3])))
        
    elif (msg == b'2'): # ACD2
        conn.sendall(struct.pack("<hhhh", adc2.readADCSingleEnded(0, ADC2gain[0], ADC2sps[0]), adc2.readADCSingleEnded(1, ADC2gain[1], ADC2sps[1]), adc2.readADCSingleEnded(2, ADC2gain[2], ADC2sps[2]), adc2.readADCSingleEnded(3, ADC2gain[3], ADC2sps[3])))
        
    elif (msg == b'3'): # ACD3
        conn.sendall(struct.pack("<hhhh", adc3.readADCSingleEnded(0, ADC3gain[0], ADC3sps[0]), adc3.readADCSingleEnded(1, ADC3gain[1], ADC3sps[1]), adc3.readADCSingleEnded(2, ADC3gain[2], ADC3sps[2]), adc3.readADCSingleEnded(3, ADC3gain[3], ADC3sps[3])))

    elif (msg == b'4'): # ACD4
        conn.sendall(struct.pack("<hhhh", adc4.readADCSingleEnded(0, ADC4gain[0], ADC4sps[0]), adc4.readADCSingleEnded(1, ADC4gain[1], ADC4sps[1]), adc4.readADCSingleEnded(2, ADC4gain[2], ADC4sps[2]), adc4.readADCSingleEnded(3, ADC4gain[3], ADC4sps[3])))
        
    elif (msg == b'5'): # ACD1 single channel
        number = ord(struct.unpack("c", conn.recv(1))[0])
        conn.sendall(struct.pack("<h", adc1.readADCSingleEnded(number, ADC1gain[number], ADC1sps[number])))
        
    elif (msg == b'6'): # ACD2 single channel
        number = ord(struct.unpack("c", conn.recv(1))[0])
        conn.sendall(struct.pack("<h", adc2.readADCSingleEnded(number, ADC2gain[number], ADC2sps[number])))
        
    elif (msg == b'7'): # ACD3 single channel
        number = ord(struct.unpack("c", conn.recv(1))[0])
        conn.sendall(struct.pack("<h", adc3.readADCSingleEnded(number, ADC3gain[number], ADC3sps[number])))

    elif (msg == b'8'): # ACD4 single channel
        number = ord(struct.unpack("c", conn.recv(1))[0])
        conn.sendall(struct.pack("<h", adc4.readADCSingleEnded(number, ADC4gain[number], ADC4sps[number])))

    elif (msg == b'T'): # Trinket get Ping Distance
        conn.sendall(struct.pack("<h", ping.getDistance()))
        
    elif (msg == b'R'): # RGB Sensor
        thread.start_new_thread(ThreadRGBSensor, ())

    elif (msg == b'G'): # Gyro+ACC 
        ret = acc.get_data()
        xrot = ret[0]*1000
        yrot = ret[1]*1000
        xgyr = ret[2]*1000
        ygyr = ret[3]*1000
        zgyr = ret[4]*1000
        xacc = ret[5]*1000
        yacc = ret[6]*1000
        zacc = ret[7]*1000
        conn.sendall(struct.pack("<hhhhhhhh", xrot, yrot, xgyr, ygyr, zgyr, xacc, yacc, zacc))

    elif (msg == b'H'): # Heading Magnetic North
        ret = mag.getHeading()
        conn.sendall(struct.pack("<hh", ret[0], ret[1]))

    elif (msg == b'X'): # MUX RGB Sensor (expects 1 byte)
        bus.write_byte(0x71, struct.unpack("B",conn.recv(1))[0])
        time.sleep(0.1)

        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))
        
    elif (msg == b'D'): # Drive (expects 8x 1 byte)
        # Receive Data
        i=0
        while i<=7:
                inputDrive[i] = ord(struct.unpack("c", conn.recv(1))[0])
                i=i+1

        # Send Commands to the Motor Board
        # 0 - 255
        myMotor1.setSpeed(inputDrive[0])
        myMotor2.setSpeed(inputDrive[2])
        myMotor3.setSpeed(inputDrive[4])
        myMotor4.setSpeed(inputDrive[6])
        # FORWARD = 1
        # BACKWARD = 2
        # BRAKE = 3
        # RELEASE = 4
        myMotor1.run(inputDrive[1])
        myMotor2.run(inputDrive[3])
        myMotor3.run(inputDrive[5])
        myMotor4.run(inputDrive[7])

        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif (msg == b'E'): # Servos (expects 1x byte 1x short )
        channel = ord(struct.unpack("c", conn.recv(1))[0])
        pulsestart = 0
        msg = conn.recv(1)
        pulseend = struct.unpack("<h", msg + conn.recv(1))[0]

        # Send Command to the Servo Board
        sh.setPWM(channel, pulsestart, pulseend)

        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'I' ): # Capture Image
        thread.start_new_thread(ThreadCameraCapture, ())
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'V' ): # Start Video Stream
        ip = conn.recv(1)
        if (ord(struct.unpack("c", ip)[0]) == 0):
            videoStream.setsink(conn.getpeername()[0] + ":" + str(PORT+2))
        else:
            address = str(ord(struct.unpack("c", ip)[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            msg = conn.recv(1)
            address = address + ":" + str(struct.unpack("<h", msg + conn.recv(1))[0])
            videoStream.setsink(address)
            
        videoStream.stream()
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'v' ): # Stop Video Stream
        ip = conn.recv(1)
        if (ord(struct.unpack("c", ip)[0]) == 0):
            pass
        else:
            address = str(ord(struct.unpack("c", ip)[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            msg = conn.recv(1)
            address = address + ":" + str(struct.unpack("<h", msg + conn.recv(1))[0])
            
        videoStream.pause()
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'S' ): # Capture Sound
        msg = conn.recv(1)
        durationSeconds = struct.unpack("<h", msg + conn.recv(1))[0]
        thread.start_new_thread(ThreadSoundCapture, (durationSeconds,1))
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'F' ): # Copy Sound
        mode = conn.recv(1)

        if (mode == b'I'):            
            thread.start_new_thread(ThreadTransferCameraCaptureResult, ())   
            conn.sendall('\x01'.encode('ascii', 'ignore'))
        elif ( mode == b'A' ):
            thread.start_new_thread(ThreadTransferAudioCaptureResult, ())   
            conn.sendall('\x01'.encode('ascii', 'ignore'))
        else:
            conn.sendall('\x00'.encode('ascii', 'ignore'))

    elif ( msg == b'f' ): # Receive WAV
        mode = conn.recv(1)
        if (mode == b'A'):
            
            thread.start_new_thread(ThreadReceiveWAV, ())   
            conn.sendall('\x01'.encode('ascii', 'ignore'))
        else:
            conn.sendall('\x00'.encode('ascii', 'ignore'))

    elif ( msg == b'P' ): # Play Received WAV
        thread.start_new_thread(ThreadPlayReceivedWAV, ())   
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'A' ): # Start Audio Stream
        ip = conn.recv(1)
        if (ord(struct.unpack("c", ip)[0]) == 0):
            audioStream.setsink(conn.getpeername()[0] + ":" + str(PORT+4))
        else:
            address = str(ord(struct.unpack("c", ip)[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            msg = conn.recv(1)
            address = address + ":" + str(struct.unpack("<h", msg + conn.recv(1))[0])
            audioStream.setsink(address)
            
        audioStream.stream()
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'a' ): # Stop Audio Stream
        ip = conn.recv(1)
        if (ord(struct.unpack("c", ip)[0]) == 0):
            pass
        else:
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            address = address + "." + str(ord(struct.unpack("c", conn.recv(1))[0]))
            msg = conn.recv(1)
            address = address + ":" + str(struct.unpack("<h", msg + conn.recv(1))[0])
            
        audioStream.pause()
        conn.sendall('\x01'.encode('ascii', 'ignore'))
        
    elif ( msg == b'M' ): # RGB Matrix (expects 64x 1 byte, super wasteful but i dont feel like shifting bits, the display should not be that time critical)
        # Receive Data
        # OFF = 0
        # GREEN = 1
        # RED = 2
        # YELLOW = 3
        i=0
        while i < 8*8:
            inputRGBMatrix[i] = ord(struct.unpack("c", conn.recv(1))[0])
            i=i+1

        display.clear()
        for x in range(8):
            for y in range(8):
                display.set_pixel(x, y, inputRGBMatrix[x*8+y])
        display.write_display()
                
	# Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'W' ): # LCD Display (expects RGB 3x 1 byte, 32 x 1byte) can fail for unknown reasons (W for Write)
        red = ord(struct.unpack("c", conn.recv(1))[0])
        green = ord(struct.unpack("c", conn.recv(1))[0])
        blue = ord(struct.unpack("c", conn.recv(1))[0])

        i = 0
        outputstring=""
        while i < 32:
            outputstring += struct.unpack("c", conn.recv(1))[0]
            i=i+1

        try:
            lcd.clear()
            lcd.set_color(red, green, blue)
            lcd.message(outputstring)
        except:
            # Confirm
            conn.sendall('\x00'.encode('ascii', 'ignore'))
        else:
            # Confirm
            conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'L' ): # Laser
        msg = ord(struct.unpack("c", conn.recv(1))[0])
        if ( msg == 1 ): # on
            GPIO.output(18, True)
        elif ( msg == 0 ): # off
            GPIO.output(18, False)

        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'N' ): # Noise Sensor
        conn.sendall(struct.pack("<h", GPIO.input(25)))

    elif ( msg == b'B' ): # Buzzer receives 2 byte frequency in hz. Order: high byte low byte
        msg = conn.recv(1)
        msg = msg + conn.recv(1)
        number = struct.unpack("<H", msg)[0]
        if (number == 0):
            p = GPIO.PWM(23, 1)
            p.stop()
        else:
            p = GPIO.PWM(23, number)
            p.start(1)
        
        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))

    elif ( msg == b'C' ):
        conn.sendall(struct.pack("<h", read_temp()[0]))
        read_temp()[0]
                     
    elif ( msg == b'i' ): #Initiate
        print("Connection from: " + conn.getpeername()[0] + ":" + str(conn.getpeername()[1]))
        print("Initializing ...")

        if ( ord(struct.unpack("c", conn.recv(1))[0]) != 0 ):
            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC1gain[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC2gain[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC3gain[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC4gain[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC1sps[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC2sps[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC3sps[x] = number

            for x in range(0, 4):
                msg = conn.recv(1)
                msg = msg + conn.recv(1)
                number = struct.unpack("<h",msg)[0]
                ADC4sps[x] = number
                
        if ( ord(struct.unpack("c", conn.recv(1))[0]) != 0 ):
            msg = conn.recv(1)
            msg = msg + conn.recv(1)
            RGB1integrationtime = struct.unpack("<h",msg)[0]
            msg = conn.recv(1)
            msg = msg + conn.recv(1)
            RGB1gain = struct.unpack("<h",msg)[0]
            msg = conn.recv(1)
            msg = msg + conn.recv(1)
            RGB2integrationtime = struct.unpack("<h",msg)[0]
            msg = conn.recv(1)
            msg = msg + conn.recv(1)
            RGB2gain = struct.unpack("<h",msg)[0]

        if ( not initialized ):
            # Initialize the LCD 
            lcd = Adafruit_CharLCD.Adafruit_CharLCDPlate(address=0x20, busnum=1)
            lcd.clear()
            s_google = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s_google.connect(('8.8.8.8',80))
            while True:
                try:
                    lcd.message(s_google.getsockname()[0])
                except:
                     continue
                else:
                     break
            s_google.close()
            lcd.set_color(0.0, 1.0, 1.0)

            # Initiate the Motor Driver
            mh = Adafruit_MotorHAT(addr=0x60)
            atexit.register(turnOffMotors)
            myMotor1 = mh.getMotor(1)
            myMotor2 = mh.getMotor(2)
            myMotor3 = mh.getMotor(3)
            myMotor4 = mh.getMotor(4)
            inputDrive = array.array('h',(0 for _ in range(8)))

            # Initiate the Servo Driver
            sh = PWM(0x41)
            sh.setPWMFreq(50)

            # Initiate the ADCs
            adc1 = ADS1x15(address=0x48, ic=0x00)
            adc2 = ADS1x15(address=0x49, ic=0x00)
            adc3 = ADS1x15(address=0x4a, ic=0x00)
            adc4 = ADS1x15(address=0x4b, ic=0x00)

            # Initiate the RGB Matrix
            display = BicolorMatrix8x8.BicolorMatrix8x8(address=0x72, busnum=1)
            display.begin()
            display.clear()
            inputRGBMatrix = array.array('h',(0 for _ in range(8*8)))

            # Initiate the RGB Sensor
            bus.write_byte(0x71, 0x80)
            time.sleep(0.1)
            tcs = TCS34725(integrationTime=RGB1integrationtime, gain=RGB1gain)
            bus.write_byte(0x71, 0x40)
            time.sleep(0.1)
            tcs = TCS34725(integrationTime=RGB2integrationtime, gain=RGB2gain)

            acc = MPU6050()

            mag = HMC5883L()

            ping = Trinket_Ping()   

        # Confirm
        print("Initialization complete!")
        conn.sendall('\x01'.encode('ascii', 'ignore'))
        initialized = True
        
    elif ( msg == b'c' ): #Connect
        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))
        
    elif ( msg == b'q' ): #Quit
        # Confirm
        conn.sendall('\x01'.encode('ascii', 'ignore'))
        q = 0
        
    else:
        print(msg)                                            
        q = 0
        
s.close()
videoStream.join()
soundCapture.join()
audioStream.join()
GPIO.cleanup()
