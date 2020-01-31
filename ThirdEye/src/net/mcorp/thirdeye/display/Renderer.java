package net.mcorp.thirdeye.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.mcorp.thirdeye.host.camera.CameraFeed;

public class Renderer extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2557404348249996820L;

	public final ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	
	final byte[] seperator = new byte[] {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10};
	
	private int menu_x = 0;
	private int menu_width = 200;
	
	public Renderer() {
		super(null);
		menu_x = -menu_width;
		
		/*try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = fis.readAllBytes();
			fis.close();
			
			BufferedImage bimg = null;
			int width = 640;
			int height = 480;
			
			int x = 0;
			int y = 0;
			
			for(int i = 0; i < data.length; i+=3) {
				if(bimg == null)
					bimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
				
				if(x > width) {
					x = 0;
					y++;
				}
				
				if(y > height) {
					y = 0;
					this.images.add(bimg);
					bimg = null;
					continue;
				}
				byte r = (byte) Math.abs(data[i]);
				byte g = (byte) Math.abs(data[i+1]);
				byte b = (byte) Math.abs(data[i+2]);
				System.out.println(r+", "+g+", "+b);
				Color color = new Color((r < 0 ? 0 : r),(g < 0 ? 0 : g),(b < 0 ? 0 : b));
				bimg.setRGB(x, y, color.getRGB());
			}
			
			if(bimg != null)
				this.images.add(bimg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
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
						if(menu_x == 0)
							menu_x = -menu_width;
						else
							menu_x = 0;
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
		BufferedImage bimg = null;
		if(this.images.size() > 0)
			bimg = this.images.get(0);
		if(bimg == null) {
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.white);
			g.drawString("No image available", 5, 20);
		}else {
			g.drawImage(bimg, 0, 0, null);
		}
		
		g.setColor(Color.gray);
		g.fillRect(menu_x, 0, menu_width, this.getHeight());
		g.setColor(Color.black);
		g.drawLine(menu_x+menu_width, 0, menu_x+menu_width, this.getHeight());
		
		int h = g.getFontMetrics().getHeight();
		int y = this.getHeight()/2;
		g.drawRect(menu_x+2, y-h/2-5, menu_width-5, h);
		
		File[] files = CameraFeed.recordingFolder().listFiles();
		
		if(selected > files.length-1)
			selected = files.length-1;
		else
			if(selected < 0)
				selected = 0;
		
		int index = 0;
		for(File file : files) {
			if(file == null || file.getName().endsWith(".rec.zip") == false)
				continue;
			int y_ = (index-selected)*(h+5);
			g.drawString(file.getName(), menu_x+5, y+y_);
			index++;
		}
	}
	
}
