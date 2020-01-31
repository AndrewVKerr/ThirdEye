package net.mcorp.thirdeye.host.camera;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.mcorp.thirdeye.host.Device;
import net.mcorp.thirdeye.host.DeviceEntry;
import net.mcorp.thirdeye.host.DeviceEnvironment;
import net.mcorp.thirdeye.host.GPIOMode;
import net.mcorp.thirdeye.utils.networking.IPAddress;

public class Camera extends Device {

	public final UUID id = UUID.randomUUID();
	public IPAddress address = new IPAddress(192,168,1,18);
	
	public String url = "/jpgmulreq/1/image.jpg?key="+System.currentTimeMillis()+"&lq=";
	
	private int index = 10;
	public int index() { if(index > 3000) index = 10; return index++; };
	
	public static final File recordingFolder = new File("./recordings");
	public static final File temporaryFolder = new File("./temp/images");
	
	public final CameraSession session = new CameraSession(this);
	
	public CameraThread thread1;
	
	@DeviceEntry
	public Camera(File file) {
		super(file);
	}
	
	@Override
	public void run() { //TODO: Change it so that the program understands that it cannot save within the .jar file after being exported. File path issues!
		System.out.println(id+", Starting");
		//thread1 = new CameraThread(this);
		
		int i = 10;
		while(running) {
			//thread1.run();
			if(i > 3000)
				i = 10;
			else
				i++;
			try {
				CameraFrame frame = this.session.getNextFrame(new URL("http://192.168.1.18"+this.url+i));
				try {
					File dest = new File(Camera.temporaryFolder.getAbsolutePath()+"/"+frame.time()+".jpg");
					frame.save(dest,true);
				}catch(Exception e) {
					boolean print = true;
					if(e instanceof IOException) {
						if(e.getLocalizedMessage().contains("already exists")) {
							print = false;
						}
					}
					if(print)
						e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setup() {
		System.out.println("STARTING");
		GPIOMode mode = DeviceEnvironment.getEnvironment().gpioMode();
		if(mode == GPIOMode.Virtual) {
			System.out.println("Camera not enabled, later add an overview to show this.");
		}else
		if(mode == GPIOMode.Physical) {
			
			Thread myThread = new Thread(this);
			myThread.setName("Camera Thread - #"+id);
			myThread.setDaemon(true);
			myThread.start();
			
		}else {
			System.out.println("Camera not enabled, GPIOMode is set to a non valid choice. Add an overview to show this.");
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
