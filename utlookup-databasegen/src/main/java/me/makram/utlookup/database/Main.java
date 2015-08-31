/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author admin
 */
public class Main {

    public static void main(String[] args) {
        DatabaseHelper.DATABASE_VERSION = 2;
        DatabaseHelper instance = DatabaseHelper.instance();
        boolean init = instance.isInitialized();
        System.out.println("Database is initialized ? " + init);

        StGeorgeCrawler crawler = new StGeorgeCrawler(
                StGeorgeCrawler.CALENDAR_URL);
        
        List<Department> departmentList = crawler.getDepartmentList();
        int[] depsInserted = instance.insertDepartments(departmentList);
        System.out.println("Inserted " + depsInserted.length + 
                " department tuples into the database");
        
        List<Instructor> instructorList = crawler.getInstructorList();
        int[] insertions = instance.insertInstructors(instructorList);
        System.out.println("Inserted " + insertions.length + 
                " instructor tuples to the " +
                "database");
        
        List<DepartmentOffering> depOfferingList = 
                crawler.getDepartmentOfferings();
        int[] depOffersInserted = 
                instance.insertDepartmentOfferings(depOfferingList);
        System.out.println("Inserted " + depOffersInserted.length + 
                " department offer tuples into the database.");
        
        List<Course> courseList = 
                crawler.getCourseList();
        int[] coursesInserted = 
                instance.insertCourses(courseList);
        System.out.println("Inserted " + coursesInserted.length + 
                " course tuples into the database.");
        
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
