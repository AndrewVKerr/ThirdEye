package net.mcorp.thirdeye.systems;

/**
 * <h1>DataServer</h1>
 * <hr>
 * <p>
 * 	Only one object of this class will exist in memory at any one time. This object can only be accessed via the
 *  {@link DataServer#instance()} method. This class will be responsible for accepting and handling data from clients
 *  to {@linkplain Device}'s and vice versa.
 * </p>
 * @author Andrew Kerr
 */
public final class DataServer implements Runnable{
	
	/**
	 * The internal variable storing the only instance of DataServer.
	 * @implNote This should never be interacted with directly unless it is by the method {@linkplain #instance()}.
	 */
	private static DataServer instance;
	
	/**
	 * Returns the only instance of {@linkplain DataServer}. If one does not exist then it will be generated then
	 * returned.
	 * @return {@linkplain DataServer} - The only instance of DataServer.
	 */
	public static final synchronized DataServer instance() {
		if(instance == null)
			instance = new DataServer();
		return instance;
	};
	
	/**
	 * 
	 */
	private DataServer() {
		
	}

	@Override
	public void run() {
		
	};
	
}
