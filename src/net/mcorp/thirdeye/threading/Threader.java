package net.mcorp.thirdeye.threading;

import java.util.ArrayList;
import java.util.HashMap;

import net.mcorp.thirdeye.devices.Device;

public final class Threader {
	
	public static final Threader instance = new Threader();
	
	private HashMap<Device,ArrayList<Thread>> deviceThreads = new HashMap<Device,ArrayList<Thread>>();
	
	private ArrayList<Thread> registeredThreads = new ArrayList<Thread>();
	
	private final Thread mainThread;
	
	private Threader() {
		registeredThreads.add(Thread.currentThread());
		mainThread = Thread.currentThread();
	}
	
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
	
	/**
	 * This method creates a new {@linkplain Thread} object using the runnable parameter. However this method will throw a {@linkplain SecurityException}
	 * if the method was called outside of the main thread. 
	 * @param runnable - {@linkplain Runnable} - A runnable object.
	 * @return {@linkplain Thread} - The thread being generated.
	 */
	public Thread createThread(Runnable runnable) {
		
		Thread current_thread = Thread.currentThread();
		if(current_thread != mainThread)
			throw new SecurityException("Current thread doesnt equal main thread, cannot generate new thread!");
		
		Thread thread = new Thread(runnable);
		this.registeredThreads.add(thread);
		return thread;
		
	}

	public boolean isThreadRegistered(Thread target_thread) {
		if(this.registeredThreads.contains(target_thread))
			return true;
		for(Device device : this.deviceThreads.keySet()) {
			for(Thread thread : this.deviceThreads.get(device)) {
				if(thread == target_thread)
					return true;
			}
		}
		return false;
	}
	
}
