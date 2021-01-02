package bgu.spl.net.impl.actions;

public class KdamCheckCommand extends Command{
    //fields
    private short courseNumber;

    //constructor
    public KdamCheckCommand(short courseNumber) {
        super((short) 6);
        this.courseNumber = courseNumber;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
