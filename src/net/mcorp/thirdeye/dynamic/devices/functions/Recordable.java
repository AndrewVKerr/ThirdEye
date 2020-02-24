package net.mcorp.thirdeye.dynamic.devices.functions;

import java.io.File;

/**
 * <h1>Recordable</h1>
 * <hr>
 * <p>
 * 	This interface is used to define a class that extends {@linkplain Device} as a recordable object.
 * 	It provides useful functions used to get/set the devices recording status as well as get the devices
 * 	recording location.
 * </p>
 * @author Andrew Kerr
 * @implNote This interface must be implemented whenever a Device is intended to record any data. This
 * interface lets the {@linkplain DataServer} know that this device is able to record data so that a
 * client can get/set the recording status.
 */
public interface Recordable {
	
	/**
	 * Returns the current recording status for this RecordingImageDevice.
	 * @return {@linkplain Boolean} - The current recording status.
	 */
	public boolean isRecording();
	
	/**
	 * Sets the current recording status for this RecordingImageDevice.
	 * @param record - {@linkplain Boolean} - The new recording status.
	 */
	public void setRecording(boolean b);
	
	/**
	 * The current location used for storing the recording's of this RecordingImageDevice.
	 */
	public File recordingLocation();
	
}
