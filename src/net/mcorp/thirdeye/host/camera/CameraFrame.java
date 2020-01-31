package net.mcorp.thirdeye.host.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.imageio.ImageIO;

public class CameraFrame {
	
	BufferedImage bimg;
	public synchronized BufferedImage bimg() { return this.bimg; };
	
	public synchronized int[] rgb() { return this.bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), null, 0, bimg.getWidth()); };
	
	String type;
	public synchronized String type() { return this.type; };
	
	int day;
	public synchronized int day() { return this.day; };
	
	Month month;
	public synchronized Month month() { return this.month; };
	
	String time;
	public synchronized String time() { return this.time; };
	
	public CameraFrame() {
		LocalTime time = LocalTime.now();
		String format = "";
		format += String.format("%02d", time.getHour())+":";
		format += String.format("%02d", time.getMinute())+":";
		format += String.format("%02d", time.getSecond());
		this.time = format;
		LocalDate date = LocalDate.now();
		month = date.getMonth();
	}
	
	public static void saveToVideo(String month, File file) throws IOException{
		File master = new File(Camera.recordingFolder.getAbsolutePath()+"/"+month+".mjpeg");
		FileOutputStream fos = new FileOutputStream(master,true);
		
		FileInputStream fis = new FileInputStream(file);
		int bite;
		while((bite = fis.read()) != -1) {
			fos.write(bite);
		}
		fis.close();
		
		fos.close();
	}
	
	public synchronized void save(File file, boolean addToVideo) throws IOException {
		if(file.exists())
			throw new IOException("File ["+file+"] already exists.");
		if(file.getParentFile().exists() == false)
			Files.createDirectories(file.getParentFile().toPath());
		ImageIO.write(bimg, "jpg", file);
		if(addToVideo) {
			saveToVideo(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),file);
			file.delete();
		}
	}
	
}
