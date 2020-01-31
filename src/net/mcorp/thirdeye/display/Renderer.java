package net.mcorp.thirdeye.display;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.mcorp.thirdeye.host.DeviceEnvironment;
import net.mcorp.thirdeye.host.camera.Camera;

public class Renderer extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2557404348249996820L;
	
	public Renderer() {
		super(null);
	}
	
	KeyListener keyboard;
	public KeyListener getListener() {
		if(keyboard == null) {
			keyboard = new KeyListener() {
	
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyChar() == 'm') {
						
						Renderer.this.repaint();
					}
				}
	
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			};
		}
		return this.keyboard;
	}
	
	MouseWheelListener mouse;
	public MouseWheelListener getMouseListener() {
		if(mouse == null) {
			mouse = new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
						int amount = e.getScrollAmount();
						if(e.getWheelRotation() < 0)
							amount = -amount;
						if(amount > 0)
							selected++;
						else
							selected--;
						repaint();
					}
				}
				
			};
		}
		return mouse;
	}
	
	int selected = 0;
	
	public void paintComponent(Graphics g) {
		ArrayList<Camera> cameras = DeviceEnvironment.getEnvironment().getDevices(Camera.class);
		if(selected > cameras.size()) 
			selected = 0;
		if(selected < 0)
			selected = cameras.size();
		Camera camera = cameras.get(selected);
		if(camera != null && camera.running()) {
			BufferedImage bimg = camera.session.lastValidFrame().bimg();
			
			int w = bimg.getWidth();
			if(w > this.getWidth())
				w = this.getWidth();
			
			int h = bimg.getHeight();
			if(h > this.getHeight())
				h = this.getHeight();
			
			g.drawImage(bimg,0,0,w,h,null);
		}
	}
	
}
