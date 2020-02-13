package net.mcorp.thirdeye.debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;

import net.mcorp.thirdeye.ThirdEye;

public final class Debugger{
	
	public static final Debugger instance = new Debugger();
	
	public final String logsFilePath;
	
	public final File logFile;
	public final File errorFile;
	public final File warnFile;
	
	public final PrintStream out;
	public static final PrintStream out() {
		return instance.out;
	}
	
	public final PrintStream err;
	public static final PrintStream err() {
		return instance.err;
	}
	
	public final PrintStream warn;
	public static final PrintStream warn() {
		return instance.warn;
	}
	
	private Debugger() {
		String tempFilePath = ThirdEye.directory().getAbsolutePath()+"/debugger/logs/";
		if(!new File(tempFilePath).exists()) {
			try {
				Files.createDirectories(new File(tempFilePath).toPath());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(404);
				throw new RuntimeException(e);
			}
		}
		logsFilePath = tempFilePath;
		
		logFile = new File(logsFilePath+"/debug.log");
		errorFile = new File(logsFilePath+"/error.log");
		warnFile = new File(logsFilePath+"/warn.log");
		try {
			out = new PrintStream(logFile);
			err = new PrintStream(errorFile);
			warn = new PrintStream(warnFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(404);
			throw new RuntimeException(e);
		}
		//System.setErr(err);
		//System.setOut(out);
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		out.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
		err.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
		warn.println("["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
	}
	
}
