package me.makram.utlookup.database;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to encapsulate what a meeting section is supposed to be (for UofT).
 * See internal javadoc comments for more details.
 * @author Makram
 *
 */
public class MeetingSection {
	/**
	 * Each meeting section has a unique section code for each course (i.e
	 * the uniqueness of the meeting section does not hold across courses, even
	 * in the same department).
	 */
	public final String sectionCode;
	
	/**
	 * Indicates whether this meeting section has a waitlist or not. This is on a
	 * meeting section basis rather than a course basis, as some meeting sections
	 * are special enrollment.
	 */
	public String hasWaitingList;
	
	/**
	 * Where the current meeting section is taking place. While this can change 
	 * over time during a semester, it is not our job to keep track of this.
	 */
	public String location;
	
	/**
	 * The instructor that is teaching this meeting section. Similarly, this can
	 * change over the course of the semester, but we are not keeping track.
	 */
	public String instructor;
	
	/**
	 * Some sections have special enrollment indicators (like Rotman courses 
	 * for example) that indicate what kind of student can enroll.
	 */
	public String enrolmentIndicator;
	
	/**
	 * The times at which this meeting is taking place. Some meeting sections meet
	 * on more than one day, so keeping it as a list accounts for that.
	 */
	public List<String> time; // could replace Time with String because students already know how to read times from the timetable
	
	public MeetingSection(String sectionCode) {
		this.sectionCode = sectionCode;
		this.time = new ArrayList<>();
	}
	
	public String toString() {
		return "(" + sectionCode + ")";
	}
}
