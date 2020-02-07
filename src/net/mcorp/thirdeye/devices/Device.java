package net.mcorp.thirdeye.devices;

/**
 * <h1>Device</h1>
 * <hr>
 * <p>
 * 	The Device class defines the most basic functions of all devices. Each device will be responsible
 * 	for starting and stopping their own threads for execution. Later the starting and stopping of the
 * 	device will implement listeners which will be called instead. Any class that extends this class
 * 	will be automatically loaded into memory by the {@linkplain Devices} class. The class will only be
 * 	loaded in when the Devices class determines that the class is required by a device on the network.
 * </p>
 * @author Andrew Kerr
 */
public abstract class Device {
	
	public final DeviceConfiguration configuration;
	
	protected Device(DeviceConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * 
	 */
	public void start() {
		
	}
	
	public void stop() {
		
	}
	
	public abstract Device createInstance(DeviceConfiguration configuration);
	
}
