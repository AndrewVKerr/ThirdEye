package net.mcorp.thirdeye.host;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mcorp.thirdeye.ThirdEyeMode;
import net.mcorp.thirdeye.host.camera.Camera;
import net.mcorp.thirdeye.host.camera.CameraFrame;

public class ThirdEyeHost extends ThirdEyeMode {

	public boolean running = false;
	
	private ArrayList<Camera> cameras;
	
	private JFrame frame;
	private JPanel panel;
	private int cameraSelection = 0;
	
	public void updatePanel(int width, int height) {
		if(panel == null)
			return;
		if(panel.getWidth() != width || panel.getHeight() != height) {
			frame.setSize(width, height);
		}
		panel.repaint();
	}
	
	@Override
	public void run() {
		
		System.out.println("> Loading devices...");
		DeviceEnvironment.getEnvironment().loadDevices(new File("./devices"));
		
		System.out.println("> Starting device threads");
		System.out.println("\t> Cameras");
		cameras = DeviceEnvironment.getEnvironment().getDevices(Camera.class);
		for(Camera camera : cameras) {
			System.out.println("\t\t>"+camera+" starting...");
			camera.start();
		}
		
		if(GraphicsEnvironment.isHeadless()) {
			char c = ' ';
			while(c != '\n') {
				try {
					c = (char) System.in.read();
				} catch (IOException e) { e.printStackTrace(); break; }
			}
			System.exit(0);
		}else {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Close this window to exit ThirdEye!");
			frame.setResizable(false);
			
			KeyListener keyboardListener = new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_LEFT)
						cameraSelection--;
					if(e.getKeyCode() == KeyEvent.VK_RIGHT)
						cameraSelection++;
					panel.repaint();
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			};
			
			panel = new JPanel() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -5827645647741743910L;
				
				public void paintComponent(Graphics g) {
					if(cameras != null && cameras.size() > 0) {
						if(cameraSelection > cameras.size()-1)
							cameraSelection = 0;
						if(cameraSelection < 0)
							cameraSelection = cameras.size()-1;
						Camera camera = cameras.get(cameraSelection);
						if(camera != null) {
							CameraFrame frame = camera.session.lastValidFrame();
							if(frame != null && frame.bimg() != null) {
								g.drawImage(frame.bimg(),0,0,this.getWidth(),this.getHeight(),null);
							}else {
								g.setColor(Color.DARK_GRAY);
								g.fillRect(0, 0, this.getWidth(), this.getHeight());
								g.setColor(Color.black);
								g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
								g.drawString("Camera frame not available?", 5, 20);
							}
						}else {
							g.setColor(Color.DARK_GRAY);
							g.fillRect(0, 0, this.getWidth(), this.getHeight());
							g.setColor(Color.black);
							g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
							g.drawString("Camera is null?!", 5, 20);
						}
					}else {
						g.setColor(Color.DARK_GRAY);
						g.fillRect(0, 0, this.getWidth(), this.getHeight());
						g.setColor(Color.black);
						g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
						g.drawString("No Available Camera's", 5, 20);
					}
				}
				
			};
			panel.addKeyListener(keyboardListener);
			
			frame.add(panel);
			frame.setSize(1280,720);
			frame.addKeyListener(keyboardListener);
			frame.setVisible(true);
		}
		
	}

}
