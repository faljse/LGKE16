import smbus
import math

class Trinket_Ping:
	def __init__(self):
                self.bus = smbus.SMBus(1) 
                self.address = 0x07
                
	def getDistance(self):
                return self.bus.read_byte(self.address)