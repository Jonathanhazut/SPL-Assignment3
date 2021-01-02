package bgu.spl.net.srv;


import bgu.spl.net.impl.actions.Admin;
import bgu.spl.net.impl.actions.Course;
import bgu.spl.net.impl.actions.Student;
import bgu.spl.net.impl.actions.Users;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

    //fields
    private LinkedList<Course> courses;
    private volatile LinkedList<Student> students;
    private volatile LinkedList<Admin> admins;
    private volatile ConcurrentHashMap<String, String> passwordsByUserNames;
    private volatile LinkedList<String> loggedInUsers;
    private volatile ConcurrentHashMap<Student, LinkedList<Course>> studentCourseMap;



    //to prevent users from creating new Database
    private Database() {
        this.courses = new LinkedList<>();
        this.students = new LinkedList<>();
        this.admins = new LinkedList<>();
        this.passwordsByUserNames = new ConcurrentHashMap<>();
        this.loggedInUsers = new LinkedList<>();
        this.studentCourseMap = new ConcurrentHashMap<>();
    }

    public void registerStudentToCourseMap(Student student, Course course) {
        course.register(student);
    }

    public ConcurrentHashMap<Student, LinkedList<Course>> getStudentCourseMap() {
        return studentCourseMap;
    }

    public void logOut(String userName){
        loggedInUsers.remove(userName);
    }

    public LinkedList<String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(String userName) {
        loggedInUsers.add(userName);
    }

    public void setUserNamesAndPasswords(String userName, String password) {
        passwordsByUserNames.put(userName, password);
    }

    public ConcurrentHashMap<String, String> getPasswordsByUserNames() {
        return passwordsByUserNames;
    }

    public String getPasswordByUSerName(String userName){
        return passwordsByUserNames.get(userName); //return the password attached to the input userName
    }

    public void setAdmins(Admin admin) {
        admins.add(admin);
    }

    public void setStudents(Student student) {
        students.add(student);
    }

    public void setCourses(LinkedList<Course> courses) {
        this.courses = courses;
    }

    public LinkedList<Admin> getAdmins() {
        return admins;
    }

    public LinkedList<Student> getStudents() {
        return students;
    }

    public LinkedList<Course> getCourses() {
        return courses;
    }

    public Course getCourseByNumber(int courseNum){
        for(Course c : courses){
            if (c.getCourseNum() == courseNum){
                return c;
            }
        }
        return null;
    }

    /**
     * Retrieves the single instance of this class.
     */

    //make Database a singleton
    private static class SingleHolder {
        private static Database instance = new Database();
    }

    public static Database getInstance() {
        return SingleHolder.instance;//singleton;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    boolean initialize(String coursesFilePath) {
        File file = new File(coursesFilePath);
        try{
            Scanner scanner = new Scanner(new FileReader(file));
            String[] currLine; //String that holds everyLine that represents a single course
            while (scanner.hasNextLine()){
                currLine = scanner.nextLine().split("[|]");

                int courseNum = Integer.parseInt(currLine[0]); //courseNum

                String courseName = currLine[1]; //courseName

                List<String> tmp = new ArrayList<String>(Arrays.asList(currLine[3].split(",")));
                LinkedList<Integer> kdamCoursesList = new LinkedList<>(); //kdamCourses
                for(String s : tmp){ //change each string to Integer and add tp the list
                    kdamCoursesList.add(Integer.parseInt(s));
                }

                int maxStudents = Integer.parseInt(currLine[4]); //maxStudents

                Course currentCourse = new Course(courseNum, courseName, kdamCoursesList, maxStudents); //new currentCourse as a course object
                courses.add(currentCourse); //add the new course to the field courses
                return true; //if successful
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


}