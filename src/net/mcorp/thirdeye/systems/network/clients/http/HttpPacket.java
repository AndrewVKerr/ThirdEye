package net.mcorp.thirdeye.systems.network.clients.http;

import java.io.IOException;
import java.util.HashMap;

public class HttpPacket {
	
	public final HttpClient client;
	
	public String url;
	
	public final HashMap<String, String> querys = new HashMap<String,String>();
	
	
	
	private HttpPacket(HttpClient client) {
		this.client = client;
	}
	
	private void readInData() throws IOException{
		
	}
	
	public static final HttpPacket readNextPacket(HttpClient client) throws IOException{
		HttpPacket packet = new HttpPacket(client);
		packet.readInData();
		return packet;
	}
	
}
