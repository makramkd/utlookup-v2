package org.lag.utlookup.back;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lag.utlookup.interfaces.CourseCrawler;
import org.lag.utlookup.interfaces.Crawler;

public class StGeorgeCrawler extends Crawler implements CourseCrawler {
    
    /**
    * The link to the calendar of the next valid fall/winter session. This link holds the metadata to all the courses,
    * such as course code, course name, prereqs, coreqs, and so on, also separated by department.
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

    private CourseDataStore courseDatabase;

    private boolean courseDatabaseFilled;

    public StGeorgeCrawler(String url) {
        super(url);

        courseDatabase = new CourseDataStore();
        courseDatabaseFilled = false;
    }

    /**
     * Get a list of all course links located at the given url, which is assumed
     * to be {@link Crawler#CALENDAR_URL}.
     *
     * @return a list of all course links located at the given url
     */
    private List<Element> getCourseLinks() {
        final Elements links = document.getElementsByTag("a");
        // get stream of links to filter by link
        List<Element> linksToCourses = new ArrayList<>();
        for (Element e : links) {
            if (e.attr("href").startsWith("crs")) {
                linksToCourses.add(e);
            }
        }

        return linksToCourses;
    }

    /**
     * Get a list of all course url literals located at the given url, which is
     * assumed to be {@link Crawler#CALENDAR_URL}.
     *
     * @return a list of all course url literals located at the given url
     */
    @Override
    public Set<String> getCourseUrls() {
        assert (getUrl().equals(StGeorgeCrawler.CALENDAR_URL));
        List<Element> links = getCourseLinks();
        Set<String> urls = new TreeSet<>();
        for (Element e : links) {
            urls.add(getUrl() + e.attr("href"));
        }

        return urls;
    }

    /**
     * Get a list of all departments offering courses, based off of the given
     * url, which is assumed to be {@link Crawler#CALENDAR_URL}.
     *
     * @return
     */
    @Override
    public List<Department> getDepartmentList() {
        assert (getUrl().equals(StGeorgeCrawler.CALENDAR_URL));
        final Elements links = document.getElementsByTag("a");
        // get stream of links to filter by link
        List<Element> linksToCourses = new ArrayList<>();
        for (Element e : links) {
            if (e.attr("href").startsWith("crs")) {
                linksToCourses.add(e);
            }
        }

        List<Department> departments = new ArrayList<>();

        for (Element e : linksToCourses) {
            departments.add(new Department(e.text(),
                    e.attr("href").substring(4, 7).toUpperCase()));
        }

        return departments;
    }

    public List<Instructor> getInstructorList() {
        assert (getUrl().equals(StGeorgeCrawler.CALENDAR_URL));

        // we need the urls to the course pages
        Set<String> deptLinks = getCourseUrls();
        List<Instructor> instructors = new ArrayList<>();
        for (String deptLink : deptLinks) {
            List<Instructor> itors = getInstructorListForDepartment(deptLink);
            if (itors == null) {
                continue;
            }
            instructors.addAll(itors);
        }

        return instructors;
    }

    @Nullable
    private List<Instructor> getInstructorListForDepartment(String departmentDirectLink) {
        // we are assuming the given link is from the
        // calendar.

        List<Instructor> instructors = new ArrayList<>();

        Document doc = null;
        try {
            // set an infinite timeout
            doc = Jsoup.connect(departmentDirectLink).timeout(2000).get();
        } catch (IOException e) {
            doc = null;
        }

        if (doc == null) {
            return null;
        }

        // the profs are part of the unmodified indented class
        Elements profObjects = doc.getElementsByClass(UNMODIFIED_INDENTED);
        for (Element profObject : profObjects) {
            instructors.add(new Instructor(profObject.text(),
                    departmentDirectLink.substring(departmentDirectLink.length() - 7,
                            departmentDirectLink.length() - 4)));
        }

        return instructors;
    }

    /**
     * Get a list of all the courses of the university as a list of
     * {@link Course}. Requires the given URL to be
     * {@link Crawler#CALENDAR_URL}.
     *
     * @return a list of all the courses of the university
     */
    @Override
    public List<Course> getCourseList() {
        List<Course> courses = new ArrayList<>();
        Set<String> courseUrls = getCourseUrls();

        for (String url : courseUrls) {
            courses.addAll(getCourseListForDepartment(url));
        }

//        if (!courseDatabaseFilled) {
//            courseDatabase.addAll(courses);
//            courseDatabaseFilled = true;
//        }
        
        return courses;
    }

    /**
     * Get a list of all the courses of a department in the university. Requires
     * the given url to be {@link Crawler#CALENDAR_URL}. We also put the course
     * object in the course database for further access in other functions.
     *
     * @param departmentDirectLink a direct link to the department's course page
     * @return a list of all the courses of a department in the university
     */
    private List<Course> getCourseListForDepartment(String departmentDirectLink) {
        // we are assuming the given link is from the calendar
        List<Course> courses = new ArrayList<>();
        // get the web page and parse it
        Document doc = null;
        try {
            // set an infinite timeout
            doc = Jsoup.connect(departmentDirectLink).timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements strongObjects = doc.getElementsByClass("strong");

        // java 7 solution
        List<Element> courseObjects = new ArrayList<>();
        for (Element e : strongObjects) {
            if (e.text().length() >= 8
                    && Pattern.matches(COURSE_REGEX, e.text().substring(0, 8))) {
                courseObjects.add(e);
            }
        }

        for (Element e : courseObjects) {
            String text = e.text();
            String code = text.substring(0, 8);
            String name = text.substring(12, text.length());
            Element descriptionParagraph = e.nextElementSibling();
            String descriptionText = "";
            if (descriptionParagraph.tagName().equals("p")) {
                descriptionText = descriptionParagraph.text();
            }
            Course course = new Course(code, name);
            course.courseDescription = descriptionText;
            courses.add(course);
        }

        return courses;
    }

    /**
     * Get a list of department timetable links. This method requires that the
     * Crawler address is set to {@link StGeorgeCrawler#TIMETABLE_URL_FW}.
     *
     * @return list of department timetable links
     */
    @Override
    public List<String> getDepartmentTimetableLinks() {
        assert (getUrl().equals(StGeorgeCrawler.TIMETABLE_URL_FW));
        List<String> links = new ArrayList<>();

        // find the links in the page: these are direct descendants of <li> elements
        Elements departmentLinks = document.select("li > a");
        for (Element e : departmentLinks) {
            links.add(StGeorgeCrawler.TIMETABLE_URL_FW + e.attr("href"));
        }

        return links;
    }

    /**
     * Populate the internal course database with the information in the given
     * link. The given link should have the format
     * {@code http://www.artsandscience.utoronto.ca/ofr/timetable/winter/coursecode.html}.
     * We also assume that the function {@link #getCourseList()} has been called
     * in order to populate the course database with the {@link Course} objects.
     *
     * @param linkToTimetable given link that should have the format
     * {@code http://www.artsandscience.utoronto.ca/ofr/timetable/winter/coursecode.html}
     * where coursecode is different for each course.
     */
    public void populateDatabaseFromLink(String linkToTimetable) {
        // sanity checks
        // we should be crawling the timetable initially
        assert (getUrl().equals(StGeorgeCrawler.TIMETABLE_URL_FW));
        // precondition to this function
        assert (linkToTimetable.startsWith(StGeorgeCrawler.TIMETABLE_URL_FW));

        Document doc = null;
        try {
            doc = Jsoup.connect(linkToTimetable).timeout(0).get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // get the table rows
        final Elements rows = doc.getElementsByTag("tr");

		// the table has 10 columns, so the indices of the children of the tr will be from 0 to 9
        // except for the first row, because it is only the title of the table. For this reason
        // we start the loop at index 2 of the rows list.
        final int size = rows.size();

		// the table contains rows that have no information in one (or more) of the following cols:
        // Course, section code, title, location, instructor, and so on
        // however, all we are concerned with is the course column. 
        Element majorRow = null;
        String currentCourseCode = "";
        for (int i = 2; i < size; ++i) {
            Element currentRow = rows.get(i);
            if (currentRow.children().size() != 10) {
                continue;
            }
            if (!currentRow.child(0).html().equals(EMPTY_CELL)) {
                majorRow = currentRow;
                currentCourseCode = majorRow.child(COURSE_CODE).text();
            }
			// index 0: course code, 1: section code, 2: title, 3: meeting section, 4: wait list (yes or no)
            // 5: time, 6: location, 7: instructor, 8: enrolment indicator, 9: enrolment controls (useless)

            final String courseCode = currentRow.child(COURSE_CODE).text(); // could be equal to EMPTY_CELL
            final String sectionCode = currentRow.child(SECTION_CODE).text(); // could be equal to EMPTY_CELL
            final String title = currentRow.child(TITLE).text(); // could be equal to EMPTY_CELL (not reliable to be the name of the course)
            final String meetingSection = currentRow.child(MEETING_SECTION).text(); // never empty
            final String waitlist = currentRow.child(WAITLIST).text(); // never empty
            final String time = currentRow.child(TIME).text(); // never empty
            final String loc = currentRow.child(LOCATION).text(); // could be equal to EMPTY_CELL
            final String instructor = currentRow.child(INSTRUCTOR).text(); // could be equal to EMPTY_CELL
            final String enindicator = currentRow.child(ENROLMENT_INDICATOR).text(); // could be equal to EMPTY_CELL (not sure if this property is inherited)
            try {
                final String enctrl = currentRow.child(ENROLMENT_CTRL).text(); // could be equal to EMPTY_CELL (not sure if this property is inherited)
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Size of rows: " + currentRow.children().size());
                System.err.println("At the index: " + ENROLMENT_CTRL);
            }

            // look up course in db
            Course c = courseDatabase.get(currentCourseCode);
            assert (c != null);

            MeetingSection section = new MeetingSection(meetingSection);
            section.hasWaitingList = waitlist;
            section.location = loc;
            section.time.add(time);
            section.instructor = instructor;
            section.enrolmentIndicator = enindicator;

            c.insertMeetingSection(section);
        }
    }

    public CourseDataStore getCourseDataStore() {
        return this.courseDatabase;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void crawl() {
        
    }
}
