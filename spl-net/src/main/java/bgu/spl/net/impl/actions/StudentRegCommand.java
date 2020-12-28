package bgu.spl.net.impl.actions;

public class StudentRegCommand extends Command {
    //fields
    private String password;
    private String userName;

    //constructor
    public StudentRegCommand(String password, String userName) {
        super((short) 2);
        this.password = password;
        this.userName = userName;
    }

}
