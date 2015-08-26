package org.lag.utlookup.database;

import android.provider.BaseColumns;

/**
 * Created by admin on 8/26/15.
 */
public final class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "coursedb.db";
    private static final String TEXT_TYPE = " text";
    private static final String COMMA_SEP = ",";
    private static final String UNIQUE = " unique";
    private static final String NOT_NULL = " not null";


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
                BRDR_COL7 + TEXT_TYPE + COMMA_SEP +
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
                "foreign key(" + DEPTCODE_COL2 + ") references " +
                Department.TABLE_NAME + "(" + Department.DEPTCODE_COL1 + ")" +
                ");";
        public static final String DELETE_TABLE = "drop table if exists " + TABLE_NAME + ";";
    }
}
