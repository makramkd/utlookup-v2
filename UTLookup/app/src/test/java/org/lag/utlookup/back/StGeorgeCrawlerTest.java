package org.lag.utlookup.back;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by admin on 8/29/15.
 */
public class StGeorgeCrawlerTest {

    public static final String STARS = "****************************";
    StGeorgeCrawler crawler;

    @Before
    public void setUp() throws Exception {
        crawler = new StGeorgeCrawler(StGeorgeCrawler.CALENDAR_URL);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCourseUrls() throws Exception {
        Set<String> courseUrls = crawler.getCourseUrls();
//        System.out.println(STARS);
//        System.out.println("@Test(testGetCourseUrls())");
//        for (String string: courseUrls) {
//            System.out.println(string);
//        }
//        System.out.println(STARS);
    }

    @Test
    public void testGetDepartmentList() throws Exception {
        List<Department> departmentList = crawler.getDepartmentList();

//        System.out.println(STARS);
//        System.out.println("@Test(testGetDepartmentList())");
//        for (Department d : departmentList) {
//            System.out.println("(" + d.code + ", " + d.name + ")");
//        }
//        System.out.println(STARS);
    }

    @Test
    public void testGetInstructorList() throws Exception {
        List<Instructor> instructorList = crawler.getInstructorList();

//        System.out.println(STARS);
//        System.out.println("@Test(testGetInstructorList())");
//        for (Instructor i : instructorList) {
//            System.out.println("(" + i.name + "," + i.department + ")");
//        }
//        System.out.println(STARS);
    }

    @Test
    public void testGetCourseList() throws Exception {
        List<Course> courses = crawler.getCourseList();

//        System.out.println(STARS);
//        System.out.println("@Test(testGetCourseList())");
//        for (Course c : courses) {
//            System.out.println("(" + c.courseCode + "," + c.courseName + ")");
//        }
//        System.out.println(STARS);
    }

    @Test
    public void testGetDepartmentTimetableLinks() throws Exception {
        crawler.setUrl(StGeorgeCrawler.TIMETABLE_URL_FW);
        List<String> departmentTimetableLinks = crawler.getDepartmentTimetableLinks();

//        System.out.println(STARS);
//        System.out.println("@Test(testGetDepartmentTimetableLinks())");
//        for (String departmentLink : departmentTimetableLinks) {
//            System.out.println(departmentLink);
//        }
//        System.out.println(STARS);
    }

    @Test
    public void testPopulateDatabaseFromLink() throws Exception {

    }

    @Test
    public void testGetCourseDataStore() throws Exception {
        return;
    }

    @Test
    public void testGetDepartmentOfferings() throws Exception {
        List<DepartmentOffering> departmentOfferings = crawler.getDepartmentOfferings();

//        System.out.println(STARS);
//        System.out.println("@Test(testGetDepartmentOfferings())");
//        for (DepartmentOffering departmentOffering : departmentOfferings) {
//            System.out.println("(" + departmentOffering.deptCode + ", " +
//            departmentOffering.courseCode + ")");
//        }
//        System.out.println(STARS);
    }
}