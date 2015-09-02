/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author admin
 */
public class AsyncStGeorgeCrawler {

    /**
     * The link to the calendar of the next valid fall/winter session. This link
     * holds the metadata to all the courses, such as course code, course name,
     * prereqs, coreqs, and so on, also separated by department.
     */
    public static final String CALENDAR_URL = "http://www.artsandscience.utoronto.ca/ofr/calendar/";

    /**
     * The link to the timetable of the next valid fall/winter session. This
     * link hold all the links to the timetables of the courses, separated by
     * department. It contains things such as times/days the courses are offered
     * and who is teaching the course.
     */
    public static final String TIMETABLE_URL_FW = "http://www.artsandscience.utoronto.ca/ofr/timetable/winter/";

    public static final String COURSE_REGEX = "[a-zA-Z]{3}[0-9]{3}[H|Y][1|5]";

    private static final int COURSE_CODE = 0,
            SECTION_CODE = 1,
            TITLE = 2,
            MEETING_SECTION = 3,
            WAITLIST = 4,
            TIME = 5,
            LOCATION = 6,
            INSTRUCTOR = 7,
            ENROLMENT_INDICATOR = 8,
            ENROLMENT_CTRL = 9;
    private static final String EMPTY_CELL = "<font size=\"-1\">&nbsp;</font>";
    public static final String UNMODIFIED_INDENTED = "unmodifiedindented";

    public List<Course> courseList;
    public List<Department> departmentList;
    public List<DepartmentOffering> departmentOfferingList;
    public List<DeptHead> deptHeadList;
    public List<Instructor> instructorList;
    public List<MeetingSection> meetingSectionList;
    public Set<String> courseUrls;

    public AtomicInteger requestCount;

    private final OkHttpClient client = new OkHttpClient();

    public final Callback courseUrlCallback = new Callback() {

        @Override
        public void onFailure(Request rqst, IOException ioe) {
            Logger.getLogger("AsyncStGeorgeCrawler").log(Level.SEVERE,
                    "Course URL request failed");
        }

        @Override
        public void onResponse(Response rspns) throws IOException {
            String html = rspns.body().string();
            Document document = Jsoup.parse(html);
            final Elements links = document.getElementsByTag("a");
            // get stream of links to filter by link

            for (Element e : links) {
                if (e.attr("href").startsWith("crs")) {
                    addToCourseUrls(StGeorgeCrawler.CALENDAR_URL
                            + e.attr("href"));
                }
            }
        }

    };

    public final Callback instructorsPerDepartmentCallback = new Callback() {

        @Override
        public void onFailure(Request request, IOException ioe) {
            Logger.getLogger("AsyncStGeorgeCrawler").log(Level.SEVERE,
                    "Instructor request failed");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Document document = Jsoup.parse(response.body().string());
            final Elements profObjects = document.getElementsByClass(UNMODIFIED_INDENTED);

            List<Instructor> instructors = new ArrayList<>();
            String theUrl = response.request().urlString();
            for (Element profObject : profObjects) {
                instructors.add(new Instructor(profObject.text(),
                        theUrl.substring(theUrl.length() - 7,
                                theUrl.length() - 4).toUpperCase()));
            }
            addToInstructorList(instructors);

            requestCount.getAndDecrement();
        }

    };

    public AsyncStGeorgeCrawler() {
        courseList = new ArrayList<>();
        departmentList = new ArrayList<>();
        departmentOfferingList = new ArrayList<>();
        deptHeadList = new ArrayList<>();
        instructorList = new ArrayList<>();
        meetingSectionList = new ArrayList<>();
        courseUrls = new TreeSet<>();
        requestCount = new AtomicInteger();
    }

    public OkHttpClient getClient() {
        return client;
    }

    private synchronized void addToCourseList(List<Course> courses) {
        courseList.addAll(courses);
    }

    private synchronized void addToDepartmentList(List<Department> departments) {
        departmentList.addAll(departments);
    }

    private synchronized void addToDepartmentOfferingList(List<DepartmentOffering> departmentOfferings) {
        departmentOfferingList.addAll(departmentOfferings);
    }

    private synchronized void addToDeptHeadList(List<DeptHead> deptHeads) {
        deptHeadList.addAll(deptHeads);
    }

    private synchronized void addToInstructorList(List<Instructor> instructors) {
        instructorList.addAll(instructors);
        System.out.println("Added " + instructors.size() + " instructors.");
    }

    private synchronized void addToInstructorListSingle(Instructor instructor) {
        instructorList.add(instructor);
    }

    private synchronized void addToMeetingSectionList(List<MeetingSection> meetingSections) {
        meetingSectionList.addAll(meetingSections);
    }

    protected void addToCourseUrls(String url) {
        courseUrls.add(url);
        System.out.println("Added url " + url + " to course urls.");
    }

    public Request getCourseUrlRequest() {
        Request request = new Request.Builder()
                .url(StGeorgeCrawler.CALENDAR_URL)
                .build();

        return request;
    }

    public Request getInstructorListForDepartmentRequest(String departmentDirectLink) {
        Request request = new Request.Builder()
                .url(departmentDirectLink)
                .addHeader("theUrl", departmentDirectLink)
                .build();

        return request;
    }
}
