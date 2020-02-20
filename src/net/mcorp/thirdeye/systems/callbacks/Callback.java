package net.mcorp.thirdeye.systems.callbacks;

/**
 * <h1>Callback</h1>
 * <hr>
 * <p>
 * 	The callback interface defines a single method ({@linkplain #callback(Object)}). The callback method is
 * 	called in response to a specific event.
 * </p>
 * @author Andrew Kerr
 * @param <Obj> - The object class passed by an event.
 */
public interface Callback<Obj> {
	
	/**
	 * This method is called in response to an event.
	 * @param object - {@linkplain Obj} - The object passed by the event.
	 */
	public void callback(Obj object);
	
}
