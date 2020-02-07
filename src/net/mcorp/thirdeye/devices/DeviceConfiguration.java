package net.mcorp.thirdeye.devices;

import java.io.File;
import java.util.ArrayList;

/**
 * <h1>DeviceConfiguration</h1>
 * <hr>
 * <p>
 * 	
 * </p>
 * @author Andrew Kerr
 */
public final class DeviceConfiguration {
	
	/**
	 * 
	 * @author Andrew Kerr
	 * @param <T>
	 */
	public static class ConfigValue<T>{
		
		public final String name;
		public final T value;
		
		private ConfigValue(String name, T value) {
			this.value = value;
			this.name = name;
		}
		
	}
	
	private ArrayList<ConfigValue<?>> config = new ArrayList<ConfigValue<?>>();
	
	public ConfigValue<?>[] config(){
		return this.config.toArray(new ConfigValue<?>[]{});
	}
	
	public DeviceConfiguration(String filepath) {
		
	}
	
	public DeviceConfiguration(File file) {
		
	}
	
}
