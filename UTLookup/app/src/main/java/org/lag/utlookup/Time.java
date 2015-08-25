package org.lag.utlookup;

/**
 * A class to represent times at which meeting sections are taking place for 
 * courses. It is usually unambiguous as to whether the given times are 
 * during the day (i.e AM) or after noon (i.e PM).
 * @author Makram
 * @deprecated Do not use this class, use String instead to display naked times.
 */
@Deprecated
public class Time {
	// it is vital that we do not use an enum for this
	/**
	 * Days of the week.
	 */
	public static final int MONDAY = 1,
			TUESDAY = 2,
			WEDNESDAY = 3,
			THURSDAY = 4, 
			FRIDAY = 5;
	public int day;
	
	// these two could be replaced by a range of some sort (another object)
	public int startTime;
	public int endTime;
	
	/**
	 * Default constructor: give useless values to the {@link #day},
	 * {@link #startTime} and {@link #endTime} fields.
	 */
	public Time()
	{
		
	}
	
	/**
	 * Construct a time object with the given day, start time and end time.
	 * {@code day} must be one of {@link #MONDAY}, {@link #TUESDAY}, and so on,
	 * or else behavior is undefined.
	 * @param day the day of the week. Must be one of {@link #MONDAY}, {@link #TUESDAY}, and so on
	 * @param startTime the time of the day the class starts
	 * @param endTime the time of the day the class ends
	 */
	public Time(int day, int startTime, int endTime)
	{
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
