package org.lag.courselookup.back;

/**
 * Created by admin on 8/28/15.
 */
public class Teaches {

    public long id;

    public long profId;

    public String courseCode;

    public String sectionCode;

    public Teaches(long id, long profId, String courseCode, String sectionCode) {
        this.id = id;
        this.profId = profId;
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
    }

    public Teaches(long profId, String courseCode, String sectionCode) {
        this.profId = profId;
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
    }

    public Teaches() {
    }
}
