 package net.mcorp.thirdeye.systems;

import java.io.File;
import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.AccessControlContext;
import java.security.Permission;

import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.dynamic.AccessRight;
import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;
import net.mcorp.thirdeye.systems.callbacks.Callbacks;

public class TESecurityManager extends SecurityManager{
	
	public static final TESecurityManager instance = new TESecurityManager();
	
	public TESecurityManager() {
		super();
	}
	
	public void checkAccept(String host, int port) {
		
		Thread current_thread = Thread.currentThread();
		
		if(current_thread.getName().equals("system") || current_thread.getName().equals("main"))
			return;
		
		if(ThreadManager.instance() == null || Manifest.instance == null)
			return;
		
		if(DataServer.instance().isThread(current_thread))
			return;
		
		Device device = ThreadManager.instance().getDevice(current_thread.getThreadGroup()); 
		
		for(JavaClass java_class : Manifest.instance.classes()) {
			if(java_class.instance() == device) {
				for(AccessRight right : java_class.getAccessRights()) {
					if(right == AccessRight.NetworkingHost) {
						return;
					}
				}
			}
		}
		
		throw new SecurityException("Current Thread does not have access rights to connect.");
		
	}
	
	public void checkAccess(Thread t) {
		/*Thread currentThread = Thread.currentThread();
		ThreadGroup currentThreadGroup = currentThread.getThreadGroup();
		if(currentThreadGroup.getName().equals("main") || currentThreadGroup.getName().equals("system")) {
			
		}*/
	}
	
	public void checkAccess(ThreadGroup g) {
		Thread current_thread = Thread.currentThread();
		
		if(current_thread.getName().equals("system") || current_thread.getName().equals("main"))
			return;
		
		//Check current stack, because we are cross checking ThreadGroups we need to make
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if(ste[1].getMethodName().equals("checkAccess") && ste[1].getClassName().equals(this.getClass().getName()))
			return;
		
		if(ThreadManager.instance() == null || Manifest.instance == null)
			return;
		
		Device device = ThreadManager.instance().getDevice(current_thread.getThreadGroup());
		if(device != null) {
			
			for(JavaClass java_class : Manifest.instance.classes()) {
				if(java_class.instance() == device) {
					for(AccessRight right : java_class.getAccessRights()) {
						if(right == AccessRight.Threading) {
							return;
						}
					}
				}
			}
			
		}
		
		throw new SecurityException("Current Thread does not have a right to start a thread.");
	}
	
	public void checkConnect(String host, int port) {
		if(port == -1)
			return;
		
		Thread current_thread = Thread.currentThread();
		
		if(current_thread.getName().equals("system") || current_thread.getName().equals("main"))
			return;
		
		if(ThreadManager.instance() == null || Manifest.instance == null)
			return;
		
		if(DataServer.instance().isThread(current_thread))
			return;
		
		Device device = ThreadManager.instance().getDevice(current_thread.getThreadGroup()); 
		
		for(JavaClass java_class : Manifest.instance.classes()) {
			if(java_class.instance() == device) {
				for(AccessRight right : java_class.getAccessRights()) {
					if(right == AccessRight.NetworkingConnect) {
						return;
					}
				}
			}
		}
		
		throw new SecurityException("Current Thread does not have access rights to connect.");
	}
	
	public void checkConnect(String host, int port, Object context) {
		if(port == -1)
			return;
		
		if(!(context instanceof AccessControlContext))
			throw new SecurityException("Context is not an instanceof AccessControlContext");
		
		Thread current_thread = Thread.currentThread();
		
		if(current_thread.getName().equals("system") || current_thread.getName().equals("main"))
			return;
		
		if(ThreadManager.instance() == null || Manifest.instance == null)
			return;
		
		Device device = ThreadManager.instance().getDevice(current_thread.getThreadGroup()); 
		
		for(JavaClass java_class : Manifest.instance.classes()) {
			if(java_class.instance() == device) {
				for(AccessRight right : java_class.getAccessRights()) {
					if(right == AccessRight.NetworkingConnect) {
						return;
					}
				}
			}
		}
		
		throw new SecurityException("Current Thread does not have access rights to connect.");
	}
	
	public void checkCreateClassLoader() {
		//TODO:
	}
	
	public void checkDelete(String file) {
		File file_ = new File(file);
		
		Thread current_thread = Thread.currentThread();
		
		if(current_thread.getName().equals("system") || current_thread.getName().equals("main"))
			return;
		
		if(ThreadManager.instance() == null || Manifest.instance == null)
			return;
		
		if(DataServer.instance().isThread(current_thread))
			if(file.startsWith("/tmp"))
				return;
		
		Device device = ThreadManager.instance().getDevice(current_thread.getThreadGroup()); 
		
		for(JavaClass java_class : Manifest.instance.classes()) {
			if(java_class.instance() == device) {
				for(AccessRight right : java_class.getAccessRights()) {
					if(right == AccessRight.FileWrite) {
						//Allows temporary file storage used by some things like ImageIO.
						if(file.startsWith("/tmp"))
							return;
						if(file_.isDirectory()) {
							if(java_class.getWorkspace().getAbsolutePath().contains(file_.getAbsolutePath())) {
								return;
							}
						}else{
							if(java_class.getWorkspace().getAbsolutePath().contains(file_.getParent())) {
								return;
							}
						}
						throw new SecurityException("Current Thread associated with Device ["+device.deviceName()+"] does not have any right to delete a file.");
					}
				}
			}
		}
		
		Debugger.warn().println("Current Thread ["+current_thread+"] has no right to delete file ["+file+"]!");
		throw new SecurityException("Current Thread does not have rights to delete this file.");
	}
	
	public void checkExec(String cmd) {
		
	}
	
	public void checkExit(int status) {
		Thread t = Thread.currentThread();
		if(ThreadManager.instance().isThreadRegistered(t)) {
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
		System.out.println(target);
	}
	
	public void checkSetFactory() {
		
	}
	
	public void checkWrite(FileDescriptor fd) {
		
	}
	
	public void checkWrite(String file) {
		
	}
	
}
