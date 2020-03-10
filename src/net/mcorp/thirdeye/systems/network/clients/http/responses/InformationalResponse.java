package net.mcorp.thirdeye.systems.network.clients.http.responses;

/**
 * <h1>InformationalResponse</h1>
 * <hr>
 * <p>
 * 	This class defines the most basic structure for a Informational HTTP Response packet.
 * 	This class can only result in codes within the range of 100 and 199 inclusively.
 * </p>
 * @author Andrew Kerr
 */
public class InformationalResponse extends HttpResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2800299454015298049L;

	/**
	 * The public constructor for the {@linkplain InformationalResponse} class.
	 * @param version - {@linkplain HttpVersion} - The HTTP version.
	 * @param code - {@linkplain Integer} - The code used for this packet. Must be between 100 and 199.
	 * @param message - {@linkplain String} - The short and sweet message associated with the provided code.
	 */
	public InformationalResponse(HttpVersion version, int code, String message) {
		super(version, code, message);
		if(code < 100 || code > 199)
			throw new IndexOutOfBoundsException("A Informational HTTP Response needs to use a code between 100 and 199 inclusive.");
	}

	/**
	 * Returns a null value due to the 1xx Series Informational HTTP Responses cannot have a payload attached to them.
	 * @return {@linkplain Byte byte[]} - Returns a null value as the Informational HTTP Responses cannot have a payload attached to them.
	 */
	public final byte[] payload() {
		return null;
	}

}
