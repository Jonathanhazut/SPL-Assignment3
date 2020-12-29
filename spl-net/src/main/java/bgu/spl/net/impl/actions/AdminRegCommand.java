package bgu.spl.net.impl.actions;

public class AdminRegCommand extends Command {

    //fields
    private String password;
    private String userName;

    //constructor
    public AdminRegCommand(String userName, String password) {
        super((short) 1);
        this.userName = userName;
        this.password = password;
    }



}
