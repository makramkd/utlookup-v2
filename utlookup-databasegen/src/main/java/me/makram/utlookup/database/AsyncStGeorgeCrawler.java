/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
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
    public static final String ASYNC_ST_GEORGE_CRAWLER = "AsyncStGeorgeCrawler";
    public static final String EXCLUSION_CLASS = "exclusion";
    public static final String PREREQ_CLASS = "prereq";
    public static final String COREQ_CLASS = "coreq";
    public static final String BR_CLASS = "br";
    public static final String DR_CLASS = "dr";
    public static final String RECOMMENDED_PREP_CLASS = "recommendedPrep";
    public static final String ENROLMENT_LIMITS_CLASS = "enrolmentLimits";
    public static final String COURSE_REFERRING_TO = "courseReferringTo";
    public static final String BREADTH_REQUIREMENT = "Breadth Requirement";
    public static final String DISTRIBUTION_REQUIREMENT = "Distribution Requirement";
    public static final String EXCLUSION = "Exclusion";
    public static final String PREREQUISITE = "Prerequisite";
    public static final String COREQUISITE = "Corequisite";
    public static final String RECOMMENDED = "Recommended";
    public static final String ENROLMENT_LIMITS = "Enrolment Limits";

    public Set<Course> courseList;
    public Set<Department> departmentList;
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
            Logger.getLogger(ASYNC_ST_GEORGE_CRAWLER).log(Level.SEVERE,
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
            Logger.getLogger(ASYNC_ST_GEORGE_CRAWLER).log(Level.SEVERE,
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

    public final Callback offersPerDepartmentCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            Logger.getLogger(ASYNC_ST_GEORGE_CRAWLER).log(Level.SEVERE,
                    "Offers request failed");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Document document = Jsoup.parse(response.body().string());
            String departmentDirectLink = response.request().urlString();
            List<DepartmentOffering> departmentOfferings = new ArrayList<>();
            Elements strongObjects = document.getElementsByClass("strong");

            // java 7 solution
            List<Element> courseObjects = new ArrayList<>();
            for (Element e : strongObjects) {
                if (e.text().length() >= 8
                        && Pattern.matches(COURSE_REGEX, e.text().substring(0, 8))) {
                    courseObjects.add(e);
                }
            }

            String deptCode = departmentDirectLink.substring(departmentDirectLink.length() - 7,
                    departmentDirectLink.length() - 4).toUpperCase();
            for (Element e : courseObjects) {
                String text = e.text();
                String code = text.substring(0, 8);
                departmentOfferings.add(new DepartmentOffering(deptCode, code));
            }

            addToDepartmentOfferingList(departmentOfferings);

            requestCount.getAndDecrement();
        }
    };

    public final Callback coursesPerDepartmentCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            Logger.getLogger(ASYNC_ST_GEORGE_CRAWLER).log(Level.SEVERE,
                    "Courses request failed");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Document document = annotateCourseDetails(Jsoup.parse(response.body().string()));

            Set<Course> courses = new TreeSet<>();
            Elements strongObjects = document.getElementsByClass("strong");

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
                if ((descriptionParagraph != null) && descriptionParagraph.tagName().equals("p")) {
                    descriptionText = descriptionParagraph.text();
                }

                Course course = new Course(code, name);
                course.courseDescription = descriptionText;

                Elements details = document.getElementsByAttributeValue(COURSE_REFERRING_TO, code);
                for (Element detail : details) {
                    final String aClass = detail.attr("class");
                    if (aClass.equals(PREREQ_CLASS)) {
                        String[] prePrereq = detail.text().split(":");
                        String s = prePrereq.length == 1 ? "" : prePrereq[1];
                        course.prerequisites = s + getCourseDetails(detail);
                    } else if (aClass.equals(COREQ_CLASS)) {
                        String[] coreqReq = detail.text().split(":");
                        String s = coreqReq.length == 1 ? "" : coreqReq[1];
                        course.corequisites = getCourseDetails(detail);
                    } else if (aClass.equals(EXCLUSION_CLASS)) {
                        String[] exclusionSion = detail.text().split(":");
                        String s = exclusionSion.length == 1 ? "" : exclusionSion[1];
                        course.exclusions = s + getCourseDetails(detail);
                    } else if (aClass.equals(RECOMMENDED_PREP_CLASS)) {
                        String[] recPrepPrep = detail.text().split(":");
                        String s = recPrepPrep.length == 1 ? "" : recPrepPrep[1];
                        course.recommendedPreparation = s + getCourseDetails(detail);
                    } else if (aClass.equals(BR_CLASS)) {
                        String[] theText = detail.text().split(":");
                        course.breadthRequirement = theText[1];
                    } else if (aClass.equals(DR_CLASS)) {
                        String[] theText = detail.text().split(":");
                        course.distributionRequirement = theText[1];
                    }
                }

                courses.add(course);
            }

            addToCourseList(courses);

            requestCount.getAndDecrement();
        }
    };

    public final Callback departmentsCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            Logger.getLogger(ASYNC_ST_GEORGE_CRAWLER).log(Level.SEVERE,
                    "Department list request failed");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Document document = Jsoup.parse(response.body().string());

            final Elements links = document.getElementsByTag("a");
            List<Element> linksToCourses = new ArrayList<>();
            for (Element e : links) {
                if (e.attr("href").startsWith("crs")) {
                    linksToCourses.add(e);
                }
            }

            Set<Department> departmentSet = new TreeSet<>();

            for (Element e : linksToCourses) {
                departmentSet.add(new Department(e.text(),
                        e.attr("href").substring(4, 7).toUpperCase()));
            }

            addToDepartmentList(departmentSet);

            requestCount.getAndDecrement();
        }
    };

    public AsyncStGeorgeCrawler() {
        courseList = new TreeSet<>();
        departmentList = new TreeSet<>();
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

    private Document annotateCourseDetails(Document document) {
        List<Node> childNodes = document.body().childNodes();

        for (Node childNode : childNodes) {
            String nodeText = childNode.toString();
            if (childNode instanceof TextNode &&
                    (nodeText.startsWith(BREADTH_REQUIREMENT) ||
                            nodeText.startsWith(DISTRIBUTION_REQUIREMENT) ||
                            nodeText.startsWith(EXCLUSION) ||
                            nodeText.startsWith(PREREQUISITE) ||
                            nodeText.startsWith(COREQUISITE) ||
                            nodeText.startsWith(RECOMMENDED) ||
                            nodeText.startsWith(ENROLMENT_LIMITS)
                    )) {
                // go back until first span.strong element
                Node courseNode = null;
                Node previousSibling = childNode.previousSibling();
                while (previousSibling != null) {
                    if (previousSibling.attr("class").equals("strong")) {
                        courseNode = previousSibling;
                        break;
                    } else {
                        previousSibling = previousSibling.previousSibling();
                    }
                }

                if (courseNode != null) {
                    Element theCourse = (Element) courseNode;
                    String className = getClassName(nodeText);
                    childNode.wrap("<span courseReferringTo=\"" + theCourse.text().substring(0, 8) + "\"" +
                            " class=\"" + className + "\">"
                            + "</span>");
                }
            }
        }

        return document;
    }

    private String getClassName(String nodeText) {
        if (nodeText.startsWith("Exclusion")) {
            return EXCLUSION_CLASS;
        } else if (nodeText.startsWith(PREREQUISITE)) {
            return PREREQ_CLASS;
        } else if (nodeText.startsWith(COREQUISITE)) {
            return COREQ_CLASS;
        } else if (nodeText.startsWith("Breadth")) {
            return BR_CLASS;
        } else if (nodeText.startsWith("Distribution")) {
            return DR_CLASS;
        } else if (nodeText.startsWith(RECOMMENDED)) {
            return RECOMMENDED_PREP_CLASS;
        } else if (nodeText.startsWith(ENROLMENT_LIMITS)) {
            return ENROLMENT_LIMITS_CLASS;
        }

        return "undefined";
    }

    private String getCourseDetails(Element detailNode) {
        StringBuilder sb = new StringBuilder();
        Node currentNode = detailNode.nextSibling();
        boolean isTextNode = false, isElement = false;
        TextNode textNode = null;
        Element textElement = null;
        while (currentNode != null && !currentNode.hasAttr(COURSE_REFERRING_TO)) {
            if (currentNode instanceof TextNode) {
                isTextNode = true;
                isElement = false;
                textNode = (TextNode) currentNode;
            } else if (currentNode instanceof Element) {
                isTextNode = false;
                isElement = true;
                textElement = (Element) currentNode;
            }
            if (isElement && textElement != null) {
                sb.append(textElement.text());
            } else if (isTextNode && textNode != null){
                sb.append(textNode.toString());
            } else {
                sb.append(currentNode.toString());
            }
            currentNode = currentNode.nextSibling();
        }

        return sb.toString();
    }

    private synchronized void addToCourseList(Collection<Course> courses) {
        courseList.addAll(courses);
        System.out.println("Added " + courses.size() + " courses to the list");
    }

    private synchronized void addToDepartmentList(Collection<Department> departments) {
        departmentList.addAll(departments);
        System.out.println("Added " + departments.size() + " to department list.");
    }

    private synchronized void addToDepartmentOfferingList(List<DepartmentOffering> departmentOfferings) {
        departmentOfferingList.addAll(departmentOfferings);
        System.out.println("Added " + departmentOfferings.size() + " department offerings to list.");
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

    public Request getCourseListForDepartmentRequest(String departmentDirectLink) {
        Request request = new Request.Builder()
                .url(departmentDirectLink)
                .build();

        return request;
    }

    public Request getDepartmentOfferingsForDepartmentRequest(String departmentDirectLink) {
        Request request = new Request.Builder()
                .url(departmentDirectLink)
                .build();

        return request;
    }

    public Request getDepartmentsRequest() {
        Request request = new Request.Builder()
                .url(StGeorgeCrawler.CALENDAR_URL)
                .build();

        return request;
    }
}
