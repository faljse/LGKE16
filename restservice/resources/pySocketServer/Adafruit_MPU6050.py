import smbus
import math

class MPU6050:
    def __init__(self):
	# Power management registers
    	self.power_mgmt_1 = 0x6b
    	self.power_mgmt_2 = 0x6c

        self.bus = smbus.SMBus(1) 
        self.address = 0x68

        # Wake the 6050 as it starts in sleep mode
        self.bus.write_byte_data(self.address, self.power_mgmt_1, 0)

    def read_byte(self, adr):
        return self.bus.read_byte_data(self.address, adr)

    def read_word(self, adr):
        high = self.bus.read_byte_data(self.address, adr)
        low = self.bus.read_byte_data(self.address, adr+1)
        val = (high << 8) + low
        return val

    def read_word_2c(self,adr):
        val = self.read_word(adr)
        if (val >= 0x8000):
            return -((65535 - val) + 1)
        else:
            return val

    def dist(self,a,b):
        return math.sqrt((a*a)+(b*b))

    def get_y_rotation(self,x,y,z):
        radians = math.atan2(x, self.dist(y,z))
        return -math.degrees(radians)

    def get_x_rotation(self,x,y,z):
        radians = math.atan2(y, self.dist(x,z))
        return math.degrees(radians)

    def get_data(self):
        gyro_xout = self.read_word_2c(0x43) / 131
        gyro_yout = self.read_word_2c(0x45) / 131
        gyro_zout = self.read_word_2c(0x47) / 131

        accel_xout = self.read_word_2c(0x3b) / 16384.0
        accel_yout = self.read_word_2c(0x3d) / 16384.0
        accel_zout = self.read_word_2c(0x3f) / 16384.0

        # Guess there is no z rotation with accelerometer
        return (self.get_x_rotation(accel_xout, accel_yout, accel_zout), self.get_y_rotation(accel_xout, accel_yout, accel_zout), gyro_xout, gyro_yout, gyro_zout, accel_xout, accel_yout, accel_zout)


