package org.lag.utlookup.back;

/**
 * Created by admin on 8/28/15.
 */
public class DepartmentOffering {

    /**
     * The id of this object as provided by the DBMS. Could be
     * uninitialized.
     */
    public long id;

    /**
     * The code of the department that is offering this course. E.g "CSC".
     */
    public String deptCode;

    /**
     * The course code being offered. E.g "CSC383".
     */
    public String courseCode;

    public DepartmentOffering(long id, String deptCode, String courseCode) {
        this.id = id;
        this.deptCode = deptCode;
        this.courseCode = courseCode;
    }

    public DepartmentOffering() {
    }

    public DepartmentOffering(String deptCode, String courseCode) {
        this.deptCode = deptCode;
        this.courseCode = courseCode;
    }
}
