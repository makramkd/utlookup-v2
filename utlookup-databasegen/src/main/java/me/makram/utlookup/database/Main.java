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

            Response timetableResponse = crawler
                    .getClient()
                    .newCall(crawler.getDepartmentTimetableUrlsRequest())
                    .execute();
            Document document1 = Jsoup.parse(timetableResponse.body().string());
            // find the links in the page: these are direct descendants of <li> elements
            Elements departmentLinks = document1.select("li > a");
            for (Element e : departmentLinks) {
                if (e.attr("href").contains("daniels") || e.attr("href").contains("engineering")) {
                    continue;
                }
                crawler.timetableDepartmentUrls.add(StGeorgeCrawler.TIMETABLE_URL_FW + e.attr("href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Request> instructorRequests = new ArrayList<>();
        List<Request> courseRequests = new ArrayList<>();
        List<Request> offerRequests = new ArrayList<>();
        Request departmentRequest = crawler.getDepartmentsRequest();
        List<Request> meetingSectionRequests = new ArrayList<>();

        for (String url : crawler.courseUrls) {
            instructorRequests.add(crawler.getInstructorListForDepartmentRequest(url));
            courseRequests.add(crawler.getCourseListForDepartmentRequest(url));
            offerRequests.add(crawler.getDepartmentOfferingsForDepartmentRequest(url));
        }

        for (String timetableUrl : crawler.timetableDepartmentUrls) {
            meetingSectionRequests.add(crawler.getMeetingSectionRequest(timetableUrl));
        }

        crawler.requestCount.set(instructorRequests.size()
                + courseRequests.size() +
                offerRequests.size() + 1);

        for (Request request : instructorRequests) {
            crawler.getClient()
                    .newCall(request)
                    .enqueue(crawler.instructorsPerDepartmentCallback);
        }

        for (Request request : courseRequests) {
            crawler.getClient()
                    .newCall(request)
                    .enqueue(crawler.coursesPerDepartmentCallback);
        }

        for (Request request : offerRequests) {
            crawler.getClient()
                    .newCall(request)
                    .enqueue(crawler.offersPerDepartmentCallback);
        }

        for (Request request : meetingSectionRequests) {
            crawler.getClient()
                    .newCall(request)
                    .enqueue(crawler.meetingSectionsCallback);
        }
        crawler.getClient()
                .newCall(departmentRequest)
                .enqueue(crawler.departmentsCallback);

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
