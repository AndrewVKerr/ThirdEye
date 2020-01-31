package net.mcorp.thirdeye.host.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.host.camera.Camera;

public class Server implements Runnable {
	
	private ServerSocket socket;
	
	public final ExecutorService pool = Executors.newCachedThreadPool();
	
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
	
	public Camera camera;
	
	@Override
	public void run() {
		running = true;
		while(running) {
			try {
				final Socket sock = socket.accept();
				System.out.println(sock);
				pool.execute(new Runnable() {

					@Override
					public void run() {
						try {
							OutputStream out = sock.getOutputStream();
							if(camera == null) {
								out.write("Http/1.1 503 Service Unavailable\n\n".getBytes());
								out.write("<h1>503 Service Unavailable</h1>\n<hr>\n<p>There is no camera to display.</p>".getBytes());
							}else
							if(camera.session.lastValidFrame() == null) {
								out.write("Http/1.1 404 Not Found\n\n".getBytes());
								out.write("<h1>404 Not Found</h1>\n<hr>\n<p>Could not send the next valid frame as it doesnt exist.</p>".getBytes());
							}else {
								out.write("Http/1.1 200 OK\nContent-Type:images/jpg\n\n".getBytes());
								ImageIO.write(camera.session.lastValidFrame().bimg(),"jpg",out);
							}
							out.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							sock.close();
						}catch(Exception e) {e.printStackTrace();}
					}
					
				});
			}catch(Exception e) {
				if(e instanceof SocketTimeoutException)
					continue;
				e.printStackTrace();
				//Debugger.instance().println(e);
			}
		}
	}

}
