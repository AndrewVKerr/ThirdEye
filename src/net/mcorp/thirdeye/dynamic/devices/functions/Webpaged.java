package net.mcorp.thirdeye.dynamic.devices.functions;

import java.util.HashMap;

import net.mcorp.thirdeye.systems.network.clients.Client;

/**
 * <h1>Webpaged</h1>
 * <hr>
 * <p>
 * 	This interface defines the method {@linkplain #httpResponse(Client, String, HashMap)} which is used to retrieve
 * 	a binary version of the webpage as well as performing any applicable actions.
 * </p>
 * @author Andrew Kerr
 */
public interface Webpaged {
	
	/**
	 * Performs any actions recognized within the url or the query's list, before returning a binary
	 * version of this {@linkplain Devices} webpage.
	 * @param client - {@linkplain Client} - The client asking for the webpage.
	 * @param protocol - {@linkplain String} - The http protocol. (GET, HEAD, POST, etc.)
	 * @param url - {@linkplain String} - The url of the request.
	 * @param querys - {@linkplain HashMap}&lt;{@linkplain String},{@linkplain String}&gt; - A HashMap of query's.
	 * @implNote The HashMap called query's may contain some keys with null values paired up.
	 */
	public void httpResponse(Client client, String protocol, String url, HashMap<String,String> querys) throws Exception;
	
	
	
}
