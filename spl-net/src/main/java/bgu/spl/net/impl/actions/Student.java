package bgu.spl.net.impl.actions;

public class Student extends Users{


    public Student(String userName, String password){
        super(userName, password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Student getStudentByUserName(String userName){
        if (this.username.equals(userName)) {
            return this;
        }
        return null;
    }

}
