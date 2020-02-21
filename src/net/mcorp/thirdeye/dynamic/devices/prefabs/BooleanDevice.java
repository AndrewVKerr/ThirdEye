package net.mcorp.thirdeye.dynamic.devices.prefabs;

import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>BooleanDevice</h1>
 * <hr>
 * <p>
 * 	This class defines a device that can only give a boolean status, such as a button or a switch. 
 * 	To retrieve the current boolean status of the device use the {@linkplain #currentTriggerStatus()} method.
 *  This class extends the {@linkplain Device} class.
 * </p>
 * @author Andrew Kerr
 * @see Device
 * @implNote When extending this class please refer to the <b>"Extending this Class"</b> section of the {@linkplain Device} class documentation.
 */
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
