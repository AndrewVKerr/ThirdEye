package net.mcorp.thirdeye.systems.network;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.devices.functions.Recordable;
import net.mcorp.thirdeye.dynamic.devices.prefabs.ImageDevice;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;
import net.mcorp.thirdeye.systems.ThreadManager;
import net.mcorp.thirdeye.systems.network.clients.http.HttpClient;

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
	
	private ExecutorService pool = Executors.newCachedThreadPool();
	
	public synchronized void start() throws Exception{
		
		if(socket != null || thread != null)
			throw new IOException("Server is already running!");
		
		try {
			socket = new ServerSocket(80);
		}catch(Exception e) {
			socket = new ServerSocket(2000);
		}
		
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
				HttpClient client = new HttpClient(sock);
				pool.execute(client);
				/*InputStream in = sock.getInputStream();
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
				
				if(url[0].startsWith("/css")) {
					if(url[0].endsWith("/darkmode")) {
						String css = "";
						css += "html, html * {\n";
						css += "\tcolor: #eeeeee !important;\n";
						css += "\tbackground-color: #292929 !important;\n";
						css += "}\n\n";
						css += "img, video {z-index: 1}\n";
						css += "* {border-color: #555555 !important}\n";
						css += "cite, cite * {color: #029833 !important}\n";
						css += ":link, :link * {color: #8db2e5 !important}\n";
						css += "input, textarea {background-color: #333333 !important}\n";
						css += "a {background-color: rgba(255, 255, 255, 0.01) !important}\n";
						css += ":visited, :visited * {color: rgb(211, 138, 138) !important}\n";
						css += "html, html::before, body, body::before, input, select, button {background-image: none !important}";
						css += "video {\n";
						css += "\tbackground-color: transparent !important;\\n";
						css += "}";
						out.write(this.httpResponse(200, "Ok", new String[] {"Content-Type:text/css"}, css.getBytes()));
					}else {
						out.write(this.httpResponse(404, "Not Found", null, new byte[0]));
					}
					done = true;
				}
				
				if(!done) {
					for(JavaClass javaclass : Manifest.instance().classes()) {
						Device device = javaclass.instance();
						if(device != null) {
							String device_url = "/"+device.deviceName()+"/"+javaclass.name();
							if(url[0].startsWith(device_url)) {
								
								if(url[0].startsWith(device_url+"/image")) {
									
									if(device instanceof ImageDevice) {
										ImageDevice img_device = ((ImageDevice) device);
										Image img = img_device.getCurrentImage();
										if(img != null) {
											out.write("Http/1.1 200 OK\n\n".getBytes());
											ImageIO.write((BufferedImage) img, "jpg", out);
										}else {
											out.write("Http/1.1 503 No Images\n\n".getBytes());
											out.write("No Image Available!".getBytes());
										}
									}else{
										out.write("Http/1.1 503 Device isnt Image\n\n".getBytes());
									}
									
								}else
								if(url[0].startsWith(device_url+"/recordable")){

									if(device instanceof Recordable){
										Recordable rec_device = (Recordable) device;
										if(url[0].endsWith("/set")) {
											if(url.length < 2) {
												out.write(this.httpResponse(400, "Failed to pass recording parameter", null, new byte[0]));
											}else {
												String[] params = url[1].split("&");
												boolean temp_done = false;
												for(String param : params) {
													if(param.startsWith("recording=")) {
														String[] temp_param = param.split("=");
														if(temp_param.length != 2) {
															out.write(this.httpResponse(400, "Invalid Query String", null, new byte[0]));
														}else {
															rec_device.setRecording(Boolean.valueOf(temp_param[1]));
															System.out.println(rec_device.isRecording());
															out.write(this.httpResponse(200, "OK", null, new byte[0]));
														}
														temp_done = true;
													}
												}
												if(!temp_done)
													out.write(this.httpResponse(404, "Query Not Found", null, new byte[0]));
											}
										}
									}else {
										out.write("Http/1.1 503 Device isnt Recordable\n\n".getBytes());
									}
								}else {
									
									String html = "";
									html += "<html>\n";
									html += "\t<head>\n";
									html += "\t\t<title>ThirdEye</title>\n";
									html += "\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/darkmode\">\n";
									html += "\t</head>\n";
									html += "\t<body>\n";
									
									if(device instanceof ImageDevice) {
										html += "\t\t<div id='ImageDevice' style='text-align:center'>\n";
										html += "\t\t\t<img id=\"Test\" style='display:inline'/>\n";
										html += "\t\t\t<script defer>\n";
										html += "\t\t\t\tfunction d(){\n";
										html += "\t\t\t\t\tvar e = document.getElementById(\"Test\")\n";
										html += "\t\t\t\t\te.src = \""+device_url+"/images?t=\"+new Date().getTime()\n";
										html += "\t\t\t\t}\n";
										html += "\t\t\t\tsetInterval(d,1000)\n";
										html += "\t\t\t</script>\n";
										html += "\t\t</div>\n";
									}
									
									//TODO: FIXME: Add Dynamic Tags
									if(device instanceof Recordable) {
										if(html.endsWith("<hr>\n") == false)
											html += "\t\t<hr>\n";
										html += "\t\t<div id='Recordable'>\n";
										html += "\t\t\t<p>Recording: <span id=\"isRecording\">Unknown?</span></p>\n";
										html += "\t\t\t<button id='setRecording' onclick='setRecording'>Toggle Recording</button>\n";
										html += "\t\t</div>\n";
									}
									
									html += "\t</body>\n";
									html += "</html>";
									out.write(this.httpResponse(200, "OK", null, html.getBytes()));
									
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
						html += "\t\t<title>ThirdEye Directory</title>\n";
						html += "\t</head>\n";
						html += "\t<body>\n";
						html += "\t\t<h1>ThirdEye Device Directory</h1>\n";
						html += "\t\t<hr>\n";
						for(JavaClass javaclass : Manifest.instance().classes()) {
							Device device = javaclass.instance();
							if(device != null) {
								html += "\t\t<a href='/"+device.deviceName()+"/"+javaclass.name()+"'>"+javaclass.name()+"</a>\n";
							}
						}
						html += "\t\t \n";
						html += "\t</body>\n";
						html += "</html>";
						out.write(this.httpResponse(200, "OK", null, html.getBytes()));
					}else {
						out.write("Http/1.1 404 Not Found\n\n".getBytes());
					}
				}
				
				out.flush();
				out.close();
				*/
				
				Thread.sleep(1);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) { return; }
		}
		
	}

	private byte[] httpResponse(int code, String text, String[] headers, byte[] payload) {
		
		String http = "Http/1.1 "+code+" "+text+"\n";
		if(headers != null) {
			for(String header : headers) {
				http += header+"\n";
			}
		}
		http += "\n";
		byte[] data = http.getBytes();
		byte[] result = new byte[data.length + payload.length];
		int i = 0;
		for(byte b : data) {
			result[i] = b;
			i++;
		}
		for(byte b : payload) {
			result[i] = b;
			i++;
		}
		return result;
	}
	
	public boolean isThread(Thread currentThread) {
		if(currentThread == this.thread)
			return true;
		return false;
	};
	
}
