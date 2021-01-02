package bgu.spl.net.impl.actions;

public class StudentRegCommand extends Command {
    //fields
    private String userName;
    private String password;

    //constructor
    public StudentRegCommand(String userName, String password) {
        super((short) 2);
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
