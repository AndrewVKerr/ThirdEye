package net.mcorp.thirdeye.utils.debugger;

import java.time.LocalTime;

/**
 * <h1>DebugException</h1>
 * <hr>
 * <p>
 * 	This class contains a {@linkplain Exception} object and a {@linkplain LocalTime} object. Using these objects allows for the ability to record an exception at a
 * 	specific time or to raise the exception again at a later date.
 * </p>
 * @author Andrew Kerr
 */
public class DebugException {
	
	/**
	 * {@linkplain Exception} - The exception that is associated with this object.
	 * @implNote Should not be accessed directly. This was left public and not final so that a class can extend this class.
	 * Access using {@linkplain #exception()} or {@linkplain #throwException()}.
	 */
	public Exception exception;
	
	/**
	 * {@linkplain LocalTime} - The time object that is associated with the {@linkplain #exception}.
	 * @implNote Should not be accessed directly. This was left public and not final so that a class can extend this class.
	 * Access using {@linkplain #time()}.
	 */
	public LocalTime time;
	
	/**
	 * Creates a new {@linkplain DebugException} object. The {@linkplain #time} object is set to {@linkplain LocalTime#now()}.
	 * @param exception - {@linkplain Exception} - Sets {@linkplain #exception} to this object.
	 */
	public DebugException(Exception exception) {
		this.exception = exception;
		this.time = LocalTime.now();
	}
	
	/**
	 * Creates a new {@linkplain DebugException} object.
	 * @param exception - {@linkplain Exception} - Sets {@linkplain #exception} to this object.
	 * @param time - {@linkplain LocalTime} - Sets {@linkplain #time} to this object.
	 */
	public DebugException(Exception exception, LocalTime time) {
		this.exception = exception;
		this.time = time;
	}
	
	/**
	 * Returns the {@linkplain Exception} associated with this object.
	 * @return {@linkplain Exception} - The exception associated with this object at this {@linkplain #time}.
	 */
	public Exception exception() {
		return exception;
	}
	
	/**
	 * Throws the stored {@linkplain #exception} for stack-tracing purposes.
	 * @throws Exception Throws the stored Exception stored at {@linkplain #exception}.
	 * @implNote If you are trying to access the {@linkplain #exception} parameter use the {@linkplain #exception()} method instead of throwing and catching.
	 */
	public void throwException() throws Exception{
		throw exception;
	}
	
	/**
	 * The time at which this exception was either thrown or the time at which this object was created.
	 * @return {@linkplain LocalTime} - A time object stored within the {@linkplain #time} variable.
	 */
	public LocalTime time() {
		return this.time;
	}
	
}
