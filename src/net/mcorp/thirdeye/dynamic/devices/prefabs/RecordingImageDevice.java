package net.mcorp.thirdeye.dynamic.devices.prefabs;

import java.io.File;

import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>RecordingImageDevice</h1>
 * <hr>
 * <p>
 * 	This class defines basic functions that will be used to interface with a recording device such as a camera.
 * 	This class extends {@linkplain ImageDevice}.
 * </p>
 * @author Andrew Kerr
 * @see ImageDevice
 * @implNote When extending this class please refer to the <b>"Extending this Class"</b> section of the {@linkplain Device} class documentation.
 */
public abstract class RecordingImageDevice extends ImageDevice{

	/**
	 * Creates a new {@linkplain RecordingImageDevice} object.
	 * @param configuration - {@linkplain JavaClass} - A JavaClass object representing this {@linkplain RecordingImageDevice} object.
	 * @see {@linkplain Device#Device(JavaClass)}
	 */
	protected RecordingImageDevice(JavaClass configuration) {
		super(configuration);
	}
	
	/**
	 * Returns the current recording status for this RecordingImageDevice.
	 * @return {@linkplain Boolean} - The current recording status.
	 */
	public abstract boolean isRecording();
	
	/**
	 * Sets the current recording status for this RecordingImageDevice.
	 * @param record - {@linkplain Boolean} - The new recording status.
	 */
	public abstract void setRecording(boolean record);
	
	/**
	 * The current location used for storing the recording's of this RecordingImageDevice.
	 */
	public abstract File recordingLocation();

}
