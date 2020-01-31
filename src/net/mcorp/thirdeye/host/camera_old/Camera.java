package net.mcorp.thirdeye.host.camera;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.http.HttpTimeoutException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.host.Device;
import net.mcorp.thirdeye.host.GPIOMode;
import net.mcorp.thirdeye.host.DeviceEnvironment;

public class Camera extends Device{

	public static File streamNotFound;
	public static File streamDisabled;
	
	public static LocalTime time() { return LocalTime.now(); };
	public static LocalDate date() { return LocalDate.now(); };
	
	public String name = "UNKNOWN";
	public String desc = "";
	
	public String ipAddress = "127.0.0.1";
	public int ipPort = 80;
	
	public String url;
	
	public String recordPlan = "NONE";
	public int frameDelay = 0;
	public int maxTime = 0;
	
	public Rectangle resolution = null;
	public CameraFeed current_feed = null;
	
	public Thread thread;
	public boolean running = false;
	
	public Camera(List<String> lines) {
		for(String line : lines) {
			String lower = line.toLowerCase();
			if(line.contains(":")) {
				if(lower.startsWith("name")) {
					System.out.println("Setting name: "+line);
					this.name = line.split(":")[1];
				}else
				if(lower.startsWith("ip")) {
					System.out.println("Setting ip: "+line);
					String[] split = line.split(":");
					this.ipAddress = split[1];
					if(split.length >= 3)
						this.ipPort = Integer.parseInt(split[2]);
				}
			}
		}
	}

	public CameraThread camThread;
	
	@Override
	public void run() {
		System.out.println("Running");
		camThread = new CameraThread(this);
		camThread.run();
		/*
		GPIOMode gpioMode = DeviceEnvironment.getEnvironment().gpioMode();
		if(gpioMode == GPIOMode.Virtual) {
			System.out.println("Camera cannot run in virutal mode, exiting thread. Awaiting reboot in physical mode.");
		}else
		if(gpioMode == GPIOMode.Physical) {//"http://" + ip + "/jpgmulreq/1/image.jpg?key=" + time + "&lq=" + frame (10-3000)
			
		}else {
			System.out.println("Camera disabled, exiting thread now.");
		}*/
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
