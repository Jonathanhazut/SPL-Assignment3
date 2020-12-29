package bgu.spl.net.impl.actions;

public class StudentRegCommand extends Command {
    //fields
    private String password;
    private String userName;

    //constructor
    public StudentRegCommand(String userName, String password) {
        super((short) 2);
        this.userName = userName;
        this.password = password;
    }

}
