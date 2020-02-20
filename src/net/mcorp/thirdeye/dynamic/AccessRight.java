package net.mcorp.thirdeye.dynamic;

/**
 * This is the set of rights that a device can have.
 * @author Andrew Kerr
 */
public enum AccessRight {
	
	/**
	 * Allows the Device to create/manage {@linkplain Thread}'s.
	 */
	Threading,
	
	/**
	 * Allows the Device to create/manage {@linkplain ServerSocket}'s.
	 */
	NetworkingHost,
	/**
	 * Allows the Device to create/manage {@linkplain Socket}'s.
	 */
	NetworkingConnect,
	/**
	 * Allows the Device to Multicast.
	 */
	NetworkingMulticast,
	
	/**
	 * Allows the Device to read files.
	 * @implNote Only files that the Device owns access to.
	 */
	FileRead,
	/**
	 * Allows the Device to write files. (Includes Creating and Deleting as well, as this writes data to the disk.)
	 * @apiNote Only files that the Device owns access to or to the "/tmp" folder.
	 */
	FileWrite,
	/**
	 * Allows the Device to read files it does not own.
	 * @implNote Only files that are associated with other Devices.</b>
	 */
	CriticalFileRead,
	/**
	 * Allows the Device to write files it does not own. (Includes Creating and Deleting as well, as this writes data to the disk.)
	 * @implNote Only files that are associated with other Devices.</b>
	 */
	CriticalFileWrite,
	/**
	 * Allows the linking of library functions.
	 */
	LibraryLink,
	/**
	 * Allows arbitrary unchecked code to execute within the console.
	 * @implNote <b>ADVANCED</b>In doing this you are allowing this device to execute arbitrary unchecked code within the console
	 * (most likely with either psudo or root permissions). In doing this you accepting any risks that could occurs with this 
	 * device. Make sure you trust this Device entirely. This should only be if there is no other way for the device to function.
	 */
	CMDExecution,
	
	
	;

	public static AccessRight parse(String name) {
		for(AccessRight right : values()) {
			if(right.name().equalsIgnoreCase(name)) {
				return right;
			}
		}
		return null;
	}
	
}
