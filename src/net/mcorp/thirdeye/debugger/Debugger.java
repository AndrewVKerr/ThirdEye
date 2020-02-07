package net.mcorp.thirdeye.debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;

public final class Debugger{
	
	public static final Debugger instance = new Debugger();
	
	public final String logsFilePath;
	
	public final File logFile;
	public final File errorFile;
	
	public final PrintStream out;
	public static final PrintStream out() {
		return instance.out;
	}
	
	public final PrintStream err;
	public static final PrintStream err() {
		return instance.err;
	}
	
	private Debugger() {
		String tempFilePath = "/opt/ThirdEye/debugger/logs/";
		if(!new File(tempFilePath).exists()) {
			try {
				Files.createDirectories(new File(tempFilePath).toPath());
			} catch (IOException e) {
				try {
					tempFilePath = "/home/andrew/ThirdEye/";
					Files.createDirectories(new File(tempFilePath).toPath());
				}catch(IOException e1) {
					e1.addSuppressed(e);
					e1.printStackTrace();
					System.exit(404);
					throw new RuntimeException(e1);
				}
			}
		}
		logsFilePath = tempFilePath;
		
		logFile = new File(logsFilePath+"/log.txt");
		errorFile = new File(logsFilePath+"/error.txt");
		try {
			out = new PrintStream(logFile);
			err = new PrintStream(errorFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(404);
			throw new RuntimeException(e);
		}
		System.setErr(err);
		//System.setOut(out);
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		out.println("\n["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
		err.println("\n["+date.getMonth().toString()+"-"+date.getDayOfMonth()+"-"+date.getYear()+" @ "+time.getHour()+":"+time.getMinute()+":"+time.getSecond()+"]");
	}
	
}
