package net.mcorp.thirdeye.manifest;

import java.util.ArrayList;

import net.mcorp.thirdeye.devices.Device;

public final class JavaClass {
	
	private Device instance;
	public Device instance() { return this.instance; };
	
	private final ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>();
	public AccessRight[] getAccessRights() { return this.accessRights.toArray(new AccessRight[] {}); };
	
	JavaClass() {};
	
}
