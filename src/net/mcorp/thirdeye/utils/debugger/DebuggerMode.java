package net.mcorp.thirdeye.utils.debugger;

import java.awt.GraphicsEnvironment;

/**
 * <h1>DebuggerMode</h1>
 * <hr>
 * <p>
 * 	Sets the mode of a {@linkplain Debugger} object.
 * </p>
 * @author Andrew Kerr
 */
public enum DebuggerMode {
	
	/**
	 * First attempts to create a graphical display.<br>
	 * If that fails or is impossible due to {@linkplain GraphicsEnvironment#isHeadless()} returning false then it will switch over to a console based system.
	 */
	GraphicalOrConsole,
	
	/**
	 * Attempts to create a graphical display.<br>
	 * If that fails or is impossible due to {@linkplain GraphicsEnvironment#isHeadless()} returning false then it will simply exit the program.
	 * @implNote This is used only in instances where you wish for your program to bail if you cannot create a window and don't want to spam your console with text.
	 */
	GraphicalOnly,
	
	/**
	 * First attempts to create a graphical display.<br>
	 * If that fails or is impossible due to {@linkplain GraphicsEnvironment#isHeadless()} returning false then it will 
	 * simply disables itself and allows the overlaying program to do the error handling.
	 */
	GraphicalOrDisabled,
	
	/**
	 * Attempts to create a graphical display.<br>
	 * If that fails or is impossible due to {@linkplain GraphicsEnvironment#isHeadless()} returning false then it will simply exit the program.
	 * @implNote This is used only in instances where you wish for your program to bail if you cannot print to console.
	 */
	ConsoleOnly,
	
	/**
	 * Simply disables the {@linkplain Debugger} and allows the overlaying program to do the error handling.
	 * @apiNote This only disables the Debugger's displaying of stored information. The Debugger will still collect, store, provide {@linkplain DebugException} objects.
	 */
	Disabled;
	
}
