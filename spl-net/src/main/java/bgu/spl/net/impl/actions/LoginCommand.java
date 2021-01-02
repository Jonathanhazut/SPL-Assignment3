package bgu.spl.net.impl.actions;

public class LoginCommand extends Command {
    //fields
    private String userName;
    private String password;

    //constructor
    public LoginCommand(String userName, String password) {
        super((short) 3);
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
