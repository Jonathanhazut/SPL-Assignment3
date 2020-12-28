package bgu.spl.net.impl.actions;

public class LoginCommand extends Command {
    //fields
    private String password;
    private String userName;

    //constructor
    public LoginCommand(String password, String userName) {
        super((short) 3);
        this.password = password;
        this.userName = userName;
    }

}
