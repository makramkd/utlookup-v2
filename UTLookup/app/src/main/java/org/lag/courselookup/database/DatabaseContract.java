package org.lag.courselookup.database;

import android.provider.BaseColumns;

/**
 * This is the database contract class. We will have one database
 * for all campuses. Courses per campus can be distinguished by their
 * section codes (H1, H5, etc.). Refer to databaseschema.sql in the extras
 * folder for the sql schema itself.
 * Created by admin on 8/26/15.
 */
public final class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "coursedb.db";
    private static final String TEXT_TYPE = " text";
    private static final String COMMA_SEP = ",";
    private static final String UNIQUE = " unique";
    private static final String NOT_NULL = " not null";
    private static final String ON_CONFLICT_REPLACE = " on conflict replace";

    public static final String[] CREATE_TABLE_ARRAY = {
            Course.CREATE_TABLE,
            Offers.CREATE_TABLE,
            Department.CREATE_TABLE,
            Instructor.CREATE_TABLE,
            DeptHead.CREATE_TABLE,
            MeetingSection.CREATE_TABLE,
            Teaches.CREATE_TABLE
    };

    public static final String[] DROP_TABLE_ARRAY = {
            Course.DELETE_TABLE,
            Offers.DELETE_TABLE,
            Department.DELETE_TABLE,
            Instructor.DELETE_TABLE,
            DeptHead.DELETE_TABLE,
            MeetingSection.DELETE_TABLE,
            Teaches.DELETE_TABLE
    };

    // empty ctor to prevent accidental instantiation
    private DatabaseContract() {}

    public static abstract class Course implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String CODE_COL1 = "courseCode";
        public static final String NAME_COL2 = "courseName";
        public static final String PREREQS_COL3 = "prereqs";
        public static final String COREQS_COL4 = "coreqs";
        public static final String EXCLUSIONS_COL5 = "exclusions";
        public static final String DESCRIPTION_COL6 = "description";
        public static final String BRDR_COL7 = "brdr";
        public static final String RECPREP_COL8 = "recPrep";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                CODE_COL1 + TEXT_TYPE + UNIQUE + NOT_NULL + COMMA_SEP +
                NAME_COL2 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                PREREQS_COL3 + TEXT_TYPE + COMMA_SEP +
                COREQS_COL4 + TEXT_TYPE + COMMA_SEP +
                EXCLUSIONS_COL5 + TEXT_TYPE + COMMA_SEP +
                DESCRIPTION_COL6 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                BRDR_COL7 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                RECPREP_COL8 + TEXT_TYPE + ");";

        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class Offers implements BaseColumns {
        public static final String TABLE_NAME = "offers";
        public static final String DEPTCODE_COL1 = "deptCode";
        public static final String COURSECODE_COL2 = "courseCode";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                DEPTCODE_COL1 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                COURSECODE_COL2 + TEXT_TYPE + NOT_NULL + COMMA_SEP +

                // unique constraint: the department code and the course code
                // together are unique.
                UNIQUE + "(" + DEPTCODE_COL1 + COMMA_SEP + COURSECODE_COL2 + ")" +
                ON_CONFLICT_REPLACE +

                // first foreign key constraint: department code
                "foreign key(" + DEPTCODE_COL1 + ") references " +
                Department.TABLE_NAME + "(" + DEPTCODE_COL1 + ")" + COMMA_SEP +

                // second foreign key constraint: course code
                "foreign key(" + COURSECODE_COL2 + ") references " +
                Course.TABLE_NAME + "(" + Course.CODE_COL1 + ")" +
                ");";

        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class Department implements BaseColumns {
        public static final String TABLE_NAME = "department";
        public static final String DEPTCODE_COL1 = "deptCode";
        public static final String DEPTNAME_COL2 = "deptName";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                DEPTCODE_COL1 + TEXT_TYPE + UNIQUE + NOT_NULL + COMMA_SEP +
                DEPTNAME_COL2 + TEXT_TYPE + NOT_NULL +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class Instructor implements BaseColumns {
        public static final String TABLE_NAME = "instructor";
        public static final String NAME_COL1 = "name";
        public static final String DEPTCODE_COL2 = "deptCode";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                NAME_COL1 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                DEPTCODE_COL2 + TEXT_TYPE + NOT_NULL + COMMA_SEP +

                // foreign key constraint:
                "foreign key(" + DEPTCODE_COL2 + ") references " +
                Department.TABLE_NAME + "(" + Department.DEPTCODE_COL1 + ")" +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class DeptHead implements BaseColumns {
        public static final String TABLE_NAME = "depthead";
        public static final String DEPTCODE_COL1 = "deptCode";
        public static final String PROFID_COL2 = "profId";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                DEPTCODE_COL1 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                PROFID_COL2 + " integer " + NOT_NULL + COMMA_SEP +

                // unique constraint: the combination of dept code and prof is unique
                UNIQUE + "(" + DEPTCODE_COL1 + COMMA_SEP + PROFID_COL2 + ")" + ON_CONFLICT_REPLACE +
                COMMA_SEP +

                // first foreign key: dept code
                "foreign key(" + DEPTCODE_COL1 + ") references " +
                Department.TABLE_NAME + "(" + Department.DEPTCODE_COL1 + ")" + COMMA_SEP +

                // second foreign key: professor id
                "foreign key(" + PROFID_COL2 + ") references " +
                Instructor.TABLE_NAME + "(" + Instructor._ID + ")" +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class MeetingSection implements BaseColumns {
        public static final String TABLE_NAME = "meetingsection";
        public static final String COURSECODE_COL1 = "courseCode";
        public static final String SECTIONCODE_COL2 = "sectionCode";
        public static final String LOCATION_COL3 = "location";
        public static final String ENRLINDICATOR_COL4 = "enrolIndicator";
        public static final String TIMES_COL5 = "times";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                COURSECODE_COL1 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                SECTIONCODE_COL2 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                LOCATION_COL3 + TEXT_TYPE + COMMA_SEP +
                ENRLINDICATOR_COL4 + TEXT_TYPE + COMMA_SEP +
                TIMES_COL5 + TEXT_TYPE + COMMA_SEP +

                // unique constraint: course code and section code are unique
                UNIQUE + "(" + COURSECODE_COL1 + COMMA_SEP + SECTIONCODE_COL2 + ")" +
                ON_CONFLICT_REPLACE +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

    public static abstract class Teaches implements BaseColumns {
        public static final String TABLE_NAME = "teaches";
        public static final String PROFID_COL1 = "profid";
        public static final String COURSECODE_COL2 = "courseCode";
        public static final String SECTIONCODE_COL3 = "sectionCode";

        public static final String CREATE_TABLE = "create table " +
                TABLE_NAME + " (" +
                _ID + " integer primary key," +
                PROFID_COL1 + " integer " + NOT_NULL + COMMA_SEP +
                COURSECODE_COL2 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                SECTIONCODE_COL3 + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                UNIQUE + "(" + PROFID_COL1 + COMMA_SEP + COURSECODE_COL2 + COMMA_SEP +
                SECTIONCODE_COL3 + ")" + ON_CONFLICT_REPLACE + COMMA_SEP +
                "foreign key(" + COURSECODE_COL2 + COMMA_SEP + SECTIONCODE_COL3 + ") references " +
                MeetingSection.TABLE_NAME + "(" + MeetingSection.COURSECODE_COL1 + COMMA_SEP +
                MeetingSection.SECTIONCODE_COL2 + ")" +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }

}
