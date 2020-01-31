package net.mcorp.thirdeye.host;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements Runnable {
	
	private ServerSocket socket;
	
	boolean running = false;
	public boolean isRunning() { return this.running; };
	public void stop() { this.running = false; }
	
	public Server(int port) {
		try {
			socket = new ServerSocket(port);
			socket.setSoTimeout(1000);
		}catch(Exception e) {
			//Debugger.instance().println(e);
		}
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				Socket sock = socket.accept();
				
			}catch(Exception e) {
				if(e instanceof SocketException)
					continue;
				//Debugger.instance().println(e);
			}
		}
	}

}
