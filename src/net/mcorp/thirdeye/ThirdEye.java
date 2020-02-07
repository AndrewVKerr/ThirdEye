package net.mcorp.thirdeye;

import java.io.File;

import net.mcorp.thirdeye.callbacks.Callback;
import net.mcorp.thirdeye.callbacks.Callbacks;
import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.manifest.Manifest;
import net.mcorp.thirdeye.security.ThirdEyeSecurityManager;

public class ThirdEye {
	
	public static void main(String[] args) {
		
		//Step 1: Setup Security Manager
		try {
			System.setSecurityManager(ThirdEyeSecurityManager.instance);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 2: Setup Debugger
		try {
			Debugger.instance.out.println("Debugger Ready...");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 3: Start Server Software
		try {
			//TODO: Add Server Software
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 4: Create a Callback Manager
		try {
			Callbacks.instance.onShutdown.registerCallback(new Callback<Integer>() {

				@Override
				public void callback(Integer object) {
					System.out.println("Shutting down!");
					Debugger.instance.out.println("Program shutting down...");
				}
				
			});
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 5: Read in Manifest TODO:
		try {
			Manifest.instance.read(new File("/home/andrew/ThirdEye/manifest.xml"));
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 6: Restart the System if Program Fails.
		//System.exit(0);
		
	}
	
}
