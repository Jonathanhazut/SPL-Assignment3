package bgu.spl.net.impl.actions;

public class CourseRegCommand extends Command{
    //fields
    private short courseNumber;

    //constructor
    public CourseRegCommand(short courseNumber) {
        super((short) 5);
        this.courseNumber = courseNumber;
    }

}
