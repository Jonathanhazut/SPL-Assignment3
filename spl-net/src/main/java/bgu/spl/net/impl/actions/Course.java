package bgu.spl.net.impl.actions;

import java.util.LinkedList;

public class Course {

    //fields
    private String courseName;
    private int courseNum;
    private LinkedList<Integer> kdamCoursesList;
    private int maxStudents;
    private int numberOfRegisteredStudents;
    private LinkedList<String> registeredStudentNames;

    //constructor
    public Course(int courseNum, String courseName, LinkedList<Integer> kdamCoursesList, int maxStudents){
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdamCoursesList = kdamCoursesList;
        this.maxStudents = maxStudents;
        this.numberOfRegisteredStudents =0;
        this.registeredStudentNames = new LinkedList<>();
    }

    public int getCourseNum() {
        return courseNum;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public LinkedList<Integer> getKdamCoursesList() {
        return kdamCoursesList;
    }

    public String getCourseName() {
        return courseName;
    }

    public int seatsLeft(){
        return maxStudents - numberOfRegisteredStudents;
    }

    public int getNumberOfRegisteredStudents() {
        return numberOfRegisteredStudents;
    }

    public void register(Student student){
        registeredStudentNames.add(student.getUsername());
        numberOfRegisteredStudents++;
    }

    public boolean unregister(Student student){
        //returens true is the removal was successful
        if (registeredStudentNames.remove(student.username)) {
            numberOfRegisteredStudents--;
            return true;
        }
        return false;

    }

    public LinkedList<String> getRegisteredStudentNames() {
        return registeredStudentNames;
    }
}
