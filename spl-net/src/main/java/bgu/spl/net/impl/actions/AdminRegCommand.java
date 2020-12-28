package bgu.spl.net.impl.actions;

public class AdminRegCommand extends Command {

    //fields
    private String password;
    private String userName;

    //constructor
    public AdminRegCommand(String password, String userName) {
        super((short) 1);
        this.password = password;
        this.userName = userName;
    }



}
