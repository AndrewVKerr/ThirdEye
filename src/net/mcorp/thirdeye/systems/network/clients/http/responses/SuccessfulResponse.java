package net.mcorp.thirdeye.systems.network.clients.http.responses;

public class SuccessfulResponse extends HttpResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8029810391308233104L;
	
	/**
	 * {@linkplain Byte byte[]} - A byte array of data returned by {@linkplain #payload()}.
	 */
	private byte[] payload;
	
	/**
	 * The public constructor for the {@linkplain SuccessfulResponse} class.
	 * @param version - {@linkplain HttpVersion} - The HTTP version.
	 * @param code - {@linkplain Integer} - The code used for this packet. Must be between 100 and 199.
	 * @param message - {@linkplain String} - The short and sweet message associated with the provided code.
	 */
	public SuccessfulResponse(HttpVersion version, int code, String message, byte[] payload) {
		super(version, code, message);
		if(code < 200 || code > 299)
			throw new IndexOutOfBoundsException("A Successful HTTP Response needs to use a code between 200 and 299 inclusive.");
		this.payload = payload;
	}

	@Override
	public byte[] payload() {
		return payload;
	}
	
}
