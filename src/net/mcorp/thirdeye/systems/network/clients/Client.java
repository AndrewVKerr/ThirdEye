package net.mcorp.thirdeye.systems.network.clients;

import java.io.File;
import java.net.Socket;

import net.mcorp.thirdeye.ThirdEye;

/**
 * <h1>Client</h1>
 * <hr>
 * <p>
 * 	This abstract class defines the most basic functions that the client should be able to do.
 * </p>
 * @author Andrew Kerr
 */
public abstract class Client implements Runnable{
	
	public static final File WORKSPACE = new File(ThirdEye.directory().getAbsolutePath()+"/Network");
	
	/**
	 * The socket object given to the {@linkplain Client} object at creation.
	 * @implNote This socket should remain open until the end of the {@linkplain #run()} method;
	 * however, it <b><u>MUST</u></b> be <b><u>CLOSED</u></b> before the run method returns.
	 */
	public final Socket socket;
	
	/**
	 * Constructor for {@linkplain Client}.
	 * @param socket - {@linkplain Socket} - The socket object for this client.
	 */
	public Client(Socket socket) {
		this.socket = socket;
	}
	
	public abstract void handle() throws Exception;
	
	@Override
	public void run() {
		try {
			this.handle();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.socket.getOutputStream().write(this.genericExceptionResponse(e));
			}catch(Exception e1) {}
		}finally {
			if(this.socket.isClosed() == false) {
				try {
					this.socket.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public abstract byte[] genericExceptionResponse(Exception e);
	
}
