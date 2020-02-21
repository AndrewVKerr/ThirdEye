package net.mcorp.thirdeye.debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;

import net.mcorp.thirdeye.ThirdEye;

/**
 * <h1>Debugger</h1>
 * <hr>
 * <p>
 * 	This class contains several {@linkplain PrintStream}'s that are used to write out information
 * 	to {@linkplain File}'s. These PrintStreams include {@linkplain #out}, {@linkplain #err}, {@linkplain #warn}.
 * </p>
 * @author Andrew Kerr
 */
public final class Debugger{
	
	public static final Debugger instance = new Debugger();
	
	public final String logsFilePath;
	
	public final File logFile;
	public final File errorFile;
	public final File warnFile;
	
	/**
	 * {@linkplain PrintStream} - Used to print information used for debugging purposes, or for logging purposes.
	 */
	public final PrintStream out;
	
	/**
	 * Returns the out PrintStream.
	 * @return {@linkplain #out}
	 */
	public static final PrintStream out() {
		return instance.out;
	}
	
	/**
	 * {@linkplain PrintStream} - Used to print out error's that occurred throughout the program.
	 */
	public final PrintStream err;
	
	/**
	 * Returns the error PrintStream.
	 * @return {@linkplain #err}
	 */
	public static final PrintStream err() {
		return instance.err;
	}
	
	/**
	 * {@linkplain PrintStream} - Used to print out warning that have occurred throughout the program.
	 */
	public final PrintStream warn;
	
	/**
	 * Returns the warn PrintStream.
	 * @return {@linkplain #warn}
	 */
	public static final PrintStream warn() {
		return instance.warn;
	}
	
	/**
	 * Creates a new {@linkplain Debugger} object.
	 */
	private Debugger() {
		
		//Set the logs folder path.
		String tempFilePath = ThirdEye.directory().getAbsolutePath()+"/debugger/logs/";
		
		//Create the logs folder if it was not found.
		if(!new File(tempFilePath).exists()) {
			try {
				Files.createDirectories(new File(tempFilePath).toPath());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(404);
				throw new RuntimeException(e);
			}
		}
		
		//Set the static final path to the temporary path.
		logsFilePath = tempFilePath;
		
		//Assign the files to objects.
		logFile = new File(logsFilePath+"/debug.log");
		errorFile = new File(logsFilePath+"/error.log");
		warnFile = new File(logsFilePath+"/warn.log");
		
		//Attempt to assign PrintStreams with files into objects.
		try {
			out = new PrintStream(logFile);
			err = new PrintStream(errorFile);
			warn = new PrintStream(warnFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(404);
			throw new RuntimeException(e);
		}
		
		//Override the System defaults. Disabled for debugging.
		//System.setErr(err);
		//System.setOut(out);
		
		//Print the execution start date/time.
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		out.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
		err.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
		warn.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
	}
	
}
