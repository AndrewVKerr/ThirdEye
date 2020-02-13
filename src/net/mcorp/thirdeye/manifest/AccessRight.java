package net.mcorp.thirdeye.manifest;

/**
 * This is the set of rights that a device can have.
 * @author Andrew Kerr
 */
public enum AccessRight {
	
	Threading,
	
	NetworkingHost,
	NetworkingConnect,
	NetworkingMulticast,
	
	FileRead,
	FileWrite,
	CriticalFileRead,
	CriticalFileWrite,
	LibraryLink,
	CMDExecution,;

	public static AccessRight parse(String name) {
		for(AccessRight right : values()) {
			if(right.name().equalsIgnoreCase(name)) {
				return right;
			}
		}
		return null;
	}
	
}
