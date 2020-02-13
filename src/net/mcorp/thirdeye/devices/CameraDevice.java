package net.mcorp.thirdeye.devices;

import java.io.File;

import net.mcorp.thirdeye.manifest.JavaClass;

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
