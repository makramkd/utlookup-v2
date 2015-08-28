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
    public long deptCode;

    /**
     * The course code being offered. E.g "CSC383".
     */
    public long courseCode;

    public DepartmentOffering(long id, long deptCode, long courseCode) {
        this.id = id;
        this.deptCode = deptCode;
        this.courseCode = courseCode;
    }

    public DepartmentOffering() {
    }

    public DepartmentOffering(long deptCode, long courseCode) {
        this.deptCode = deptCode;
        this.courseCode = courseCode;
    }
}
