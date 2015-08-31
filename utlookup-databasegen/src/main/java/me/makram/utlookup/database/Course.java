package org.lag.utlookup.back;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that encapsulates all data related to a course at U of T. This includes
 * metadata (like course code and description) as well as concrete data (like meeting sections,
 * when it is taking place, and so on).
 * @author Makram
 *
 */
public class Course {
	
	/**
	 * Must be supplied: the course code. This is guaranteed to be
	 * unique by the university.
	 */
	public final String courseCode;
	
	/**
	 * Must be supplied: the course name.
	 */
	public final String courseName;
	
	/**
	 * Course section code. Could be H or Y (or possibly others, but usually a letter).
	 */
	public String sectionCode;
	
	/**
	 * Optionally supplied: course description.
	 */
	public String courseDescription;
	
	/**
	 * Optionally supplied: course prerequisites.
	 */
	public String prerequisites;
	
	/**
	 * Optionally supplied: course corequisites.
	 */
	public String corequisites;
	
	/**
	 * Optionally supplied: course recommended preparation.
	 */
	public String recommendedPreparation;
	
	/**
	 * Optionally supplied: course exclusions.
	 */
	public String exclusions;
	
	/**
	 * Optionally supplied: distribution requirement of the course.
	 */
	public String distributionRequirement;
	
	/**
	 * Optionally supplied: breadth requirement of the course.
	 */
	public String breadthRequirement;
	
	// The following information is from the timetable section of the application
	private List<MeetingSection> meetingSections;
	private Map<String, String> meetingSectionToTimeMap;
	
	public Course(String courseCode, String courseName)
	{
		// don't accept null values
		assert(courseCode != null && courseName != null);
		
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.meetingSections = new ArrayList<>();
		this.meetingSectionToTimeMap = new HashMap<>();
	}
	
	public void insertMeetingSection(String meetingSection, String time) {
		meetingSectionToTimeMap.put(meetingSection, time);
		MeetingSection section = new MeetingSection(meetingSection);
		section.time.add(time);
		meetingSections.add(section);
	}
	
	public String getTimeForMeetingSection(String meetingSection) {
		return meetingSectionToTimeMap.get(meetingSection);
	}
	
	public void insertMeetingSection(MeetingSection section) {
		meetingSections.add(section);
	}
	
	@Override
	public String toString() {
		return "(" + courseCode + "," + courseName + ")";
	}
	
	public List<MeetingSection> getMeetingSections() {
		return meetingSections;
	}
}
