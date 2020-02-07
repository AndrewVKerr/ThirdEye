package net.mcorp.thirdeye.threading;

import java.util.ArrayList;
import java.util.HashMap;

import net.mcorp.thirdeye.devices.Device;

public final class Threader {
	
	public static final Threader instance = new Threader();
	
	private HashMap<Device,ArrayList<Thread>> deviceThreads = new HashMap<Device,ArrayList<Thread>>();
	
	public Thread createThread(Device device, Runnable runnable) {
		
		ArrayList<Thread> thread_list = deviceThreads.get(device);
		if(thread_list == null) {
			thread_list = new ArrayList<Thread>();
			deviceThreads.put(device, thread_list);
		}
		
		Thread thread = new Thread(runnable);
		thread_list.add(thread);
		return thread;
	}

	public boolean isThreadRegistered(Thread target_thread) {
		for(Device device : this.deviceThreads.keySet()) {
			for(Thread thread : this.deviceThreads.get(device)) {
				if(thread == target_thread)
					return true;
			}
		}
		return false;
	}
	
}
