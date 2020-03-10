package net.mcorp.thirdeye.systems.network.clients.http.responses;

/**
 * <h1>HttpVersion</h1>
 * <hr>
 * <p>
 * 	This enum defines the several version of HTTP. This is used
 * 	to determine the resulting HTTP packets.
 * </p>
 * @author Andrew Kerr
 */
public enum HttpVersion {
	
	/**
	 * Http/1.0 - The first version of HTTP.
	 */
	HTTP_1_0,
	
	/**
	 * Http/1.1 - The second version of HTTP. (Most Common)
	 */
	HTTP_1_1,
	
	/**
	 * Http/2 - The third version of HTTP. 
	 */
	HTTP_2;
}
