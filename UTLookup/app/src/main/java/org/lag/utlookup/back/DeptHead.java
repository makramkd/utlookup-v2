package org.lag.utlookup.back;

/**
 * Created by admin on 8/28/15.
 */
public class DeptHead {

    /**
     * The unique identifier provided by the DBMS. Doesn't have
     * to be set here, but just for convenience.
     */
    public long id;

    /**
     * The department code that this department head is head of. e.g "CSC"
     */
    public String deptCode;

    /**
     * The id of this department head. Notice that it is an integer so to get the name we
     * have to join it to the instructor table and extract it from there, or do a select
     * with this id in the where clause.
     */
    public long profId;

    public DeptHead() {

    }

    public DeptHead(long id, String deptCode, long profId) {
        this.id = id;
        this.deptCode = deptCode;
        this.profId = profId;
    }

    public DeptHead(String deptCode, long profId) {
        this.deptCode = deptCode;
        this.profId = profId;
    }
}
