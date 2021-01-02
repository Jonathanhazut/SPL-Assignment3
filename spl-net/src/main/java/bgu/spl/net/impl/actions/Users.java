package bgu.spl.net.impl.actions;

public abstract class Users {

    //fields
    protected String username;
    protected String password;



    //constructor

    public Users(String username, String password){
        this.username = username;
        this.password = password;
    }

    public abstract String getUsername();

    public abstract String getPassword();
}
