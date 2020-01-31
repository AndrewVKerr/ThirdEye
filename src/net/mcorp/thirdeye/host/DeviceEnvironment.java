package net.mcorp.thirdeye.host;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.mcorp.thirdeye.host.camera.Camera;

public class DeviceEnvironment {
	
	protected static DeviceEnvironment environment = new DeviceEnvironment();
	public static DeviceEnvironment getEnvironment() { return environment; };
	
	protected DeviceEnvironment() {};
	
	private GPIOMode gpioMode = GPIOMode.Virtual;
	public void gpioMode(GPIOMode mode) { this.gpioMode = mode; };
	public GPIOMode gpioMode() { return this.gpioMode; };
	
	private final HashMap<Class<? extends Device>,ArrayList<Device>> devices = new HashMap<Class<? extends Device>,ArrayList<Device>>();
	
	/**
	 * Adds the provided device to this environment.
	 * @param device - {@linkplain Device} - The device to add.
	 * @return {@linkplain Boolean} - True if either the device was added or already was in this environment otherwise False.
	 */
	public boolean addDevice(Device device) {
		//Retrieve Class from device, check if null or is invalid throw error, otherwise continue.
		Class<? extends Device> device_class = device.getClass(); 
		if(device_class == null)
			throw new NullPointerException("Unable to add device as the method getClass returned a null or invalid Class object.");
		
		//Retrieve arraylist of devices using device_class, if no arraylist exists make one.
		ArrayList<Device> deviceList = this.devices.get(device_class);
		if(deviceList == null) {
			deviceList = new ArrayList<Device>();
			devices.put(device_class, deviceList);
		}
		
		if(deviceList.contains(device))
			return true;
			
		//Append device to deviceList.
		return deviceList.add(device);
	}
	
	/**
	 * Removes the provided device to this environment.
	 * @param device - {@linkplain Device} - The device to remove.
	 * @return {@linkplain Boolean} - True if either the device was removed or already was removed from this environment otherwise False.
	 */
	public boolean removeDevice(Device device) {
		//Retrieve Class from device, check if null or is invalid throw error, otherwise continue.
		Class<? extends Device> device_class = device.getClass(); 
		if(device_class == null || !(device_class.isInstance(device_class)))
			throw new NullPointerException("Unable to add device as the method getClass returned a null or invalid Class object.");
		
		//Retrieve arraylist of devices using device_class, if no arraylist exists make one.
		ArrayList<Device> deviceList = this.devices.get(device_class);
		if(deviceList == null) {
			deviceList = new ArrayList<Device>();
			devices.put(device_class, deviceList);
		}
		
		if(deviceList.contains(device) == false)
			return true;
		
		//Remove device from deviceList.
		return deviceList.remove(device);
	}
	
	/**
	 * Searches the provided deviceList for the specified Device.
	 * @param <DT> - ? extends {@linkplain Device} - A Class that extends Device.
	 * @param deviceList - {@linkplain DT}[] - An array of devices.
	 * @param device - {@linkplain DT} - The device to check for.
	 * @return {@linkplain Boolean} - True if the device existed or if a similar device was found.
	 */
	public <DT extends Device> boolean containsDevice(DT[] deviceArray, DT device) {
		for(DT dt : deviceArray) {
			if(dt == device || dt.equals(device))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns an arraylist of {@linkplain DT}'s using the device_class as the parameter.
	 * @param <DT> - ? extends {@linkplain Device} - A Class that extends Device.
	 * @param device_class - {@linkplain Class Class&lt;DT&gt;} - A Class that contains the type parameter DT.
	 * @return @{@linkplain ArrayList}&lt;{@linkplain DT}&gt; - An arraylist of {@linkplain DT} objects.
	 */
	public <DT extends Device> ArrayList<DT> getDevices(Class<DT> device_class) {
		if(device_class == null)
			throw new NullPointerException("Unable to get devices as the provided class is null.");
		
		ArrayList<Device> deviceList = this.devices.get(device_class);
		ArrayList<DT> dt = new ArrayList<DT>();
		
		if(deviceList != null) {
			for(Device device : deviceList) {
				@SuppressWarnings("unchecked")
				DT dev = (DT) device;
				dt.add(dev);
			}
		}
		
		return dt;
	} 
	
	/**
	 * Searches the provided file (folder) for any device files("*.device.io").
	 * @param devicesFolder - {@linkplain File} - The file (folder) to search through.
	 */
	public void loadDevices(File devicesFolder) {
		
		if(devicesFolder == null || devicesFolder.exists() == false)
			throw new NullPointerException("The folder doesnt exist or is null.");
		
		for(File file : devicesFolder.listFiles()) {
			if(file.getName().endsWith(".device.io")) {
				try {
					this.loadDevice(file);
					System.out.println("> "+file+" loaded...");
				} catch (IOException e) {
					System.out.println("> "+file+" unable to load...\n\t> "+e.getLocalizedMessage());
				}
			}
		}
		
		this.addDevice(new Camera());
		
	}
	
	public void loadDevice(File deviceFile) throws IOException {
		
		if(deviceFile == null || deviceFile.exists() == false)
			throw new NullPointerException("The file doesnt exist or is null.");
		
		if(deviceFile.getName().endsWith(".device.io") == false)
			throw new IOException("File does not have proper extension. (\"*.device.io\") ");
		
		try {
			
			List<String> lines = Files.readAllLines(deviceFile.toPath());
			String type = lines.get(0);
			if(type.contains(":"))
				type = type.split(":")[1];
			if(type.equalsIgnoreCase("class")) {
				
			}else{
				throw new Exception("Type ["+type+"] not supported...");
			}
			
		}catch(Exception e) {
			throw new IOException(e.getLocalizedMessage(),e);
		}
		
	}
	
}
