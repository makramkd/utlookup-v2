/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.makram.utlookup.database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author admin
 */
public class DatabaseHelper {
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    
    private static DatabaseHelper databaseHelper;
    
    public static final String ORGSQLITE_JDBC = "org.sqlite.JDBC";
    public static final String DATABASE_URL = "jdbc:sqlite:";
    
    public static String DATABASE_NAME = "courseDatabase";
    public static int DATABASE_VERSION = 1;
    public static boolean ENFORCE_FOREIGN_KEYS = false;
    
    private boolean initialized;
    
    private DatabaseHelper(String databaseName, String createQuery) {
        try {
            Class.forName(ORGSQLITE_JDBC);
            
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(ENFORCE_FOREIGN_KEYS);
            
            dbConnection = DriverManager.getConnection(DATABASE_URL + 
                    databaseName +
                    DATABASE_VERSION + ".db", config.toProperties());
                                   
            dbConnection.setAutoCommit(false);
            
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(createQuery);
            
            dbConnection.commit();
            
            initialized = true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName())
                    .log(Level.SEVERE, null, ex);
            initialized = false;
        }
    }
        
    public static DatabaseHelper instance() {
        if (databaseHelper == null) {
            String CREATE_QUERY = null;
            try {
                CREATE_QUERY = Main.readFile("extras/databaseschema.sql",
                        Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(DatabaseHelper.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            databaseHelper = new DatabaseHelper(DATABASE_NAME, CREATE_QUERY);
        }
        
        return databaseHelper;
    }
    
    public final boolean isInitialized() {
        return initialized;
    }
    
    public int[] insertInstructors(List<Instructor> instructors) {
        int[] batchResult = null;
        try {
            String query = "insert into instructor (name, deptCode) values (?, ?);";
            preparedStatement = dbConnection.prepareStatement(query);
            for (Instructor i : instructors) {
                preparedStatement.setString(1, i.name);
                preparedStatement.setString(2, i.department);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
    
    public int[] insertCourses(List<Course> courses) {
        int[] batchResult = null;
        try {
            String query = "insert into course (code, name, description) values (?, ?, ?)";
            preparedStatement = dbConnection.prepareStatement(query);
            for (Course c : courses) {
                preparedStatement.setString(1, c.courseCode);
                preparedStatement.setString(2, c.courseName);
                preparedStatement.setString(3, c.courseDescription == null ?
                        "" : c.courseDescription);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
    
    public int[] insertDepartments(List<Department> departments) {
        int[] batchResult = null;
        try {
            String query = "insert into department (code, name) values (?, ?)";
            preparedStatement = dbConnection.prepareStatement(query);
            for (Department d : departments) {
                preparedStatement.setString(1, d.code);
                preparedStatement.setString(2, d.name);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
    
    public int[] insertDepartmentOfferings(List<DepartmentOffering> departmentOfferings) {
        int[] batchResult = null;
        try {
            String query = "insert into offers (deptCode, courseCode) values (?, ?)";
            preparedStatement = dbConnection.prepareStatement(query);
            for (DepartmentOffering dof : departmentOfferings) {
                preparedStatement.setString(1, dof.deptCode);
                preparedStatement.setString(2, dof.courseCode);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
    
    public int[] insertDeptHeads(List<DeptHead> deptHeads) {
        int[] batchResult = null;
        try {
            String query = "insert into depthead (deptCode, profId) values (?, ?)";
            preparedStatement = dbConnection.prepareStatement(query);
            for (DeptHead dh : deptHeads) {
                preparedStatement.setString(1, dh.deptCode);
                preparedStatement.setLong(2, dh.profId);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
    
    public int[] insertMeetingSections(List<MeetingSection> meetingSections) {
        int[] batchResult = null;
        try {
            String query = "insert into meetingsection (courseCode, sectionCode) values (?, ?)";
            preparedStatement = dbConnection.prepareStatement(query);
            for (MeetingSection ms : meetingSections) {
                preparedStatement.setString(1, ms.courseCode);
                preparedStatement.setString(2, ms.sectionCode);
                preparedStatement.addBatch();
            }
            
            batchResult = preparedStatement.executeBatch();
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return batchResult;
    }
}
