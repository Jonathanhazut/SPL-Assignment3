package bgu.spl.net.impl.actions;

public class Admin extends Users {

    //constructor

    public Admin(String username, String password){
        super(username, password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
