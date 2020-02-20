package net.mcorp.thirdeye.dynamic.devices.prefabs;

import java.io.File;

import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

public abstract class CameraDevice extends ImageDevice{

	/**
	 * Creates a new {@linkplain CameraDevice} object.
	 * @param configuration - {@linkplain JavaClass} - A JavaClass object representing this {@linkplain CameraDevice} object.
	 * @see {@linkplain Device#Device(JavaClass)}
	 */
	protected CameraDevice(JavaClass configuration) {
		super(configuration);
	}
	
	public abstract boolean isRecording();
	
	public abstract void setRecording(boolean record);
	
	public abstract File recordingLocation();

}
