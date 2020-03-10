package net.mcorp.thirdeye.systems.network.clients.http.responses;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>HttpResponse</h1>
 * <hr>
 * <p>
 * 	This class is used as the basis for all http responses. It defines all the necessary variables and methods needed
 * 	to send a valid http packet to the client. This class happens to also extend {@linkplain IOException}, this was done
 * 	so that you could throw these responses and catch them as a default response if a proper response could not be made.
 * 	Example: You wanted to send a webpage but you encountered a {@linkplain FileNotFoundException} you could after catching the
 * 	exception throw a 404 Exception in its place.
 * </p>
 * @author Andrew Kerr
 * @see IOException
 */
public abstract class HttpResponse extends IOException {
	
	/**
	 * A unique serial version id.
	 * @see Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 4879607886806544050L;

	/**
	 * {@linkplain HttpVersion} - The HTTP version.
	 */
	public final HttpVersion version;
	
	/**
	 * {@linkplain Integer} - This is an integer that follows the HTTP Status Code Standard defined by the 
	 * <a href="https://www.ietf.org/assignments/http-status-codes/http-status-codes.xml">ietf</a> standards.
	 */
	public final int code;
	
	/**
	 * {@linkplain String} - This is a string representing a short simple yet descriptive message that will be
	 * associated with the stored {@linkplain #code}.
	 */
	public final String message;
	
	/**
	 * A public constructor for the {@linkplain HttpResponse} class.
	 * @param code - {@linkplain Integer} - Defines the {@linkplain #code} object variable.
	 * @param message - {@linkplain String} - Defines the {@link #message} object variable.
	 */
	public HttpResponse(HttpVersion version, int code, String message) {
		this.version = version;
		this.code = code;
		if(message != null)
			this.message = message;
		else
			this.message = "";
	}
	
	/**
	 * {@linkplain ArrayList ArrayList&lt;String&gt;} - This is the stored HTTP headers associated with this
	 * response object. 
	 */
	protected final ArrayList<String> headers = new ArrayList<String>();
	
	/**
	 * Converts the {@link #headers} object variable into a String array.
	 * @return {@linkplain String String[]} - A String array containing the data in {@linkplain #headers}.
	 */
	public final String[] headers() {
		return headers.toArray(new String[] {});
	}
	
	/**
	 * Returns the payload data associated with this response or null if this response isn't allowed to have a payload.
	 * @return {@linkplain Byte byte[]} - A byte array of payload data or null if response cannot have a payload. 
	 */
	public abstract byte[] payload();
	
	/**
	 * Combines the stored data within this object and returns the binary representation of it.
	 * @return {@linkplain Byte byte[]} - A byte array of the combined {@link #code}, {@link #message}, {@link #headers()}, {@link #payload()}.
	 */
	public final byte[] getBytes() {
		
		//Combine all plaintext data together, code, message, version, headers...
		String http = "";
		http += "Http/1.1 "+code+" "+message+"\n";
		String[] headers = headers();
		if(headers != null)
			for(String header : headers) {
				http += header+"\n";
			}
		http+="\n";
		
		//Convert plaintext into binary, retrieve payload, and create new byte array of the combined lengths.
		byte[] http_data = http.getBytes();
		byte[] payload = payload();
		byte[] resulting_http_payload = new byte[http_data.length + (payload == null ? 0 : payload.length)];
		
		//Loop through all data and merge it into resulting byte array.
		int i = 0;
		for(byte b : http_data) {
			resulting_http_payload[i] = b;
			i++;
		}
		
		if(payload != null)
			for(byte b : payload) {
				resulting_http_payload[i] = b;
				i++;
			}
		
		//Return result.
		return resulting_http_payload;
		
	}
	
}
