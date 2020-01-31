package net.mcorp.thirdeye.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Display extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3994486708179988869L;

	public Display() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(720,480);
		this.setTitle("ThirdEye");
		this.setVisible(true);
		try {
			Renderer panel = new Renderer();
			this.add(panel);
			this.addKeyListener(panel.getListener());
			this.addMouseWheelListener(panel.getMouseListener());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Timer timer = new Timer(1000,new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		timer.start();
	}
	
}
