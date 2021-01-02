package bgu.spl.net.impl.actions;

public class CourseStatCommand extends Command{
    //fields
    private  short courseNumber;
    //constructor
    public CourseStatCommand(short courseNumber) {
        super((short) 7);
        this.courseNumber = courseNumber;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
