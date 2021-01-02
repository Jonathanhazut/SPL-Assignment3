package bgu.spl.net.impl.actions;

public class IsRegisteredCommand extends Command{
    //fields
    private short courseNumber;

    //constructor
    public IsRegisteredCommand(short courseNumber) {
        super((short) 9);
        this.courseNumber = courseNumber;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
