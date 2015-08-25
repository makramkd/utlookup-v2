package org.lag.utlookup.back;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDataStore {
	Map<String, String> codeToNameMap;
	List<String> courseCodes;
	List<String> courseNames;
	List<Course> courses;
	Map<String, Course> codeToCourseMap;
	
	public CourseDataStore() {
		codeToNameMap = new HashMap<>(); // may switch to treemap
		codeToCourseMap = new HashMap<>(); // may switch to treemap
		courseCodes = new ArrayList<>();
		courseNames = new ArrayList<>();
		courses = new ArrayList<>();
	}
	
	public void add(String courseCode, String courseName) {
		codeToNameMap.put(courseCode, courseName);
		courseCodes.add(courseCode);
		courseNames.add(courseName);
		courses.add(new Course(courseCode, courseName));
	}
	
	public void add(Course course) {
		codeToNameMap.put(course.courseCode, course.courseName);
		courseCodes.add(course.courseCode);
		courseNames.add(course.courseName);
		courses.add(new Course(course.courseCode, course.courseName));
		codeToCourseMap.put(course.courseCode, new Course(course.courseCode, course.courseName));
	}
        
        public void addAll(Collection<Course> courses) {
            for (Course c : courses) 
            {
                codeToNameMap.put(c.courseCode, c.courseName);
                courseCodes.add(c.courseCode);
                courseNames.add(c.courseName);
                codeToCourseMap.put(c.courseCode, new Course(
                        c.courseCode, c.courseName));
                this.courses.add(new Course(c.courseCode, c.courseName));
            }
        }
	
	public Course get(String courseCode) {
		return codeToCourseMap.get(courseCode);
	}
}
