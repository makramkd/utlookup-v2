package org.lag.utlookup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
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
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Course.CODE_COL1, course.courseCode);
        values.put(DatabaseContract.Course.NAME_COL2, course.courseName);
        values.put(DatabaseContract.Course.PREREQS_COL3, course.prerequisites);
        values.put(DatabaseContract.Course.COREQS_COL4, course.corequisites);
        values.put(DatabaseContract.Course.EXCLUSIONS_COL5, course.exclusions);
        values.put(DatabaseContract.Course.DESCRIPTION_COL6, course.courseDescription);

        String brdr = null;
        if (course.breadthRequirement != null && course.distributionRequirement != null) {
            brdr = course.breadthRequirement + course.distributionRequirement;
        }
        values.put(DatabaseContract.Course.BRDR_COL7, brdr);
        values.put(DatabaseContract.Course.RECPREP_COL8, course.recommendedPreparation);

        long rowid = db.insert(DatabaseContract.Course.TABLE_NAME, null, values);

        db.close();

        return rowid;
    }

    public long insertCourse(@NonNull String courseCode, @NonNull String courseName,
                             String prereqs, String coreqs, String exclusions,
                             @NonNull String description,
                             String brdr, String recPrep){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Course.CODE_COL1, courseCode);
        values.put(DatabaseContract.Course.NAME_COL2, courseName);
        values.put(DatabaseContract.Course.PREREQS_COL3, prereqs);
        values.put(DatabaseContract.Course.COREQS_COL4, coreqs);
        values.put(DatabaseContract.Course.EXCLUSIONS_COL5, exclusions);
        values.put(DatabaseContract.Course.DESCRIPTION_COL6, description);
        values.put(DatabaseContract.Course.BRDR_COL7, brdr);
        values.put(DatabaseContract.Course.RECPREP_COL8, recPrep);

        long rowid = db.insert(DatabaseContract.Course.TABLE_NAME, null, values);

        db.close();

        return rowid;
    }

    public long insertCourse(@NonNull String courseCode, @NonNull String courseName,
                             @NonNull String description) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Course.CODE_COL1, courseCode);
        values.put(DatabaseContract.Course.NAME_COL2, courseName);
        values.put(DatabaseContract.Course.DESCRIPTION_COL6, description);

        long rowid = db.insert(DatabaseContract.Course.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
    }

    /**
     * Insert a course offering into the offers table. Notice that the course and department
     * tables must be filled up before you do this or you will get a foreign key error.
     * @param deptCode the code of the department offering the course (e.g "CSC"). Cannot be null.
     * @param courseCode the code of the course being offered by the department (e.g "CSC301").
     *                   Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertOffering(@NonNull String deptCode, @NonNull String courseCode) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Offers.DEPTCODE_COL1, deptCode);
        values.put(DatabaseContract.Offers.COURSECODE_COL2, courseCode);

        long rowid = db.insert(DatabaseContract.Offers.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
    }

    /**
     * Insert an instructor into the instructor table. We need to fill up the departments table
     * to do this because of the foreign key constraint on deptCode.
     * @param name the name of the instructor. Cannot be null.
     * @param deptCode the department he belongs to. Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertInstructor(@NonNull String name, @NonNull String deptCode) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Instructor.NAME_COL1, name);
        values.put(DatabaseContract.Instructor.DEPTCODE_COL2, deptCode);

        long rowid = db.insert(DatabaseContract.Instructor.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
    }

    /**
     * Insert a department head into the depthead table. Note that we may only insert into this
     * table when both the department table and the instructor table are filled.
     * @param deptCode the code of the department (e.g "CSC"). Cannot be null.
     * @param profId the id of the prof as in the instructor table. Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertDeptHead(@NonNull String deptCode, @NonNull long profId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DeptHead.DEPTCODE_COL1, deptCode);
        values.put(DatabaseContract.DeptHead.PROFID_COL2, profId);

        long rowid = db.insert(DatabaseContract.DeptHead.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
    }

    /**
     * Insert a department into the department table.
     * @param deptCode the code of the department (e.g "CSC"). Cannot be null.
     * @param deptName the name of the department (e.g "Mathematics"). Cannot be null.
     * @return the rowid of the inserted row.
     */
    public long insertDepartment(@NonNull String deptCode, @NonNull String deptName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Department.DEPTCODE_COL1, deptCode);
        values.put(DatabaseContract.Department.DEPTNAME_COL2, deptName);

        long rowid = db.insert(DatabaseContract.Department.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
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
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Teaches.PROFID_COL1, profId);
        values.put(DatabaseContract.Teaches.COURSECODE_COL2, courseCode);
        values.put(DatabaseContract.Teaches.SECTIONCODE_COL3, sectionCode);

        long rowid = db.insert(DatabaseContract.Teaches.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
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
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MeetingSection.COURSECODE_COL1, courseCode);
        values.put(DatabaseContract.MeetingSection.SECTIONCODE_COL2, sectionCode);
        values.put(DatabaseContract.MeetingSection.LOCATION_COL3, location);
        values.put(DatabaseContract.MeetingSection.ENRLINDICATOR_COL4, enrolmentIndicator);
        values.put(DatabaseContract.MeetingSection.TIMES_COL5, times);

        long rowid = db.insert(DatabaseContract.MeetingSection.TABLE_NAME,
                null, values);

        db.close();

        return rowid;
    }

    /**
     * Delete the course with the given courseCode from the course table.
     * @param courseCode the course code of the course tuple to delete.
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteCourse(@NonNull String courseCode) {
        return -1; // stub
    }

    /**
     * Delete the offer with the given deptCode and courseCode from the offers table.
     * @param deptCode the deptCode of the course offering
     * @param courseCode the course code of the offering
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteOffer(@NonNull String deptCode, @NonNull String courseCode) {
        return -1; // stub
    }

    /**
     * Delete a meeting section from the meetingsection table.
     * @param courseCode
     * @param sectionCode
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteMeetingSection(@NonNull String courseCode, @NonNull String sectionCode) {
        return -1; // stub
    }

    /**
     * Delete a teaches tuple from the teaches table.
     * @param profid the id of the professor who teaches the course
     * @param courseCode the course code of the course
     * @param sectionCode the section code of the course
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteTeaches(long profid, @NonNull String courseCode,
                             @NonNull String sectionCode) {
        return -1; // stub
    }

    /**
     * Delete a department from the department table.
     * @param deptCode the department code of the department to be deleted.
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteDepartment(@NonNull String deptCode) {
        return -1; // stub
    }

    /**
     * Delete an instructor from the instructor table.
     * @param profId the id of the prof being deleted.
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteInstructor(long profId) {
        return -1; // stub
    }

    /**
     * Delete a department head from the depthead table.
     * @param deptCode the department code the head belongs to
     * @param profId the id of the head as found in the instructor table.
     * @return the number of rows deleted (should be 1 or 0)
     */
    public int deleteDeptHead(@NonNull String deptCode, long profId) {
        return -1; // stub
    }
}
