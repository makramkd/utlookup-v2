package org.lag.courselookup.back;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lag.courselookup.interfaces.CourseCrawler;
import org.lag.courselookup.interfaces.Crawler;

public class ScarCrawler extends Crawler implements CourseCrawler {

    /**
     * The prefix that is used in the href of links to courses on the calendar
     * page.
     */
    private static final String HREF_PREFIX = "ListofCourses";

    // The base URL that precedes every departments course listing
    private static final String BASEURL = 
            "http://www.utsc.utoronto.ca/~registrar/calendars/calendar/";
    /**
     * The link to the calendar for the University of Toronto in Scarborough.
     */
    public static final String CALENDAR_URL = "http://www.utsc.utoronto.ca/~registrar/calendars/calendar/Programs.html";

    /**
     * This course regex is unique to scarborough, so we can't use the St.
     * George and UTM regexes.
     */
    public static final String COURSE_REGEX = "[a-zA-Z]{4}[0-9]{2}[H|Y][3]";
    
    /**
     * Construct a ScarCrawler object with the given url. The url must be
     * {@link #CALENDAR_URL}.
     * @param url the url to crawl. It must be {@link #CALENDAR_URL}.
     */
    public ScarCrawler(String url) {
        super(url);
        assert(url.equals(ScarCrawler.CALENDAR_URL));
    }

    /**
     * Try to get the timetable links for the UTSC departments. This is
     * not possible because UTSC has their own "course finder" which is a
     * shame.
     * @return 
     */
    @Override
    public List<String> getDepartmentTimetableLinks() {
        return null;
    }

    /**
     * Get a list of courses offered at UTSC. We return a list of
     * {@link Course} objects because they are more useful than Strings.
     * @return a list of courses offered at UTSC
     */
    @Override
    public List<Course> getCourseList() {
        List<Course> courses = new ArrayList<>();
        
        for (String dept : getDepartmentListStrings()) {
            String courseUrl = BASEURL + dept + ".html";
            courses.addAll(getCourseListForDepartment(courseUrl));
        }
        return courses;
    }

    @Override
    public List<Department> getDepartmentList() {
        return null;
    }

    private Collection<Course> getCourseListForDepartment(String url) {
        List<Course> courses = new ArrayList<>();
        
        url = url.replaceAll(" ", "_");
        
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            return courses;
        }
        
        final Elements spans = doc.getElementsByTag("span");
        
        // java 7 solution
        for (Element e : spans) {

            if (e.childNodeSize() < 1 || e.textNodes().size() < 2)
                continue;
            
            String code;
            String name;
                                        
            // I'm not too sure why this algorithm is so complicated, but it works
            // maybe we can clean it up...
            
            // 0 should be code, 1 should be name
            String potentialCourseCode = e.child(0).text();
            if (potentialCourseCode.matches(COURSE_REGEX)) 
                code = potentialCourseCode;
            else
                continue;
                
            // This must be the text if we got the course code already
            name = e.ownText();
            
            // Strip all the &nbsp - note that trim() does not work
            name = name.replaceAll("\u00A0", "");
                
            Course course = new Course(code, name);
            courses.add(course);
        }      
        
        return courses;
    }
    
    /**
     * Get a list of all departments offering courses at UTSC.
     * @return a list of all departments offering courses at UTSC.
     */
    public List<String> getDepartmentListStrings() {
        assert (getUrl().equals(ScarCrawler.CALENDAR_URL));

        final Elements elements = document.getElementsByTag("a");
        List<String> departmentList = new ArrayList<>();

        // filter out the department links: they start with ListOfCourses.html?
        for (Element link : elements) {
            if (link.attr("href").startsWith(HREF_PREFIX)) {
                departmentList.add(link.text());
            }
        }

        return departmentList;
    }

    /**
     * Get a list of the URLs to each department's course listings at UTSC.
     * @return a list of the URLs to each department's course listings at UTSC.
     */
    @Override
    public Set<String> getCourseUrls() {
        assert (getUrl().equals(ScarCrawler.CALENDAR_URL));

        // The UTSC course pages can be generated by adding the department name
        // to the end of a hardcoded URL.
       
        Set<String> courseUrls = new TreeSet<>();
        
        for (String dept : getDepartmentListStrings()) {
            courseUrls.add(BASEURL + dept + ".html");
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
