package net.mcorp.thirdeye;

import net.mcorp.thirdeye.callbacks.Callbacks;
import net.mcorp.thirdeye.debugger.Debugger;
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
			Debugger.instance.out.println("System Startup...");
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
			Callbacks.instance.hashCode();
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Step 5: Read in Manifest TODO:
		
		
	}
	
}
