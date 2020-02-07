package net.mcorp.thirdeye.callbacks;

import java.util.ArrayList;

/**
 * <h1>CallbackGroup</h1>
 * <hr>
 * <p>
 * 	This class is used to create groups of {@linkplain Callback}'s. Calling the groups {@linkplain #callback(Object)} method
 * 	will result in each callback being called.
 * </p>
 * @author Andrew Kerr
 * @param <CallbackObj> - The object class passed by an event.
 */
public class CallbackGroup<CallbackObj> {
	
	/**
	 * A collection of {@linkplain Callback}'s stored in this group.
	 */
	private final ArrayList<Callback<CallbackObj>> callbacks = new ArrayList<Callback<CallbackObj>>();
	
	/**
	 * Registers a {@linkplain Callback} with this object.
	 * @param callback - {@linkplain Callback}&lt;{@linkplain CallbackObj}&gt; - A callback object. 
	 * @return {@linkplain Boolean} - True if the callback was successfully added to the list. False otherwise.
	 */
	public final boolean registerCallback(Callback<CallbackObj> callback) {
		return callbacks.add(callback);
	}
	
	/**
	 * UnRegisters a {@linkplain Callback} with this object.
	 * @param callback - {@linkplain Callback}&lt;{@linkplain CallbackObj}&gt; - A callback object. 
	 * @return {@linkplain Boolean} - True if the callback was successfully removed from the list. False otherwise.
	 */
	public final boolean unRegisterCallback(Callback<CallbackObj> callback) {
		return callbacks.add(callback);
	}
	
	/**
	 * Returns a {@linkplain Callback}&lt;{@linkplain CallbackObj}&gt;[] array.
	 * @return {@linkplain Callback}&lt;{@linkplain CallbackObj}&gt; - An array of callback objects.
	 */
	public final Callback<CallbackObj>[] getCallbacks(){
		@SuppressWarnings("unchecked")
		Callback<CallbackObj>[] cback = (Callback<CallbackObj>[]) new Callback<?>[0];
		return this.callbacks.toArray(cback);
	}
	
	/**
	 * This method is called in response to an event.
	 * @param object - {@linkplain CallbackObj} - The object passed by the event.
	 */
	public final void callback(CallbackObj object) {
		
		for(Callback<CallbackObj> callback : callbacks) {
			if(callback == null)
				continue;
			try {
				callback.callback(object);
			}catch(Exception e) {}
		}
		
	}
	
}
