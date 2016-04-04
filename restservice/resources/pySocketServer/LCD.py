import Adafruit_CharLCD as LCD
import socket

lcd = LCD.Adafruit_CharLCDPlate(address=0x20, busnum=1)
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