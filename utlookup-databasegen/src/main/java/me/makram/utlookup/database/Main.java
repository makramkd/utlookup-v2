/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Main {

    public static void main(String[] args) {
//        DatabaseHelper.DATABASE_VERSION = 3;
//        DatabaseHelper instance = DatabaseHelper.instance();
//        boolean init = instance.isInitialized();
//        System.out.println("Database is initialized ? " + init);
//
//        StGeorgeCrawler crawler = new StGeorgeCrawler(
//                StGeorgeCrawler.CALENDAR_URL);
//
//        List<Department> departmentList = crawler.getDepartmentList();
//        int[] depsInserted = instance.insertDepartments(departmentList);
//        System.out.println("Inserted " + depsInserted.length +
//                " department tuples into the database");
//
//        List<Instructor> instructorList = crawler.getInstructorList();
//        int[] insertions = instance.insertInstructors(instructorList);
//        System.out.println("Inserted " + insertions.length +
//                " instructor tuples to the " +
//                "database");
//
//        List<DepartmentOffering> depOfferingList =
//                crawler.getDepartmentOfferings();
//        int[] depOffersInserted =
//                instance.insertDepartmentOfferings(depOfferingList);
//        System.out.println("Inserted " + depOffersInserted.length +
//                " department offer tuples into the database.");
//
//        List<Course> courseList =
//                crawler.getCourseList();
//        int[] coursesInserted =
//                instance.insertCourses(courseList);
//        System.out.println("Inserted " + coursesInserted.length +
//                " course tuples into the database.");

        AsyncStGeorgeCrawler crawler = new AsyncStGeorgeCrawler();
        String testUrl = "http://www.artsandscience.utoronto.ca/ofr/calendar/crs_abs.htm";

        try {
            Response response = crawler.getClient()
                    .newCall(crawler.getCourseUrlRequest())
                    .execute();
            String html = response.body().string();
            Document document = Jsoup.parse(html);
            final Elements links = document.getElementsByTag("a");
            // get stream of links to filter by link

            for (Element e : links) {
                if (e.attr("href").startsWith("crs")) {
                    crawler.addToCourseUrls(StGeorgeCrawler.CALENDAR_URL
                            + e.attr("href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Request> requests = new ArrayList<>();
        for (String url : crawler.courseUrls) {
            requests.add(crawler.getInstructorListForDepartmentRequest(url));
        }

        crawler.requestCount.set(requests.size());

        for (Request request : requests) {
            crawler.getClient()
                    .newCall(request)
                    .enqueue(crawler.instructorsPerDepartmentCallback);
        }

        while (crawler.requestCount.get() != 0) {
            continue;
        }
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
