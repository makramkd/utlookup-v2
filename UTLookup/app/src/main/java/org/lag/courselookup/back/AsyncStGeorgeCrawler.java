package org.lag.courselookup.back;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lag.courselookup.ChooseCampusActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The difference between this crawler and StGeorge crawler is that
 * this one will use Volley to do asynchronous HTTP requests and will use
 * synchronized java methods to fill up java objects that were otherwise
 * done serially in StGeorgeCrawler.
 *
 * Since Volley needs an Android Context we have to use this class only in
 * the Android runtime (i.e this isn't possible in vanilla java).
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

    public List<Course> courseList;
    public List<Department> departmentList;
    public List<DepartmentOffering> departmentOfferingList;
    public List<DeptHead> deptHeadList;
    public List<Instructor> instructorList;
    public List<MeetingSection> meetingSectionList;
    public Set<String> courseUrls;

    public AtomicInteger requestCount;

    public ChooseCampusActivity context;

    public AsyncStGeorgeCrawler(ChooseCampusActivity context) {
        courseList = new ArrayList<>();
        departmentList = new ArrayList<>();
        departmentOfferingList = new ArrayList<>();
        deptHeadList = new ArrayList<>();
        instructorList = new ArrayList<>();
        meetingSectionList = new ArrayList<>();
        courseUrls = new TreeSet<>();
        requestCount = new AtomicInteger();
        this.context = context;
    }

    private synchronized void addToCourseList(List<Course> courses) {
        courseList.addAll(courses);
        Log.d("ASTC", "added " + courses.size() + " elements to courseList");
    }

    private synchronized void addToDepartmentList(List<Department> departments) {
        departmentList.addAll(departments);
        Log.d("ASTC", "added " + departments.size() + " elements to departmentList");
    }

    private synchronized void addToDepartmentOfferingList(List<DepartmentOffering> departmentOfferings) {
        departmentOfferingList.addAll(departmentOfferings);
        Log.d("ASTC", "added " + departmentOfferings.size() + " elements to departmentOfferingList");
    }

    private synchronized void addToDeptHeadList(List<DeptHead> deptHeads) {
        deptHeadList.addAll(deptHeads);
        Log.d("ASTC", "added " + deptHeads.size() + " elements to deptHeadList");
    }

    private synchronized void addToInstructorList(List<Instructor> instructors) {
        instructorList.addAll(instructors);
        Log.d("ASTC", "added " + instructors.size() + " elements to instructorList");
    }

    private synchronized void addToInstructorListSingle(Instructor instructor) {
        instructorList.add(instructor);
    }

    private synchronized void addToMeetingSectionList(List<MeetingSection> meetingSections) {
        meetingSectionList.addAll(meetingSections);
        Log.d("ASTC", "added " + meetingSections.size() + " elements to meetingSectionList");
    }

    private void addToCourseUrls(String url) {
        courseUrls.add(url);
    }

    public StringRequest getCourseUrlsStringRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                CALENDAR_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // parse the return html
                Document document = Jsoup.parse(response);
                final Elements links = document.getElementsByTag("a");
                for (Element e : links) {
                    if (e.attr("href").startsWith("crs")) {
                        addToCourseUrls(CALENDAR_URL + e.attr("href"));
                    }
                }
                int count = requestCount.decrementAndGet();
//                if (count == 0) {
//                    context.hideProgress();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getCourseUrls", error.getMessage());
            }
        });

        return stringRequest;
    }

    /**
     * Get a StringRequest object that can be queued with Volley's request queue with
     * the given department direct link. This is the link to the department course listing
     * in the calendar and not in the timetable.
     * @param departmentDirectLink the http link to the department's calendar (i.e course
     *                             offerings).
     * @return a Volley StringRequest object that can be queued with Volley's request queue.
     */
    public StringRequest getInstructorListForDepartmentStringRequest(final String departmentDirectLink) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                departmentDirectLink, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                // parse the returned html
                new Thread() {
                    @Override
                    public void run() {
                        Document document = Jsoup.parse(response);
                        final Elements profObjects = document.getElementsByClass(UNMODIFIED_INDENTED);

                        List<Instructor> instructors = new ArrayList<>();
                        for (Element profObject : profObjects) {
                            instructors.add(new Instructor(profObject.text(),
                                    departmentDirectLink.substring(departmentDirectLink.length() - 7,
                                            departmentDirectLink.length() - 4)));
                        }
                        addToInstructorList(instructors);
                    }
                }.start();
                int count = requestCount.decrementAndGet();
//                if (count == 0) {
//                    context.hideProgress();
//                }
//                Document document = Jsoup.parse(response);
//                final Elements profObjects = document.getElementsByClass(UNMODIFIED_INDENTED);
//
//                List<Instructor> instructors = new ArrayList<>();
//                for (Element profObject : profObjects) {
//                    instructors.add(new Instructor(profObject.text(),
//                            departmentDirectLink.substring(departmentDirectLink.length() - 7,
//                                    departmentDirectLink.length() - 4)));
//                }
//                addToInstructorList(instructors);
//                int count = requestCount.decrementAndGet();
//                if (count == 0) {
//                    context.hideProgress();
//                }
//                AsyncTask<String, Void, Void> theTask = new AsyncTask<String, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(String... strings) {
//                        // parse the returned html
//                        String response = strings[0];
//                        Document document = Jsoup.parse(response);
//                        final Elements profObjects = document.getElementsByClass(UNMODIFIED_INDENTED);
//
//                        List<Instructor> instructors = new ArrayList<>();
//                        for (Element profObject : profObjects) {
//                            instructors.add(new Instructor(profObject.text(),
//                                    departmentDirectLink.substring(departmentDirectLink.length() - 7,
//                                            departmentDirectLink.length() - 4)));
//                        }
//                        addToInstructorList(instructors);
//                        return null;
//                    }
//
//                    protected void onPostExecute() {
//                        int count = requestCount.decrementAndGet();
//                        if (count == 0) {
//                            context.hideProgress();
//                        }
//                    }
//                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("instructorListForDept",
                        error.getMessage());
            }
        });

        return stringRequest;
    }
}
