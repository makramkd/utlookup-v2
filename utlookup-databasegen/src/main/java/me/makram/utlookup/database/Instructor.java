package me.makram.utlookup.database;

/**
 * Created by admin on 8/27/15.
 */
public class Instructor {

    /**
     * A unique identification number that is provided by the dbms as a 64 bit
     * unsigned integer (I think). We keep it here for binding this object to the
     * gui when the need arises.
     */
    public long id;

    /**
     * The name of the instructor.
     */
    public String name;

    /**
     * The department the instructor belongs to. E.g "CSC".
     */
    public String department;

    public Instructor() {

    }

    public Instructor(long id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Instructor(String name, String department) {
        this.name = name;
        this.department = department;
    }
}
