import smbus
import math

class HMC5883L:
        ModeRegister = 0x02
        ConfigurationRegisterB = 0x01
        AxisXDataRegisterMSB = 0x03
        AxisXDataRegisterLSB = 0x04
        AxisZDataRegisterMSB = 0x05
        AxisZDataRegisterLSB = 0x06
        AxisYDataRegisterMSB = 0x07
        AxisYDataRegisterLSB = 0x08

        MeasurementContinuous = 0x00
        MeasurementSingleShot = 0x01
        MeasurementIdle = 0x03

        def __init__(self):
                self.bus = smbus.SMBus(1) 
                self.address = 0x1e
                self.bus.write_byte_data(self.address, self.ModeRegister, self.MeasurementContinuous)
                self.setScale(1.3)
                self.setDeclination(4, 0)

        def setDeclination(self, degree, min = 0):
                self.declinationDeg = degree
                self.declinationMin = min
                self.declination = (degree+min/60) * (math.pi/180)

        def twosToInt(self, val, len):
                # Convert twos compliment to integer
                if(val & (1 << len - 1)):
			val = val - (1<<len)

		return val

        def setScale(self, gauss):
                if gauss == 0.88:
                        self.scale_reg = 0x00
                        self.scale = 0.73
                elif gauss == 1.3:
                        self.scale_reg = 0x01
                        self.scale = 0.92
                elif gauss == 1.9:
                        self.scale_reg = 0x02
                        self.scale = 1.22
                elif gauss == 2.5:
                        self.scale_reg = 0x03
                        self.scale = 1.52
                elif gauss == 4.0:
                        self.scale_reg = 0x04
                        self.scale = 2.27
                elif gauss == 4.7:
                        self.scale_reg = 0x05
                        self.scale = 2.56
                elif gauss == 5.6:
                        self.scale_reg = 0x06
                        self.scale = 3.03
                elif gauss == 8.1:
                        self.scale_reg = 0x07
                        self.scale = 4.35
                
                self.scale_reg = self.scale_reg << 5
                self.bus.write_byte_data(self.address, self.ConfigurationRegisterB, self.scale_reg)
	
	# Returns heading in degrees and minutes
        def getHeading(self):
                # print("test")
                # print((self.bus.read_byte_data(self.address, self.AxisXDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisXDataRegisterLSB))
                # print((self.bus.read_byte_data(self.address, self.AxisYDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisYDataRegisterLSB))
                # print((self.bus.read_byte_data(self.address, self.AxisZDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisZDataRegisterLSB))
                # print("end")
                scaled_x = round(self.twosToInt((self.bus.read_byte_data(self.address, self.AxisXDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisXDataRegisterLSB), 16) * self.scale, 4)
                scaled_y = round(self.twosToInt((self.bus.read_byte_data(self.address, self.AxisYDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisYDataRegisterLSB), 16) * self.scale, 4)
                scaled_z = round(self.twosToInt((self.bus.read_byte_data(self.address, self.AxisZDataRegisterMSB) << 8) | self.bus.read_byte_data(self.address, self.AxisZDataRegisterLSB), 16) * self.scale, 4)
                      
                headingRad = math.atan2(scaled_y, scaled_x)
                headingRad += self.declination
                
		# Correct for reversed heading
                if(headingRad < 0):
                        headingRad += 2*math.pi

		# Check for wrap and compensate
                if(headingRad > 2*math.pi):
                        headingRad -= 2*math.pi

		# Convert to degrees from radians
                headingDeg = headingRad * 180/math.pi
                degrees = math.floor(headingDeg)
                minutes = round(((headingDeg - degrees) * 60))
                return (degrees, minutes)
