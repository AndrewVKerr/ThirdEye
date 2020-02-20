package net.mcorp.thirdeye.systems;

import java.util.HashMap;

import net.mcorp.thirdeye.dynamic.devices.Device;

public final class ThreadManager {
	
	private static ThreadManager instance = new ThreadManager();
	public static final ThreadManager instance() { /*if(instance == null) throw new NullPointerException("No global static instance exists for Threader.");*/ return instance; };
	
	public final ThreadGroup systemThreadGroup;
	public final ThreadGroup devicesThreadGroup;
	public final ThreadGroup mainThreadGroup;
	
	private HashMap<Device,ThreadGroup> registeredThreadGroups = new HashMap<Device,ThreadGroup>();
	
	private ThreadManager() {
		
		mainThreadGroup = Thread.currentThread().getThreadGroup();
		
		systemThreadGroup = mainThreadGroup.getParent();
		
		devicesThreadGroup = new ThreadGroup(systemThreadGroup,"Device ThreadGroups");
		devicesThreadGroup.setDaemon(false);
		
	}
	
	/**
	 * Creates a new {@linkplain ThreadGroup} using the supplied {@linkplain Device} object.
	 * @param device - {@linkplain Device} - The device object.
	 * @return A new {@linkplain ThreadGroup} object.
	 * @throws SecurityException Thrown if the given {@linkplain Device} object is not authorized to create {@linkplain Thread}'s or {@linkplain ThreadGroup}'s.
	 */
	public ThreadGroup createThreadGroup(Device device) throws SecurityException{
		if(device == null)
			throw new NullPointerException("The device parameter requires a non-null Device object.");
		
		if(device.deviceName() == null || device.deviceName().length() <= 0)
			throw new NullPointerException("This method requires that the Device object given by the device parameter contains a non-null string returned from the deviceName() method. This string must also be of length 1 or more.");
		
		if(registeredThreadGroups.containsKey(device))
			return registeredThreadGroups.get(device);
		
		ThreadGroup tg = new ThreadGroup(devicesThreadGroup,device.deviceName());
		tg.setDaemon(false);
		registeredThreadGroups.put(device, tg);
		return tg;
	}

	/**
	 * Checks if the thread given was created using this object.
	 * @param thread - {@linkplain Thread} - A Thread object.
	 * @return {@linkplain Boolean} - True if the thread exists, otherwise False.
	 */
	public boolean isThreadRegistered(Thread thread) {
		ThreadGroup tg = thread.getThreadGroup();
		if(tg == this.systemThreadGroup || tg == this.mainThreadGroup)
			return true;
		
		while(tg != null) {
			if(this.registeredThreadGroups.containsValue(tg))
				return true;
			tg = tg.getParent();
		}
		return false;
	}

	/**
	 * Creates a {@linkplain Thread} object using the given {@linkplain Device} to authorize the creation.
	 * @param device - {@linkplain Device} - The device object used to authorize the device.
	 * @param runnable - {@linkplain Runnable} - An object implementing the Runnable interface.
	 * @return {@linkplain Thread} - A new thread object set to the given {@linkplain Runnable} object.
	 * @throws SecurityException Thrown if the given {@linkplain Device} is not authorized to create {@linkplain Thread}'s or {@linkplain ThreadGroup}'s.
	 */
	public Thread createThread(Device device, Runnable runnable) throws SecurityException{
		if(device == null)
			throw new NullPointerException("The device parameter requires a non-null Device object.");
		
		if(runnable == null)
			throw new NullPointerException("The runnable parameter requires a non-null Runnable object.");
		
		ThreadGroup tg = this.registeredThreadGroups.get(device);
		if(tg == null)
			tg = this.createThreadGroup(device);
		
		Thread t = new Thread(tg,runnable);
		t.setName(tg.getName()+"- Thread #"+(tg.activeCount()+1));
		return t;
	}

	/**
	 * Creates a new Thread object.
	 * @param runnable - {@linkplain Runnable} - An object implementing the Runnable interface.
	 * @return {@linkplain Thread} - A new thread object set to the given {@linkplain Runnable} object.
	 * @throws SecurityException Thrown by {@linkplain Thread#checkAccess()}.
	 */
	public Thread createThread(Runnable runnable) throws SecurityException {
		if(runnable == null)
			throw new NullPointerException("The runnable parameter requires a non-null Runnable object.");
		
		Thread t = new Thread(runnable);
		t.checkAccess();
		return t;
	}
	
	/**
	 * Retrieves the device associated with target_thread_group.
	 * @param target_thread_group - {@linkplain ThreadGroup} - A ThreadGroup with a possible Device associated with it.
	 * @return {@linkplain Device} - The Device object associated with target_thread_group.
	 * @throws SecurityException Thrown if the current Thread is not allowed to retrieve the Device.
	 */
	public Device getDevice(ThreadGroup target_thread_group) {
		
		Thread current_thread = Thread.currentThread();
		ThreadGroup current_thread_group = current_thread.getThreadGroup();
		if(current_thread_group == target_thread_group || target_thread_group.parentOf(current_thread_group)
				|| current_thread_group == this.mainThreadGroup || current_thread_group == this.systemThreadGroup) {
			
			for(Device device : this.registeredThreadGroups.keySet()) {
				ThreadGroup thread_group = this.registeredThreadGroups.get(device);
				if(thread_group == target_thread_group) {
					return device;
				}
			}
			
			return null;
		}
		throw new SecurityException("The operating thread does not have any permissions to retrieve the device associated with the target_thread_group.");
	}
	
}
