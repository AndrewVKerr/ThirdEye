package net.mcorp.thirdeye.devices;

import net.mcorp.thirdeye.manifest.JavaClass;

public abstract class BooleanDevice extends Device{

	/**
	 * Creates a new {@linkplain BooleanDevice} object.
	 * @param configuration - {@linkplain JavaClass} - A JavaClass object representing this {@linkplain BooleanDevice} object.
	 * @see {@linkplain Device#Device(JavaClass)}
	 */
	protected BooleanDevice(JavaClass configuration) {
		super(configuration);
	}
	
	/**
	 * Prompts the device for its current triggered status.
	 * @return {@linkplain Boolean} - True if the device is triggered, otherwise False.
	 */
	public abstract boolean currentTriggerStatus();

}
