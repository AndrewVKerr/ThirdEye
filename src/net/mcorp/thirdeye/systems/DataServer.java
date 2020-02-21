package net.mcorp.thirdeye.systems;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.devices.prefabs.CameraDevice;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>DataServer</h1>
 * <hr>
 * <p>
 * 	Only one object of this class will exist in memory at any one time. This object can only be accessed via the
 *  {@link DataServer#instance()} method. This class will be responsible for accepting and handling data from clients
 *  to {@linkplain Device}'s and vice versa.
 * </p>
 * @author Andrew Kerr
 */
public final class DataServer implements Runnable{
	
	/**
	 * The internal variable storing the only instance of DataServer.
	 * @implNote This should never be interacted with directly unless it is by the method {@linkplain #instance()}.
	 */
	private static DataServer instance;
	
	/**
	 * Returns the only instance of {@linkplain DataServer}. If one does not exist then it will be generated then
	 * returned.
	 * @return {@linkplain DataServer} - The only instance of DataServer.
	 */
	public static final synchronized DataServer instance() {
		if(instance == null)
			instance = new DataServer();
		return instance;
	};
	
	/**
	 * 
	 */
	private DataServer() {
		
	}
	
	private ServerSocket socket;
	private Thread thread;
	
	public synchronized void start() throws Exception{
		
		if(socket != null || thread != null)
			throw new IOException("Server is already running!");
		
		socket = new ServerSocket(2000);
		
		thread = ThreadManager.instance().createThread(this);
		thread.setName("Network Thread");
		thread.setDaemon(true);
		thread.start();
		
	}
	
	public synchronized void stop() throws IOException {
		if(thread == null || thread.isInterrupted())
			throw new IOException("Server is already stopped or is stopping!");
		socket.close();
		thread.interrupt();
		thread = null;
	}

	@Override
	public void run() {
		
		while(socket != null && !socket.isClosed()) {
			try {
				Socket sock = socket.accept();
				InputStream in = sock.getInputStream();
				String header = "";
				char c;
				while((c = (char) in.read()) != '\n') {
					header += c;
				}
				
				String[] temp = header.split(" ");
				if(temp.length < 3) {
					sock.getOutputStream().write("Http/1.1 400 Client Error\n\n".getBytes());
					sock.close();
					continue;
				}
					
				String protocol = temp[0];
				String[] url = temp[1].split(Pattern.quote("?"));
				String version = temp[2];
				
				OutputStream out = sock.getOutputStream();
				boolean done = false;
				
				for(JavaClass javaclass : Manifest.instance.classes()) {
					Device device = javaclass.instance();
					if(device != null) {
						if(url[0].equals("/"+device.deviceName())) {
							if(device instanceof CameraDevice) {
								CameraDevice cam_device = ((CameraDevice) device);
								Image img = cam_device.getCurrentImage();
								if(img != null) {
									out.write("Http/1.1 200 OK\n\n".getBytes());
									ImageIO.write((BufferedImage) img, "jpg", out);
								}else {
									out.write("Http/1.1 503 No Images\n\n".getBytes());
									out.write("No Image Available!".getBytes());
								}
								done = true;
							}
						}
					}
				}
				
				if(!done) {
					if(url[0].equals("/")) {
						String html = "";
						html += "<html>\n";
						html += "\t<head>\n";
						html += "\t\t<title>ThirdEye</title>\n";
						html += "\t</head>\n";
						html += "\t<body>\n";
						html += "\t\t<img id=\"Test\"/>\n";
						html += "\t\t<script defer>\n";
						html += "\t\t\tfunction d(){\n";
						html += "\t\t\t\tvar e = document.getElementById(\"Test\")\n";
						html += "\t\t\t\te.src = \"/JideTechCamera?t=\"+new Date().getTime()\n";
						html += "\t\t\t}\n";
						html += "\t\t\tsetInterval(d,1000)\n";
						html += "\t\t</script>\n";
						html += "\t</body>\n";
						html += "</html>";
						out.write("Http/1.1 200 OK\n\n".getBytes());
						out.write(html.getBytes());
					}else {
						out.write("Http/1.1 404 Not Found\n\n".getBytes());
					}
				}
				
				out.flush();
				out.close();
				
				Thread.sleep(1);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) { return; }
		}
		
	}

	public boolean isThread(Thread currentThread) {
		if(currentThread == this.thread)
			return true;
		return false;
	};
	
}
