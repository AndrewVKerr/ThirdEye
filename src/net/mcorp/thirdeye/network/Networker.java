package net.mcorp.thirdeye.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import net.mcorp.thirdeye.devices.Device;

public final class Networker {
	
	public static final Networker instance = new Networker();
	
	private HashMap<Device,ArrayList<String>> deviceHostnames = new HashMap<Device,ArrayList<String>>();
	
	public boolean registerHostname(Device device, String host) {
		ArrayList<String> host_list = deviceHostnames.get(device);
		if(host_list == null) {
			host_list = new ArrayList<String>();
			deviceHostnames.put(device, host_list);
		}
		return host_list.add(host);
	}
	
	public boolean unRegisterHostname(Device device, String host) {
		ArrayList<String> host_list = deviceHostnames.get(device);
		if(host_list == null) {
			return false;
		}
		return host_list.remove(host);
	}
	
	public Socket createSocket(String host, int port) throws UnknownHostException, IOException {
		return new Socket(host,port);
	}
	
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws UnknownHostException, IOException {
		return new Socket(host, port, localAddress, localPort);
	}
	
}
