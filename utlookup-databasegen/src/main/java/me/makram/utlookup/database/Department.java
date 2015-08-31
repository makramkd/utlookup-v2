package me.makram.utlookup.database;

/**
 * The Department class/entity will be used to encapsulate
 * information about a department in the university.
 * Created by admin on 8/26/15.
 */
public class Department {
    /**
     * An identification number that is unique and that will probably be
     * provided by the database. The only reason we put it here is so that
     * when we extract department info from the database, we can store it here.
     * We probably won't show this id to the user.
     */
    public int id;

    /**
     * The name of the department, for example "Mathematics" or "Computer Science".
     */
    public String name;

    /**
     * The code of the department. This is the prefix for the courses offered by them,
     * for example, "CSC" or "MAT".
     */
    public String code;

    public Department() {

    }

    public Department(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Department(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
