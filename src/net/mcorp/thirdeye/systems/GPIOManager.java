package net.mcorp.thirdeye.systems;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;

public final class GPIOManager {

	private static final GPIOManager instance = new GPIOManager();
	
	public final GPIOManager instance() { return instance; };
	
	/**
	 * Enables the GPIO functions. Used to prevent accidental READ/WRITE of GPIO I/O in the development environment.
	 * @implNote Set to True in production. Unless the device used is not GPIO compatible.
	 */
	public boolean GPIO_ACTIVE = false;
	
	/**
	 * The controller for the GPIO.
	 */
	private GpioController gpio = null;
	
	/**
	 * Attempts to start the GPIO for this device. Will immediately bail if {@linkplain #GPIO_ACTIVE} is set to false.
	 * @param pins - {@linkplain Map}&lt;{@linkplain Pin},{@linkplain PinMode}&gt; - A map of pins to their mode. 
	 */
	public void startGPIO(Map<Pin,PinMode> pins) {
		if(GPIO_ACTIVE == false)
			return;
		if(gpio != null)
			throw new RuntimeException("GPIO is already Running!!!");
		gpio = GpioFactory.getInstance();
		if(gpio == null)
			return;
		for(Pin pin : pins.keySet()) {
			PinMode mode = pins.get(pin);
			gpio.provisionPin(pin, mode);
		}
	}
	
	public double readAnalog(Pin pin) {
		if(gpio == null)
			return 0;
		
		GpioPin gpio_pin = gpio.getProvisionedPin(pin);
		if(gpio_pin == null)
			return 0;
		
		if(gpio_pin.getMode() == PinMode.ANALOG_INPUT) {
			GpioPinAnalogInput gpio_pin_ai = (GpioPinAnalogInput) gpio_pin;
			return gpio_pin_ai.getValue();
		}
		
		throw new RuntimeException("The given pin is not in a input mode!");
		
	}
	
}
