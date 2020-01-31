package net.mcorp.thirdeye.host.camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.host.DeviceEnvironment;
import net.mcorp.thirdeye.host.GPIOMode;

public class CameraThread implements Runnable{

	public final Camera camera;
	
	public CameraThread(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public void run() {
		
		GPIOMode mode = DeviceEnvironment.getEnvironment().gpioMode();
		if(mode != GPIOMode.Physical) {
			BufferedImage bimg = null;
			while(true) {
				System.out.println("TICK");
				if(bimg == null) {
					try {
						bimg = ImageIO.read(Camera.streamDisabled);
					} catch (Exception e) {
						bimg = new BufferedImage(720,480,BufferedImage.TYPE_INT_RGB);
						Graphics g = bimg.getGraphics();
						g.setColor(Color.DARK_GRAY);
						g.fillRect(0, 0, 720, 480);
						g.setColor(Color.black);
						g.drawRect(0, 0, 719, 479);
						
						String text = "Cannot connect to camera stream, program was launched with GPIOMode set to "+mode+"!";
						int width = g.getFontMetrics().stringWidth(text)+5;
						int height = g.getFontMetrics().getHeight();
						g.setColor(Color.gray);
						g.fillRect(5, 20-height+2, width+5, height+5);
						g.setColor(Color.black);
						g.drawRect(5, 20-height+2, width+5, height+5);
						
						g.setColor(Color.white);
						g.drawString(text, 10, 20);
					}
				}
				
				try {
					LocalTime time = Camera.time();
					ImageIO.write(bimg, "jpg", new File("./images/"+time.getHour()+":"+time.getMinute()+":"+time.getSecond()));
				} catch (IOException e1) { System.err.println("Attempted to save the stream disabled screen."); }
				
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {}
			}
		}else {
			
		}
	}
	
}
