package net.mcorp.thirdeye.dynamic.devices.functions;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <h1>Scheduled</h1>
 * <hr>
 * <p>
 * 	This interface is used as a means to check if the current {@linkplain LocalDate} and {@linkplain LocalTime} match a predefined
 * 	Schedule. Using the {@link #isActive(LocalDate, LocalTime)} method you can check if the given LocalDate/LocalTime is a active
 * 	time for the given {@linkplain Device}.
 * </p>
 * @author Andrew Kerr
 * @implNote This interface is used to schedule an event such as enabling recording. This interface is accepted as a means of
 * setting a Device's state (such as recording) instead of manual; however, it is strongly recommended that you allow for both
 * of these conditions to be used. If possible please allow the manual setting of the state to take precedence over this automatic
 * one.
 */
public interface Scheduled {
	
	/**
	 * Checks to see if the {@linkplain Device} is active during the given LocalDate/LocalTime.
	 * @param ld - {@linkplain LocalDate} - A date for which to check with.
	 * @param lt - {@linkplain LocalTime} - A time for which to check with.
	 * @return {@linkplain Boolean} - True if the Device is active during the given time/date, otherwise false.
	 */
	public boolean isActive(LocalDate ld, LocalTime lt);
	
}
