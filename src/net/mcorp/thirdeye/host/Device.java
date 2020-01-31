package net.mcorp.thirdeye.host;

import java.io.File;

public abstract class Device implements Runnable{
	
	public abstract void setup();
	public abstract void shutdown();
	
	protected boolean running = false;
	public boolean running() { return this.running; };
	
	protected final File device_file;
	
	public Device(File file) { device_file = file; }
	
	public void start() {
		this.setup();
		this.running = true;
	}
	
	public void stop() {
		this.running = false;
		this.shutdown();
	}
	
}
