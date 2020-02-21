package net.mcorp.thirdeye;

import java.io.File;

import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.systems.DataServer;
import net.mcorp.thirdeye.systems.TESecurityManager;
import net.mcorp.thirdeye.systems.ThreadManager;
import net.mcorp.thirdeye.systems.callbacks.Callback;
import net.mcorp.thirdeye.systems.callbacks.Callbacks;

public class ThirdEye {
	
	private static File directory;
	public static final File directory() { return directory; };
	
	public static void main(String[] args) throws Exception {
		
		File temp_directory = new File("./");
		if(temp_directory.getAbsolutePath().contains(".jar")) {
			while(temp_directory.getAbsolutePath().contains(".jar"))
				temp_directory = temp_directory.getParentFile();
		}
		directory = temp_directory;
		
		//Step 1: Setup Security Manager
		System.setSecurityManager(TESecurityManager.instance);
		
		//Step 1.5: Setup Threader
		ThreadManager.instance().toString();
		
		//Step 2: Setup Debugger
		Debugger.instance.out.println("Debugger Ready...");
		
		//Step 3: Start Server Software
		//TODO: Add Networker
		DataServer.instance().start();
		
		//Step 4: Create a Callback Manager
		Callbacks.instance.onShutdown.registerCallback(new Callback<Integer>() {

			@Override
			public void callback(Integer object) {
				System.out.println("Shutting down!");
				Debugger.instance.out.println("Program shutting down...");
			}
			
		});
		
		//Step 5: Read in Manifest TODO:
		Manifest.instance.read(new File(directory.getAbsolutePath()+"/manifest.xml"));
		
		//Step 6: Restart the System if Program Fails.
		//System.exit(0);
		
	}
	
}
