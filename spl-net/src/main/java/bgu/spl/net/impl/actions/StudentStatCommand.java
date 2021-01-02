package bgu.spl.net.impl.actions;

public class StudentStatCommand extends Command{

        //fields
        private String studentUserName;

        //constructor
        public StudentStatCommand(String studentUserName) {
            super((short) 8);
            this.studentUserName = studentUserName;
        }

    public String getStudentUserName() {
        return studentUserName;
    }
}


