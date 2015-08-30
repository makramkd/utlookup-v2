package org.lag.utlookup.back;

/**
 * The difference between this crawler and StGeorge crawler is that
 * this one will use Volley to do asynchronous HTTP requests and will use
 * synchronized java methods to
 * Created by admin on 8/30/15.
 */
public class AsyncStGeorgeCrawler {
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


}
