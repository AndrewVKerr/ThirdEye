package net.mcorp.thirdeye.host.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CameraFeed {
	
	static File recordingFolder = new File("./recordings");
	public static File recordingFolder() { return recordingFolder; };
	
	int hour;
	public synchronized int hour() { return this.hour; };
	
	File folder;
	public synchronized File folder() { return this.folder; };
	
	Calendar cal = Calendar.getInstance();
	public final int month = cal.get(Calendar.MONTH);
	public final int day = cal.get(Calendar.DAY_OF_MONTH);
	
	Camera camera;
	public synchronized Camera camera() { return this.camera; };
	
	public CameraFeed(Camera camera) throws IOException {
		this.camera = camera;
		this.hour = Camera.time().getHour();
		this.folder = new File("./images/");
		if(this.folder.exists() == false)
			Files.createDirectory(this.folder.toPath());
	};
	
	public synchronized void addFrame(CameraFrame frame) {
		
		if(frames.size() > 20) {
			CameraFrame old_frame = frames.remove(0);
			boolean saved = new File(this.folder.getAbsolutePath()+"/"+old_frame.time()+".jpg").exists();
			int copy = 0;
			while(!saved) {
				saved = true;
				try {
					old_frame.save(new File(this.folder.getAbsolutePath()+"/"+old_frame.time()+(copy > 0 ? "("+copy+")" : "")+".jpg"));
				} catch (IOException e) { saved = false; copy++; }
			}
		}
		
		frames.add(frame);
		try {
			frame.save(new File(this.folder.getAbsolutePath()+"/"+frame.time()+".jpg"));
		} catch (IOException e) {}
		
	}
	
	public synchronized void getNextFrame() {
		String url = "/jpgmulreq/1/image.jpg?key="+System.currentTimeMillis()+"&lq=";
		int index = 0;
		running = true;
		while(running) {
			Socket socket = null;
			try {
				if(this.ipAddress == null)
					throw new NullPointerException("IP Address has not been assigned.");
				String host = this.ipAddress;
				int port = this.ipPort;
				if(port == -1)
					port = 80;
				
				socket = new Socket(host,port);
				long timeout = System.currentTimeMillis();
				while(socket.isConnected() == false) {
					if(System.currentTimeMillis()-timeout >= 10*1000) {
						throw new SocketTimeoutException("Socket host did not connect in time.");
					}
				}
				
				OutputStream out = socket.getOutputStream();
				out.write(("GET "+url+index+" Http/1.1\n\n").getBytes());
				out.flush();
				
				InputStream in = socket.getInputStream();
				while(in.available() <= 0) {
					if(System.currentTimeMillis()-timeout >= 10*1000) {
						throw new HttpTimeoutException("Socket host did not respond in time.");
					}
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String[] response_header = br.readLine().split(" ",3);
				
				if(response_header.length != 3)
					throw new IOException("Host sent invalid http response, line 0 did not yield 3 strings when split.");
				
				if(!response_header[0].equalsIgnoreCase("http/1.1"))
					throw new IOException("Host sent a invalid http response, line 0 yielded a non http protocol packet");
				
				if(!response_header[1].equalsIgnoreCase("200"))
					throw new IOException("Host sent a invalid http response, line 0 yielded a non 200 response packet");
				
				CameraFrame frame = new CameraFrame();
				synchronized(frame) {
					String line;
					while(!(line = br.readLine()).equalsIgnoreCase("")) {
						if(line.startsWith("Content-Length: ")) {
							String length = line.substring("Content-Length: ".length());
							int len = Integer.parseInt(length);
							frame.data = new int[len];
						}
						if(line.startsWith("Content-Type: ")) {
							frame.type = line.substring("Content-Type: ".length());
						}
						if(line.startsWith("Date:")) {
							frame.date = line.substring("Date: ".length());
						}
					}
					for(int i = 0; i < frame.data.length; i++) {
						frame.data[i] = in.read();
					}
					if(frame.data[0] != 0xff || frame.data[1] != 0xD8 || frame.data[2] != 0xff) {
						throw new IOException("This frame was corrupted, skip it...");
					}
				}
				
				if(this.current_feed == null)
					this.current_feed = new CameraFeed();
				
				if(this.current_feed.hour != LocalTime.now().getHour()) {
					this.current_feed.compress();
					this.current_feed = new CameraFeed();
				}
				
				this.current_feed.addFrame(frame);
				
			}catch(Exception e) {
				if(!e.getLocalizedMessage().contains("corrupted")) {
					e.printStackTrace();
					url = "/jpgmulreq/1/image.jpg?key="+System.currentTimeMillis()+"&lq=";
				}
			}finally {
				if(socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				index++;
				if(index > 3000)
					index = 10;
				
				try {
					Thread.sleep(70);
				}catch(Exception e) {}
			}
		}
	}
	
	public synchronized void compress() throws IOException{//TODO: 11:00 results in the next day?
		System.out.println("Compressing");
		if(folder == null || folder.exists() == false)
			throw new IOException("Folder does not exist!");
		
		if(folder.isDirectory() == false)
			throw new IOException("Folder is a file!?");
		
		File result = new File(recordingFolder.getAbsolutePath()+"/"+month+"-"+day+"-"+hour+".rec.zip");
		if(result.getParentFile().exists() == false)
			Files.createDirectories(result.getParentFile().toPath());
		
		try {
 
            FileOutputStream fos = new FileOutputStream(result);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setMethod(ZipOutputStream.DEFLATED);
 
            for (File aFile : this.folder.listFiles()) {
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
		
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith(".jpg")) {
				if(file.delete() == false) {
					try {
						Files.delete(file.toPath());
					}catch(Exception e) {};
				}
			}
		}
	}
	
}
