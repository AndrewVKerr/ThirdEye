package net.mcorp.thirdeye.devices;

import java.awt.Image;

import net.mcorp.thirdeye.manifest.JavaClass;

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
