package bgu.spl.net.impl.actions;

public class AdminRegCommand extends Command {

    //fields
    private String userName;
    private String password;

    //constructor
    public AdminRegCommand(String userName, String password) {
        super((short) 1);
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
