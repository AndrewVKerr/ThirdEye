package net.mcorp.thirdeye.dynamic.devices;

import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>Device</h1>
 * <hr>
 * <p>
 * 	The Device class defines the most basic functions of all devices. Each device will be responsible
 * 	for starting and stopping their own threads for execution. Any class that extends this class
 * 	will be automatically loaded into memory by the {@linkplain Devices} class. The class will only be
 * 	loaded in when the Devices class determines that the class is required by a device on the network.
 * </p>
 * <hr>
 * <p>
 * 	<b><u>Extending this Class</u></b><br>
 * 	When extending this class the constructor {@linkplain #Device(JavaClass)} should be extended without
 * 	adding any parameters, or not extended at all. The system is designed to create a new instance using
 * 	this constructor and will not function properly if the parameters do not stay the same. The device
 * 	is started using the {@linkplain #start()} method. This method will create any necessary resources that
 * 	is required for the device to function. The start method is responsible to start any continuous processes. When it
 * 	is time for the device to stop the method {@linkplain #stop()} will be executed. This method will
 * 	destroy any of the generated resources. The device may remain in memory or may be destroyed only after 
 *  the stop method has been called. This device can be restarted via a call to the {@linkplain #start()} method.
 * </p>
 * @author Andrew Kerr
 * @implNote When extending this class please refer to the <b>"Extending this Class"</b> section of the {@linkplain Device} class documentation.
 */
public abstract class Device {
	
	/**
	 * {@linkplain JavaClass} - The configuration for this {@linkplain Device}.
	 */
	public final JavaClass configuration;
	
	/**
	 * Creates a new {@linkplain Device}.
	 * @param configuration - {@linkplain JavaClass} - A JavaClass object representing this {@linkplain Devices} object.
	 */
	protected Device(JavaClass configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Starts the Device.
	 * @implNote This method must either add {@linkplain Callback}'s or spawn new {@linkplain Thread}(s).
	 */
	public abstract void start();
	
	/**
	 * Stops the Device.
	 * @implNote This method doesn't need to worry about destroying its {@linkplain Callback}'s but it does need to worry about stopping any {@linkplain Threads}.
	 */
	public abstract void stop();
	
	/**
	 * Returns the current state of the {@linkplain Device}.
	 * @return {@linkplain Boolean} - True if the device is running, otherwise False.
	 */
	public abstract boolean isRunning();
	
	/**
	 * This method is used to return any issues this device may or may not be experiencing.
	 * @implNote This method is used to display the {@linkplain Exception}(s) that may be causing the device to malfunction.
	 * @return {@linkplain Exception}[] - An array of exceptions.
	 */
	public abstract Exception[] exceptions();
	
	/**
	 * Clears a specific Exception from this Device.
	 * @param e - {@linkplain Exception} - The exception to clear.
	 */
	public abstract void clearException(Exception e);
	
	/**
	 * This method is used to return the name of the device.
	 * @return {@linkplain String} - The name of the device.
	 */
	public abstract String deviceName();
	
}
