package net.mcorp.thirdeye.dynamic.devices.prefabs;

import java.awt.Image;

import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

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
