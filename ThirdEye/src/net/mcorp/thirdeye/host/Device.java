package net.mcorp.thirdeye.host;


public abstract class Device implements Runnable{
	
	public abstract void setup();
	public abstract void shutdown();
	
	protected boolean running = false;
	public boolean running() { return this.running; };
	
	public void start() {
		this.setup();
		this.running = true;
	}
	
	public void stop() {
		this.running = false;
		this.shutdown();
	}
	
}
