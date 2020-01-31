package net.mcorp.thirdeye.host.camera;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.mcorp.thirdeye.ThirdEye;
import net.mcorp.thirdeye.host.ThirdEyeHost;

/**
 * 
 * @author Andrew Kerr
 */
public class CameraSession {
	
	/**
	 * The camera associated with this session.
	 */
	public final Camera camera;
	
	/**
	 * Retrieves the current {@linkplain LocalTime} and puts it into a integer array of size 4, in the order of Hour, Minute, Second, Nano.
	 * @return {@linkplain Integer}[] - An array of integers representing the time at the call of this method.
	 */
	public int[] getCurrentLocalTime() {
		LocalTime time = LocalTime.now();
		return new int[] {time.getHour(), time.getMinute(), time.getSecond(), time.getNano()};
	}
	
	/**
	 * Turns the given time array into something more human friendly.
	 * @param time - {@linkplain Integer}[] - An array of integers representing the time. (See {@linkplain #getCurrentLocalTime()})
	 * @param addNano - {@linkplain Boolean} - True will set the format to "HH:MM:SS.NN", False will set the format to "HH:MM:SS".
	 * @return {@linkplain String} - A human readable string of the time array given.
	 */
	public String getHumanReadableTime(int[] time, boolean addNano) {
		return time[0]+":"+time[1]+":"+time[2]+(addNano ? "."+time[3] : "");
	}
	
	/**
	 * Creates a new camera session and attaches the provided camera to it.
	 * @param camera - {@linkplain Camera} - The camera that owns this session.
	 */
	public CameraSession(Camera camera) {
		this.camera = camera;
	}
	
	private CameraFrame lastValidFrame;
	public final CameraFrame lastValidFrame() { return lastValidFrame; };
	
	private LocalTime time;
	private LocalDate date;
	/**
	 * Might compresses the recordings for each hour into a zip file. If the end of the hour has not been reached then the compression
	 * will not take place.
	 * @throws IOException 
	 */
	public void compress() throws IOException {
		File tempFolder = Camera.temporaryFolder;
		File recordingFolder = Camera.recordingFolder;
		
		if(time == null)
			time = LocalTime.now();
		if(date == null)
			date = LocalDate.now();
		
		if(time.getHour() == LocalTime.now().getHour())
			return;
		
		File result = new File(recordingFolder.getAbsolutePath()+"/"+date.getDayOfMonth()+"-"+time.getHour()+".rec.zip");
		if(result.getParentFile().exists() == false)
			Files.createDirectories(result.getParentFile().toPath());
		
		try {
 
            FileOutputStream fos = new FileOutputStream(result);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setMethod(ZipOutputStream.DEFLATED);
 
            for (File aFile : tempFolder.listFiles()) {
                zos.putNextEntry(new ZipEntry(aFile.getName()));
 
                byte[] bytes = Files.readAllBytes(aFile.toPath());
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
 
            zos.close();
 
        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
		
		for(File file : tempFolder.listFiles()) {
			if(file.getName().endsWith(".jpg")) {
				if(file.delete() == false) {
					try {
						Files.delete(file.toPath());
					}catch(Exception e) {};
				}
			}
		}
		
		LocalDate temp_date = LocalDate.now();
		if(date.getDayOfMonth() != temp_date.getDayOfMonth()) {
			
			//Compress all hour files into day files.
			System.out.println(this.camera.id+", New Day");
			
			if(temp_date.getDayOfMonth() < date.getDayOfMonth()) { //If the new date is less than the old date then its a new month. Compress even further.
				System.out.println(this.camera.id+", New Month");
			}
			date = LocalDate.now();
		}
		
		if(time.getHour() != LocalTime.now().getHour()) {
			time = LocalTime.now();
		}
		
	}
	
	/**
	 * Attempts to retrieve the next frame of data.
	 * @param url - {@linkplain URL} - The url of the next frame.
	 * @return {@linkplain CameraFrame} - The next frame.
	 * @throws IOException Thrown if the next frame couldn't be decoded.
	 */
	public CameraFrame getNextFrame(URL url) throws IOException {
		try {
			BufferedImage bimg = ImageIO.read(url);
			CameraFrame frame = new CameraFrame();
			frame.bimg = bimg;
			frame.type = "jpg";
			lastValidFrame = frame;
			((ThirdEyeHost)ThirdEye.instance()).updatePanel(bimg.getWidth(),bimg.getHeight());
			return frame;
		} catch (MalformedURLException e) {
			throw new IOException("Failed to decode url!",e);
		}
	}
	
}
