package bgu.spl.net.impl.actions;

public class LoginCommand extends Command {
    //fields
    private String password;
    private String userName;

    //constructor
    public LoginCommand(String userName, String password) {
        super((short) 3);
        this.userName = userName;
        this.password = password;
    }

}
