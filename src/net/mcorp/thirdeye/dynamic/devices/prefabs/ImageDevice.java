package net.mcorp.thirdeye.dynamic.devices.prefabs;

import java.awt.Image;

import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>ImageDevice</h1>
 * <hr>
 * <p>
 * 	This class is used to implement a ImageDevice such as a camera.
 * 	This class extends {@linkplain Device}.
 * </p>
 * @author Andrew Kerr
 * @see RecordingImageDevice
 * @see Device
 * @implNote When extending this class please refer to the <b>"Extending this Class"</b> section of the {@linkplain Device} class documentation.
 */
public abstract class ImageDevice extends Device {

	/**
	 * Creates a new {@linkplain ImageDevice} object.
	 * @param configuration - {@linkplain JavaClass} - A JavaClass object representing this {@linkplain ImageDevice} object.
	 * @see {@linkplain Device#Device(JavaClass)}
	 */
	protected ImageDevice(JavaClass configuration) {
		super(configuration);
	}
	
	/**
	 * Returns the most recent image from this {@linkplain ImageDevice}.
	 * @return {@linkplain Image} - An Image object.
	 */
	public abstract Image getCurrentImage();
	
	/**
	 * Returns an array of images from this {@linkplain ImageDevice}.
	 * @return {@linkplain Image}[] - An array of Image objects.
	 */
	public abstract Image[] getImagesArray();

}
