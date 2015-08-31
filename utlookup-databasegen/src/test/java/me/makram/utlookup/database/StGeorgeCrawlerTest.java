/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author admin
 */
public class StGeorgeCrawlerTest {
    
    StGeorgeCrawler crawler;
    
    public StGeorgeCrawlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        crawler = new StGeorgeCrawler(StGeorgeCrawler.CALENDAR_URL);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCourseUrls method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetCourseUrls() {
        System.out.println("getCourseUrls");
        Set<String> result = crawler.getCourseUrls();
        for (String url : result) {
            System.out.println(url);
        }
    }

    /**
     * Test of getDepartmentList method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetDepartmentList() {
        System.out.println("getDepartmentList");
        List<Department> result = crawler.getDepartmentList();
        for (Department dept : result) {
            System.out.println("(" + dept.code + ", " + dept.name + ")");
        }
    }

    /**
     * Test of getInstructorList method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetInstructorList() {
        System.out.println("getInstructorList");
        List<Instructor> result = crawler.getInstructorList();
        for (Instructor i : result) {
            System.out.println("(" + i.name + ", " + i.department + ")");
        }
    }

    /**
     * Test of getCourseList method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetCourseList() {
        System.out.println("getCourseList");
        List<Course> result = crawler.getCourseList();
        for (Course c : result) {
            System.out.println("(" + c.courseCode + "," + c.courseName + ")");
        }
    }

    /**
     * Test of getDepartmentTimetableLinks method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetDepartmentTimetableLinks() {
        System.out.println("getDepartmentTimetableLinks");
        List<String> result = crawler.getDepartmentTimetableLinks();
        for (String link : result) {
            System.out.println(link);
        }
    }

    /**
     * Test of populateDatabaseFromLink method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testPopulateDatabaseFromLink() {
    }

    /**
     * Test of getDepartmentOfferings method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetDepartmentOfferings() {
        System.out.println("getDepartmentOfferings");
        List<DepartmentOffering> result = crawler.getDepartmentOfferings();
        for (DepartmentOffering dof : result) {
            System.out.println("(" + dof.deptCode + ", " + dof.courseCode + ")");
        }
    }

    /**
     * Test of getDepartmentOfferingsForDepartment method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetDepartmentOfferingsForDepartment() {
    }

    /**
     * Test of getMeetingSections method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetMeetingSections() {
        System.out.println("getMeetingSections");
        List<MeetingSection> result = crawler.getMeetingSections();
        for (MeetingSection ms : result) {
            System.out.println("(" + ms.sectionCode + ", " 
                    + ms.instructor + ")");
        }
    }

    /**
     * Test of getMeetingSectionsForDepartment method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetMeetingSectionsForDepartment() {
    }

    /**
     * Test of getCourseDataStore method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testGetCourseDataStore() {
    }

    /**
     * Test of initialize method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testInitialize() {
    }

    /**
     * Test of crawl method, of class StGeorgeCrawler.
     */
    @org.junit.Test
    public void testCrawl() {
    }
    
}
