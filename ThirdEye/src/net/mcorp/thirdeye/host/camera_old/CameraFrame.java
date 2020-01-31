package net.mcorp.thirdeye.host.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CameraFrame {
	
	int[] data;
	public synchronized int[] data() { return this.data; };
	
	String type;
	public synchronized String type() { return this.type; };
	
	String date;
	public synchronized String date() { return this.date; };
	
	public synchronized String time() {
		int start = "Fri Jan 17 ".length();
		int end = " 2020".length();
		return date.substring(start,date.length()-end);
	}
	
	public synchronized void save(File file) throws IOException {
		if(file.exists())
			throw new IOException("File ["+file+"] already exists.");
		FileOutputStream fos = new FileOutputStream(file);
		for(int i : this.data) {
			fos.write(i);
		}
		fos.close();
	}
	
}
