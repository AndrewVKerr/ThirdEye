package net.mcorp.thirdeye.callbacks;

import net.mcorp.thirdeye.security.ThirdEyeSecurityManager;

/**
 * <h1>Callbacks</h1>
 * <hr>
 * <p>
 * 	This class is a collection of {@linkplain CallbackGroup}'s that are commonly used throughout different parts of the program.
 * </p>
 * @author Andrew Kerr
 */
public final class Callbacks {

	/**
	 * The static instance of the {@linkplain Callbacks} object.
	 */
	public static final Callbacks instance = new Callbacks();
	
	/**
	 * This callback group is called by the security manager ({@linkplain ThirdEyeSecurityManager}) before the program exits.
	 */
	public final CallbackGroup<Integer> onShutdown = new CallbackGroup<Integer>();
	
	private Callbacks() {}
	
}