package org.lag.utlookup.back;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SaugaCrawler extends Crawler implements CourseCrawler {

    /**
     * The link to the calendar for the University of Toronto in Mississauga.
     * This will have to be scraped differently than the St. George and
     * Scarborough pages.
     */
    public static final String CALENDAR_URL = "http://student.utm.utoronto.ca/calendar/depart_list.pl";

    /**
     * This is the calendar URL without the {@code depart_list.pl} at the end.
     * What is weird is that they are using perl in this page, unless I'm
     * reading something wrong.
     */
    private static final String CALENDAR_URL_WITHOUT_DEP = "http://student.utm.utoronto.ca/calendar/";
    
    /**
     * The prefix to the hrefs that contain course information.
     */
    private static final String HREF_PREFIX = "newdep_detail";
    
    private boolean courseDatabaseInitialized;
    private CourseDataStore courseDatabase;
    
    /**
     * Create a Sauga crawler with the given URL. Must be {@link #CALENDAR_URL}.
     *
     * @param url the URL to crawl. Must be {@link #CALENDAR_URL}.
     */
    public SaugaCrawler(String url) {
        super(url);
        assert(url.equals(SaugaCrawler.CALENDAR_URL));
        courseDatabase = new CourseDataStore();
        courseDatabaseInitialized = false;
    }

    /**
     * Get the department timetable links for the Mississauga campus of UofT.
     * This function currently returns null because there is no way to scrape
     * the mississauga timetable using an HTML parser.
     * @return {@code null} always, because we cannot scrape Mississauga's 
     * timetable data.
     */
    @Override
    public List<String> getDepartmentTimetableLinks() {
        assert(false); // for brevity
        return null;
    }

    /**
     * Get a list of all courses offered at UTM.
     * This function returns course objects because they are more useful than
     * just strings.
     * @return a list of all courses offered at UTM.
     */
    @Override
    public List<Course> getCourseList() {
        /*
         * The UTM calendar is organized in table form for each department. The table consists of two columns:
         * the course ID column and the course name column. This makes things very easy to parse.
         */

        List<Course> courses = new ArrayList<>();
        List<String> courseUrls = getCourseUrls();

        for (String url : courseUrls) {
            courses.addAll(getCourseListForDepartment(url));
        }

        if (!courseDatabaseInitialized) {
            courseDatabase.addAll(courses);
        courseDatabaseInitialized = false;
        }
        
        return courses;
    }

    /**
     * Get a list of courses offered by the department at the given url.
     * This is a helper function that is not meant to be used on its own. See
     * {@link #getCourseList()}.
     * @param url the URL to the department we wish to extract course data from.
     * @return a list of courses offered by the department at the given url
     */
    private List<Course> getCourseListForDepartment(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(0).get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // list of courses to return
        List<Course> courses = new ArrayList<>();

        final Elements rows = doc.getElementsByTag("tr");
        final int size = rows.size();

        for (int i = 0; i < size; ++i) {
            Element row = rows.get(i);
            if (row.children().size() != 2) {
                break; // there is more than one table in the page
            }

            final String courseCode = row.child(0).text();
            final String courseName = row.child(1).text();

            courses.add(new Course(courseCode, courseName));
        }

        return courses;
    }

    /**
     * Get a list of all departments at UTM offering courses.
     * @return a list of all departments at UTM offering courses.
     */
    @Override
    public List<String> getDepartmentList() {
        assert (getUrl().equals(SaugaCrawler.CALENDAR_URL));

        final Elements elements = document.getElementsByTag("a");
        List<String> departmentList = new ArrayList<>();

        // filter out those elements with the known string that they start with
        for (Element aTag : elements) {
            if (aTag.attr("href").startsWith(HREF_PREFIX)) {
                departmentList.add(aTag.text());
            }
        }

        return departmentList;
    }

    /**
     * Get a list of URLs to all courses offered by UTM.
     * @return a list of URLs to all courses offered by UTM.
     */
    @Override
    public List<String> getCourseUrls() {
        assert (getUrl().equals(SaugaCrawler.CALENDAR_URL));

        final Elements elements = document.getElementsByTag("a");
        List<String> courseUrls = new ArrayList<>();

		// filter out by what the href is starting with and add that 
        // to the upper url
        for (Element aTag : elements) {
            if (aTag.attr("href").startsWith(HREF_PREFIX)) {
                courseUrls.add(CALENDAR_URL_WITHOUT_DEP + aTag.attr("href"));
            }
        }

        return courseUrls;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void crawl() {

    }

}
