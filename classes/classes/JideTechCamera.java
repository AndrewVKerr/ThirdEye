package classes;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.mcorp.thirdeye.dynamic.devices.prefabs.CameraDevice;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;
import net.mcorp.thirdeye.systems.ThreadManager;

public class JideTechCamera extends CameraDevice implements Runnable{

	public JideTechCamera(JavaClass configuration) {
		super(configuration);
	}

	private final ArrayList<Exception> exceptions = new ArrayList<Exception>();
	
	private boolean recording = false;
	
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
				
				currentImage = bimg;
				if(frame != null)
					frame.setSize(bimg.getWidth(), bimg.getHeight());
				
				if(System.currentTimeMillis()-time < 500)
					continue;
				
				time = System.currentTimeMillis();
				
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

}
