package org.lag.utlookup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lag.utlookup.back.Course;

/**
 * Created by admin on 8/26/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create the tables
        for (int i = 0; i < DatabaseContract.CREATE_TABLE_ARRAY.length; ++i) {
            sqLiteDatabase.execSQL(DatabaseContract.CREATE_TABLE_ARRAY[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // drop the tables
        for (int i = 0; i < DatabaseContract.DROP_TABLE_ARRAY.length; ++i) {
            sqLiteDatabase.execSQL(DatabaseContract.DROP_TABLE_ARRAY[i]);
        }

        // create again
        onCreate(sqLiteDatabase);
    }

    /**
     * Insert a course into the course table in the database.
     * @param course the course to be inserted. Cannot be null.
     * @return the rowid of the inserted row. This also happens to be the primary key of the table.
     */
    public long insertCourse(@NonNull Course course) {
        return -1; // stub
    }

    /**
     * Insert a course offering into the offers table.
     * @param deptCode the code of the department offering the course (e.g "CSC"). Cannot be null.
     * @param courseCode the code of the course being offered by the department (e.g "CSC301").
     *                   Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertOffering(@NonNull String deptCode, @NonNull String courseCode) {
        return -1; // stub
    }

    /**
     * Insert an instructor into the instructor table.
     * @param name the name of the instructor. Cannot be null.
     * @param deptCode the department he belongs to. Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertInstructor(@NonNull String name, @NonNull String deptCode) {
        return -1; // stub
    }

    /**
     * Insert a department head into the depthead table. Note that we may only insert into this
     * table when both the department table and the instructor table are filled.
     * @param deptCode the code of the department (e.g "CSC"). Cannot be null.
     * @param profId the id of the prof as in the instructor table. Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertDeptHead(@NonNull String deptCode, @NonNull long profId) {
        return -1; // stub
    }

    /**
     * Insert a department into the department table.
     * @param deptCode the code of the department (e.g "CSC"). Cannot be null.
     * @param deptName the name of the department (e.g "Mathematics"). Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertDepartment(@NonNull String deptCode, @NonNull String deptName) {
        return -1; // stub
    }

    /**
     * Insert a teaches tuple into the teaches table. See the schema for a reference.
     * @param profId the integer id of the professor that is teaching the section for the course.
     *               Can never be null.
     * @param courseCode the course code being taught. Can never be null.
     * @param sectionCode the section code of the course being taught. Never null.
     * @return the rowid of the inserted row.
     */
    public long insertTeaches(@NonNull long profId, @NonNull String courseCode,
                              @NonNull String sectionCode) {
        return -1; // stub
    }

    /**
     * Insert a meeting section into the meetingsection table. See the schema for a reference.
     * @param courseCode the course code of the meeting section. Never null.
     * @param sectionCode the section code of the meeting section. Never null.
     * @param location the location of the meeting section (i.e "GB112"). Can be null.
     * @param enrolmentIndicator the enrolment indicator of the meeting section (i.e "P").
     *                           Can be null.
     * @param times the times it is offered in the week (e.g "W1-5"). Can be null.
     * @return the rowid of the inserted row.
     */
    public long insertMeetingSection(@NonNull String courseCode, @NonNull String sectionCode,
                                     @Nullable String location,
                                     @Nullable String enrolmentIndicator, @Nullable String times) {
        return -1; // stub
    }


}
