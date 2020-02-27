package net.mcorp.thirdeye.systems.network.clients.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.dynamic.devices.Device;
import net.mcorp.thirdeye.dynamic.devices.functions.Webpaged;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;
import net.mcorp.thirdeye.systems.network.clients.Client;

public class HttpClient extends Client {
	
	public static final File HTTP_WORKSPACE = new File(Client.WORKSPACE.getAbsolutePath()+"/http");
	
	public HttpClient(Socket socket) {
		super(socket);
	}
	
	@Override
	public void handle() throws Exception {
		//Get Http Frame
		InputStream in = socket.getInputStream();
		String header = "";
		char c;
		while((c = (char) in.read()) != '\n') {
			header += c;
		}
		
		while(in.available() > 0) {
			in.read();
		}
		
		String[] temp = header.split(" ");
		if(temp.length < 3) {
			socket.getOutputStream().write("Http/1.1 400 Client Error\nConnection:Closed\n\n".getBytes());
			socket.close();
		}
			
		String protocol = temp[0];
		String[] url_with_query = temp[1].split(Pattern.quote("?"),2);
		String version = temp[2];
		
		//if(!version.equalsIgnoreCase("HTTP/1.1"))
		//	throw new IOException("Incorrect version can not ensure proper decoding!!! Given:["+version+"] Required:[http/1.1]");
		
		String url = url_with_query[0];
		HashMap<String,String> querys = new HashMap<String,String>();
		if(url_with_query.length == 2) {
			String[] temp_querys = url_with_query[1].split("&");
			for(String temp_query : temp_querys) {
				if(temp_query.contains("=")) {
					String[] query = temp_query.split("=",2);
					querys.put(query[0], query[1]);
				}else {
					querys.put(temp_query, null);
				}
			}
		}
		
		//Handle
		OutputStream out = socket.getOutputStream();
		
		//1st priority is to display prefabricated html/css/javascript/etc... info.
		File file;
		if((file = new File(HTTP_WORKSPACE.getAbsolutePath()+url)).exists() && file.isDirectory() == false) {
			out.write(HttpClient.response(200, "Ok", new String[] {"Connection:Closed"}, Files.readAllBytes(file.toPath())));
			out.flush();
			return;
		}
		
		//2nd priority is to display any information from a device.
		Device device;
		for(JavaClass javaclass : Manifest.instance().classes()) {
			Device temp_device = javaclass.instance();
			if(temp_device != null) {
				String device_url = "/"+temp_device.deviceName()+"/"+javaclass.name();
				if(url.startsWith(device_url)) {
					device = temp_device;
					String temp_url = url.substring(device_url.length());
					if(temp_device instanceof Webpaged) {
						((Webpaged)temp_device).sendWebpageAndPerformActions(this,temp_url,querys);
						out.flush();
						return;
					}
				}
			}
		}
		
		//3rd priority is to display a default window.
		if(url.equalsIgnoreCase("/")) {
			String html = "";
			html += "<html>\n";
			html += "\t<head>\n";
			html += "\t\t<title>ThirdEye Directory</title>\n";
			html += "\t</head>\n";
			html += "\t<body>\n";
			html += "\t\t<h1>ThirdEye Device Directory</h1>\n";
			html += "\t\t<hr>\n";
			for(JavaClass javaclass : Manifest.instance().classes()) {
				Device temp_device = javaclass.instance();
				if(temp_device != null) {
					html += "\t\t<a href='/"+temp_device.deviceName()+"/"+javaclass.name()+"'>"+javaclass.name()+"</a>\n";
				}
			}
			html += "\t\t \n";
			html += "\t</body>\n";
			html += "</html>";
			out.write(HttpClient.response(200, "OK", new String[] {"Connection:Closed"}, html.getBytes()));
			out.flush();
			return;
		}
		
		//Last thing is to display a 404 error page.
		out.write(HttpClient.response(404, "Not Found", null, null));
		out.flush();
		
	}

	public static byte[] response(int code, String code_text, String[] headers, byte[] payload) {
		String http = "Http/1.1 "+code+" "+code_text+"\n";
		if(headers != null) {
			for(String header : headers) {
				http += header+"\n";
			}
		}
		http += "\n";
		
		byte[] data = http.getBytes();
		byte[] result = new byte[data.length + (payload == null ? 0 : payload.length)];
		int i = 0;
		for(byte b : data) {
			result[i] = b;
			i++;
		}
		if(payload != null) {
			for(byte b : payload) {
				result[i] = b;
				i++;
			}
		}
		
		return result;
	}

	public byte[] getGenericResponse(int code, String text) {
		return HttpClient.response(code, text, null, null);
	}

	@Override
	public byte[] genericExceptionResponse(Exception e) {
		return HttpClient.response(500, "Server Exception", null, e.getLocalizedMessage().getBytes());
	}

}
