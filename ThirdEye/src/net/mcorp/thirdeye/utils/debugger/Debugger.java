package net.mcorp.thirdeye.utils.debugger;

import java.awt.GraphicsEnvironment;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * <h1>Debugger</h1>
 * <hr>
 * <p>
 * </p>
 * @author Andrew Kerr
 */
public class Debugger {
	
	public static Debugger debugger = new Debugger(DebuggerMode.GraphicalOrConsole);
	public static final Debugger debugger() { return debugger; };
	
	/**
	 * A collection of {@linkplain DebugException} objects.
	 * @implNote This ArrayList should not be interacted with directly as it could cause unexpected results and/or desync.
	 */
	public final ArrayList<DebugException> debugExceptions = new ArrayList<DebugException>();
	
	public final DebuggerMode mode;
	
	public Debugger(DebuggerMode mode) {
		this.mode = mode;
		
		if(mode == DebuggerMode.Disabled)
			return;
		if(mode == DebuggerMode.ConsoleOnly)
			setupConsole();
		
		boolean isHeadless = GraphicsEnvironment.isHeadless();
		if(mode == DebuggerMode.GraphicalOnly) {
			if(isHeadless) {
				System.err.println("Could not initialize Debugger due to a missing GraphicsEnvironment. Mode:"+mode);
				System.exit(1);
			}
			setupGraphics();
		}
		if(mode == DebuggerMode.GraphicalOrDisabled) {
			if(isHeadless) {
				return;
			}
			setupGraphics();
		}
		if(mode == DebuggerMode.GraphicalOrConsole) {
			if(isHeadless) {
				setupConsole();
			}else {
				setupGraphics();
			}
		}
			
	}
	
	/**
	 * Sets up the debugger to use a console base approach.
	 * @implNote This console should be used outside of the development environment as it contains
	 * specific unicode characters that may or may not be recognized by the IDE's console due to the console being a stream rather than a "console".
	 */
	protected void setupConsole() {
		
	}
	
	/**
	 * Sets up the debugger to use a graphical base approach.
	 * @implNote This approach should only be used in a graphical environment, any attempt to use this method
	 * outside of a graphical environment should result in a false statement.
	 */
	protected boolean setupGraphics() {
		try {
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Creates a new {@linkplain DebugException} object using the given parameters, this object is then added to the {@linkplain #debugExceptions} arraylist before being returned.
	 * @param e - {@linkplain Exception} - An exception that should be stored.
	 * @return {@linkplain DebugException} - The object created for the given parameters, or null if the object could not be added to the {@linkplain #debugExceptions} list.
	 */
	public DebugException catchException(Exception e) {
		DebugException de = new DebugException(e);
		if(this.debugExceptions.add(de))
			return de;
		return null;
	}
	
	public DebugException catchException(Exception e, LocalTime time) {
		DebugException de = new DebugException(e,time);
		if(this.debugExceptions.add(de))
			return de;
		return null;
	}
	
	/*public void println(String message) {
		try {
			this.output.write(message.getBytes());
		}catch(Exception e) {
			this.exception = e;
		}
	}
	
	public void println(Exception e) {
		try {
			String message = this.exceptionMessageTemplate;
			message = message.replaceAll("{Class}", e.getClass().getSimpleName());
			message = message.replaceAll("{Message}", e.getLocalizedMessage());
			this.output.write(message.getBytes());
		}catch(Exception e1) {
			this.exception = e1;
		}
	}*/
	
}
