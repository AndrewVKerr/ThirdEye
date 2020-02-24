package classes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.mcorp.thirdeye.dynamic.devices.functions.Recordable;
import net.mcorp.thirdeye.dynamic.devices.functions.Webpaged;
import net.mcorp.thirdeye.dynamic.devices.prefabs.ImageDevice;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;
import net.mcorp.thirdeye.systems.ThreadManager;
import net.mcorp.thirdeye.systems.network.clients.Client;
import net.mcorp.thirdeye.systems.network.clients.http.HttpClient;

public class JideTechCamera extends ImageDevice implements Runnable, Recordable, Webpaged{

	public JideTechCamera(JavaClass configuration) {
		super(configuration);
	}

	private final ArrayList<Exception> exceptions = new ArrayList<Exception>();
	
	private boolean recording = true;
	
	@Override
	public boolean isRecording() {
		return recording;
	}

	@Override
	public void setRecording(boolean record) {
		recording = record;
	}

	private File recording_location = null;
	
	@Override
	public File recordingLocation() {
		return recording_location;
	}
	
	@Override
	public Image getCurrentImage() {
		return this.currentImage;
	}

	@Override
	public Image[] getImagesArray() {
		return null;
	}

	private Thread myThread;
	
	private Timer timer;
	private JFrame frame;
	private JPanel panel;
	
	protected Image currentImage;
	
	@Override
	public void start() {
		
		if(GraphicsEnvironment.isHeadless() == false) {
			frame = new JFrame();
			panel = new JPanel() {

				private static final long serialVersionUID = 1L;
				
				public void paintComponent(Graphics g) {
					if(currentImage != null) {
						g.drawImage(currentImage, 0, 0, null);
					}
				}
				
			};
			frame.add(panel);
			frame.setSize(720, 480);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setVisible(true);
			
			timer = new Timer(1,(ActionListener) new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					panel.repaint();
				}
				
			});
			timer.start();
		}
		
		System.out.println("Start");
		if(myThread == null) {
			myThread = ThreadManager.instance().createThread(this,this);
		}
		try{
			myThread.checkAccess();
		}catch(SecurityException se) {
			se.printStackTrace();
			exceptions.add(se);
			return;
		}
		myThread.setName("JideTechCamera");
		myThread.setDaemon(false);
		myThread.start();
		System.out.println("Started");
	}

	public final String url = "/jpgmulreq/1/image.jpg?key="+System.currentTimeMillis()+"&lq=";
	
	@Override
	public void run() {
		int i = 10;
		long time = System.currentTimeMillis();
		while(true) {
			if(i > 3000)
				i = 10;
			else
				i++;
			
			try {
				URL url = new URL("http://192.168.1.18"+this.url+i);
				
				BufferedImage bimg = ImageIO.read(url);
				
				Graphics g = bimg.getGraphics();
				int w = bimg.getWidth();
				int h = bimg.getHeight();
				if(this.isRecording()) {
					g.setColor(Color.red);
					g.drawRect(0, 0, w-1, h-1);
					g.drawRect(1, 1, w-3, h-3);
					String label = "Recording...";
					g.drawString(label, w-g.getFontMetrics().stringWidth(label)-5, h-10);
				}else{
					g.setColor(Color.yellow);
					g.drawRect(0, 0, w-1, h-1);
					g.drawRect(1, 1, w-3, h-3);
					String label = "Live";
					g.drawString(label, w-g.getFontMetrics().stringWidth(label)-5, h-10);
				}
				
				g.setColor(Color.cyan);
				LocalTime lt = LocalTime.now();
				String label = "System Time: "+String.format("%02d", lt.getHour())+":"+String.format("%02d", lt.getMinute())+":"+String.format("%02d", lt.getSecond());

				g.drawString(label, 5, h-10);
				
				currentImage = bimg;
				if(frame != null) {
					frame.setSize(bimg.getWidth()+6, bimg.getHeight()+28);
				}
				
				if(System.currentTimeMillis()-time < 500)
					continue;
				
				time = System.currentTimeMillis();
				
				if(this.isRecording()) {
					try {
						
						LocalDate date = LocalDate.now();
						Month month = date.getMonth();
						int day = date.getDayOfMonth();
						File video_file = new File(this.configuration.getWorkspace().getAbsolutePath()+"/"+month+"-"+day+".mjpeg");
						
						if(video_file.exists() == false) {
							Files.createDirectories(video_file.getParentFile().toPath());
						}
						
						FileOutputStream fos = new FileOutputStream(video_file,true);
						
						ImageIO.write(bimg, "jpg", fos);
						
						fos.close();
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(e instanceof SecurityException) {
					this.exceptions.add(e);
					return;
				}
			}
		}
	}
	
	@Override
	public void stop() {
		try {
			frame.dispose();
			if(myThread != null) {
				myThread.join();
			}
		}catch(Exception e) {
			e.printStackTrace();
			exceptions.add(e);
		}
	}

	@Override
	public boolean isRunning() {
		return (myThread == null ? false : myThread.isAlive());
	}

	@Override
	public Exception[] exceptions() {
		return exceptions.toArray(new Exception[] {});
	}

	@Override
	public String deviceName() {
		return "JideTechCamera";
	}

	@Override
	public void clearException(Exception e) {
		if(exceptions.contains(e))
			exceptions.remove(e);
		else
			throw new NullPointerException("Exception ["+e+"] does not exist within this device.");
	}

	@Override
	public void sendWebpageAndPerformActions(Client client, String url, HashMap<String, String> querys) throws Exception {
		OutputStream out = client.socket.getOutputStream();
		if(url.equalsIgnoreCase("/images")) {
			Image img = this.getCurrentImage();
			if(img != null) {
				out.write("Http/1.1 200 OK\nContent-Type:image/jpeg\n\n".getBytes());
				BufferedImage bimg = (BufferedImage) img;
				try {
					ImageIO.write(bimg, "jpg", client.socket.getOutputStream());
				}catch(Exception e) {
					if(!(e instanceof SocketException))
						throw e;
				}
			}else {
				out.write("Http/1.1 503 No Images\n\n".getBytes());
				out.write("No Image Available!".getBytes());
			}
		}else {
			String device_url = "/"+this.deviceName()+"/"+this.configuration.name();
			
			String html = "";
			html += "<html>\n";
			html += "\t<head>\n";
			html += "\t\t<title>ThirdEye</title>\n";
			html += "\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/darkmode.css\">\n";
			html += "\t</head>\n";
			html += "\t<body>\n";
			
			html += "\t\t<div id='ImageDevice' style='text-align:center'>\n";
			html += "\t\t\t<img id=\"Test\" style='display:inline'/>\n";
			html += "\t\t\t<script defer>\n";
			html += "\t\t\t\tfunction d(){\n";
			html += "\t\t\t\t\tvar timeout = document.getElementById('updateSpeed').value\n";
			html += "\t\t\t\t\tif(timeout>0){\n";
			html += "\t\t\t\t\t\tvar e = document.getElementById(\"Test\")\n";
			html += "\t\t\t\t\t\te.src = \""+device_url+"/images?t=\"+new Date().getTime()\n";
			html += "\t\t\t\t\t\tvar elm = document.getElementById('updateSpeed')\n";
			html += "\t\t\t\t\t}\n";
			html += "\t\t\t\t\tsetTimeout(d,timeout);\n";
			html += "\t\t\t\t}\n";
			html += "\t\t\t\tsetTimeout(d,1000)\n";
			html += "\t\t\t</script>\n";
			html += "\t\t</div>\n";
			
			html += "\t\t<hr>\n";
			html += "\t\t<div id='Actions'>\n";
			html += "\t\t\t<p>Update Interval:<input type=\"range\" min=\"0\" max=\"5000\" value=\"1000\" step=\"500\" id=\"updateSpeed\"></p>\n";
			html += "\t\t\t<button onclick=\"function toggle(){var xhttp = new XMLHttpRequest();xhttp.open('GET','"+device_url+"?record=toggle');xhttp.send();};toggle()\">Toggle Recording</button>\n";
			html += "\t\t</div>\n";
			
			html += "\t</body>\n";
			html += "</html>";
			
			out.write(HttpClient.response(200, "Okay", null, html.getBytes()));
		}
		
		if(querys.containsKey("record")) {
			String value = querys.get("record");
			if(value != null && value.equalsIgnoreCase("toggle"))
				this.setRecording(!this.isRecording());
			else
				this.setRecording(Boolean.valueOf(value));
		}
		
		/*if(url[0].startsWith(device_url+"/recordable")){

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
									return new byte[0];//out.write(this.httpResponse(200, "OK", null, new byte[0]));
								}
								temp_done = true;
							}
						}
						if(!temp_done)
							//out.write(this.httpResponse(404, "Query Not Found", null, new byte[0]));
							return new byte[0];
					}
				}
			}else {
				//return "Http/1.1 503 Device isnt Recordable\n\n".getBytes());
				return new byte[0];
			}
		}*/
		
		//return new byte[0];
	}

}
