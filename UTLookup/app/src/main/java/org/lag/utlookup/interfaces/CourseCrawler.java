/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lag.utlookup.interfaces;

import org.lag.utlookup.back.Course;

import java.util.List;

/**
 * An interface that attempts to unite all the course crawlers for different
 * campuses so to be as general as possible. 
 */
public interface CourseCrawler {
    /**
     * Get a list of links to the courses offered by each department from the 
     * campus's timetable. This requires scraping the timetable, not the calendar.
     * @return a list of links to the courses offered by each department from the 
     * campus's timetable
     */
    public List<String> getDepartmentTimetableLinks();
    
    /**
     * Get a list of all courses offered at the campus. Typically this is done
     * by scraping the calendar.
     * @return a list of all courses offered at the campus
     */
    public List<Course> getCourseList();
    
    /**
     * Get a list of all departments in the university. Note that some campuses
     * have more than one faculty (like St. George has faculty of engineering,
     * arts and science, and architecture), so that will have to be taken
     * into account eventually.
     * @return a list of all departments in the university
     */
    public List<String> getDepartmentList();
    
    /**
     * Get a list of URLs to the each department's listing of courses. This is
     * done by scraping the calendar.
     * @return a list of URLs to the each department's listing of courses
     */
    public List<String> getCourseUrls();
}
