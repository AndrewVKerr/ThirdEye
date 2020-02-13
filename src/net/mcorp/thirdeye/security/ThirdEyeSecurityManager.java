package net.mcorp.thirdeye.security;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

import net.mcorp.thirdeye.callbacks.Callbacks;
import net.mcorp.thirdeye.threading.Threader;

public class ThirdEyeSecurityManager extends SecurityManager{
	
	public static final ThirdEyeSecurityManager instance = new ThirdEyeSecurityManager();
	
	public ThirdEyeSecurityManager() {
		super();
	}
	
	public void checkAccept(String host, int port) {
		if(port == 80) {
			if(host.startsWith("192.168.")) {
				
			}
		}
	}
	
	public void checkAccess(Thread t) {
		//System.out.println(t);
	}
	
	public void checkAccess(ThreadGroup g) {
		Thread thread = Thread.currentThread();
		if(!Threader.instance.isThreadRegistered(thread)) {
			throw new SecurityException("Threads must be made using the Threader.instance.createThread(...) method.");
		}
	}
	
	public void checkConnect(String host, int port) {
		
	}
	
	public void checkConnect(String host, int port, Object context) {
		
	}
	
	public void checkCreateClassLoader() {
		
	}
	
	public void checkDelete(String file) {
		
	}
	
	public void checkExec(String cmd) {
		
	}
	
	public void checkExit(int status) {
		Thread t = Thread.currentThread();
		if(Threader.instance.isThreadRegistered(t)) {
			return;
		}else {
			throw new SecurityException("Current Thread is not registered with Threader.");
		}
	}
	
	public void checkLink(String lib) {
		
	}
	
	public void checkListen(int port) {
		
	}
	
	public void checkMulticast(InetAddress maddr) {
		
	}
	
	public void checkPackageAccess(String pkg) {
		
	}
	
	public void checkPackageDefinition(String pkg) {
		
	}
	
	public void checkPermission(Permission perm) {
		//System.out.println(perm);
	}
	
	public void checkPermission(Permission perm, Object context) {
		//System.out.println(perm);
	}
	
	public void checkPrintJobAccess() {
		
	}
	
	public void checkPropertiesAccess() {
		
	}
	
	public void checkPropertyAccess(String key) {
		//System.out.println(key);
	}
	
	public void checkRead(FileDescriptor fd) {
		
	}
	
	public void checkRead(String file) {
		
	}
	
	public void checkRead(String file, Object context) {
		
	}
	
	public void checkSecurityAccess(String target) {
		//System.out.println(target);
	}
	
	public void checkSetFactory() {
		
	}
	
	public void checkWrite(FileDescriptor fd) {
		
	}
	
	public void checkWrite(String file) {
		
	}
	
}
