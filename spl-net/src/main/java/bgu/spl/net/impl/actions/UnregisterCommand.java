package bgu.spl.net.impl.actions;

public class UnregisterCommand extends Command {
    private short courseNumber;

    //constructor
    public UnregisterCommand(short courseNumber) {
        super((short) 10);
        this.courseNumber = courseNumber;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
