package bgu.spl.net.impl.actions;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.Database;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class MessageProtocol implements MessagingProtocol<Command> {

    //fields
    private Users currentUser;
    private boolean isLoggedIn = false;


    @Override
    public Command process(Command msg) {

        if (msg instanceof AdminRegCommand){
            return processAdminRegCommand((AdminRegCommand) msg);
        }

        if (msg instanceof StudentRegCommand){
            return processStudentRegCommand((StudentRegCommand) msg);
        }

        if (msg instanceof LoginCommand){
            return processLoginCommand((LoginCommand) msg);
        }

        if (msg instanceof LogoutCommand){
            Command output = processLogOutCommand((LogoutCommand) msg);
            shouldTerminate();
            return output;
        }

        if (msg instanceof CourseRegCommand){
            return processCourseRegCommand((CourseRegCommand) msg);
        }

        if (msg instanceof KdamCheckCommand){
            return processKdamCheckCommand((KdamCheckCommand) msg);
        }

        if (msg instanceof CourseStatCommand){
            return processCourseStatCommand((CourseStatCommand) msg);
        }

        if (msg instanceof StudentStatCommand){
            return processStudentStatCommand((StudentStatCommand) msg);
        }

        if (msg instanceof IsRegisteredCommand){
            return processIsRegisteredCommand((IsRegisteredCommand) msg);
        }

        if (msg instanceof  UnregisterCommand){
            return processUnRegisterCommand((UnregisterCommand) msg);
        }

        if (msg instanceof MyCoursesCommand){
            return processMyCoursesCommand((MyCoursesCommand) msg);
        }
        return null;
    }


    public Command processAdminRegCommand(AdminRegCommand command){
        LinkedList<Admin> admins = Database.getInstance().getAdmins();
        String adminUserName = command.getUserName();
        String adminPassword = command.getPassword();
        Admin admin = new Admin(adminUserName, adminPassword);
        currentUser = admin;

        if (admins.contains(admin)){  //checks if the admin is already registered
            return new ErrorCommand((short) 1, "Admin's user name already exists in the system");
        }

        Database.getInstance().setAdmins(admin); //registers the admin
        return new ACKCommand((short) 1); //TODO : add a getter for the encoder
    }


    public Command processStudentRegCommand(StudentRegCommand command){
        LinkedList<Student> students = Database.getInstance().getStudents();
        String studentUserName = command.getUserName();
        String studentPassword = command.getPassword();

        Student student = new Student(studentUserName, studentPassword);
        currentUser = student;

        if (students.contains(student)){ //checks if the student is already registered
            return new ErrorCommand((short) 2, "Student's user name already exists in the system");
        }

        Database.getInstance().setStudents(student); //registers the student
        Database.getInstance().registerStudentToCourseMap(student,null); //no courses have been added yet
        return new ACKCommand((short) 2); //TODO : add a getter for the encoder
    }


    public Command processLoginCommand(LoginCommand command){

        ConcurrentHashMap<String, String> passwordsByUserNames = Database.getInstance().getPasswordsByUserNames();

        String loginUserName = command.getUserName();
        String loginPassword = command.getPassword();

        if (!passwordsByUserNames.containsKey(loginUserName)){ //checks if the userName is registered
            return new ErrorCommand((short) 3, "The user does not register in the system");
        }

        String registeredPassword = passwordsByUserNames.get(loginUserName);

        if (!registeredPassword.equals(loginPassword)){ //if the entered password is wrong
            return new ErrorCommand((short) 3, "The password doesn't match to the one entered at registration");
        }
        if (Database.getInstance().getLoggedInUsers().contains(loginUserName)){ //if the user is already logged in
            return new ErrorCommand((short) 3, "The user already logged in");
        }
        Database.getInstance().setLoggedInUsers(loginUserName); //adds the user to the logged in users list
        isLoggedIn = true;
        return new ACKCommand((short) 3);
    }

    public Command processLogOutCommand(LogoutCommand command){

        LinkedList<String> loggedInUsers = Database.getInstance().getLoggedInUsers();

        if (!loggedInUsers.contains(currentUser.getUsername())){ //checks if the user is logged in
            return new ErrorCommand((short) 4, "The user is not logged in");
        }
        //prepare to logout
        isLoggedIn = false;
        Database.getInstance().logOut(currentUser.getUsername());

        return new ACKCommand((short) 4);

    }

    public Command processCourseRegCommand(CourseRegCommand command){
        if (currentUser instanceof Admin){ //if an admin tries to register to a course
            return new ErrorCommand((short) 5, "Admins cannot register to courses");
        }

        LinkedList<String> loggedInUsers = Database.getInstance().getLoggedInUsers();

        if (!loggedInUsers.contains(currentUser.getUsername())){ //checks if the user is logged into the system
            return new ErrorCommand((short) 5, "The user is not logged in");
        }

        //checks if the requested course number exists
        LinkedList<Integer> courseNumbers = new LinkedList<>(); //list of course numbers
        for(Course c : Database.getInstance().getCourses()){
            courseNumbers.add(c.getCourseNum());
        }

        if (!courseNumbers.contains(command.getCourseNumber())){
            return new ErrorCommand((short) 5, "The number of the course does not exist");
        }

        //checks if all the kdam courses to the requested course had already taken
        Course course = Database.getInstance().getCourseByNumber(command.getCourseNumber()); //Take out the course by the command course number from DB
        if (course.seatsLeft() <= 0){ //checks if there are any open seats in the requested course
            return new ErrorCommand((short) 5, "no seats are available in this course");
        }

        LinkedList<Course> usersCourses = Database.getInstance().getStudentCourseMap().get(currentUser); //list of the courses currUser took
        LinkedList<Integer> kdamCourses = course.getKdamCoursesList(); // course is the input commands course, so we take it's requested kdam courses

        //make the list of courses - usersCourses to an Integer list so that we can compare with the kdamCourses list
        LinkedList<Integer> usersCoursesNumbers = new LinkedList<>();
        for(Course c : usersCourses){
            usersCoursesNumbers.add(c.getCourseNum());
        }
        //checks if al the kdam courses numbers are in the courses list that the curr student took
        for (Integer num : kdamCourses){
            if (!usersCoursesNumbers.contains(num)){
                return new ErrorCommand((short) 5, "The student does not have all the needed kdam courses");
            }
        }


        Database.getInstance().registerStudentToCourseMap((Student) currentUser, course); //register the user to the desired course
        //adds the new student to the list of registeredStudents and counts it
        course.register((Student) currentUser);

        return new ACKCommand((short) 5);

    }

    public Command processKdamCheckCommand(KdamCheckCommand command){
        //if cond that admin can't ask for a KDAMCheck
        if (currentUser instanceof Admin){
            return new ErrorCommand((short) 6, "Admin cannot check Kdam courses");
        }
        //gets the relevant course from the DB
        Course commandsCourse = Database.getInstance().getCourseByNumber(command.getCourseNumber());
//        if (commandsCourse != null){ //TODO: should we check that? if it's null - what do we need to return?
           LinkedList<Integer> kdamCourse = commandsCourse.getKdamCoursesList(); //gets the kdam courses list of the relevant course
           return new ACKCommand((short) 6, kdamCourse.toString());
//       }
    }

    public Command processCourseStatCommand(CourseStatCommand command){
        if (currentUser instanceof Student){
            return new ErrorCommand((short) 8, "Student cannot request course stat request");
        }
        //the command course number
        Course commandCourse = Database.getInstance().getCourseByNumber(command.getCourseNumber());

        //course:
        String courseName = commandCourse.getCourseName();
        String course = "(" + command.getCourseNumber() + ") " + courseName;

        //seats available:
        int seatsLeft = commandCourse.seatsLeft(); //get the number of seats left
        int maxRegStudents = commandCourse.getMaxStudents(); //get the max students that can register
        String seatsAvailable = seatsLeft + "/" + maxRegStudents;

        //students:
        LinkedList<String> regStudentsNames = commandCourse.getRegisteredStudentNames(); //get the list of names of the registered students
        String students = regStudentsNames.toString();


        String output = "Course: " + course + "\n" + "Seats Available: " + seatsAvailable + "\n" + "Students Registered: " + students;
        return new ACKCommand((short) 7, output);
    }

    public Command processStudentStatCommand(StudentStatCommand command){
        if (currentUser instanceof Student){
            return new ErrorCommand((short) 8, "Only Admin can request student stat");
        }
        String userName = command.getStudentUserName();

        //creating a new student object
        Student tmp = new Student(userName, null);

        //overriding the temp student by the actual student
        Student actualStudent = tmp.getStudentByUserName(userName);

        //takes the list of the courses that this student is registered to
        LinkedList<Course> studentsCoursesList = Database.getInstance().getStudentCourseMap().get(actualStudent);


        String output = "Student: " + userName + "\n" + "Courses: " + studentsCoursesList.toString();

        return new ACKCommand((short) 8, output);
    }

    public Command processIsRegisteredCommand(IsRegisteredCommand command){
        if (currentUser instanceof Admin){
            return new ErrorCommand((short) 9, "Admin cannot request for IsRegistered command");
        }
        //get the number of the desired course
        int courseNum = command.getCourseNumber();

        //get the course by it's number
        Course course = Database.getInstance().getCourseByNumber(courseNum);

        //get the list of the registered student to this course
        LinkedList<String> registeredStudentNames = course.getRegisteredStudentNames();

        //check if the student is listed in the registered students list
        if (registeredStudentNames.contains(currentUser.username)){
            return new ACKCommand((short) 9, "REGISTERED");
        }
        return new ACKCommand((short) 9, "NOT REGISTERED");
    }

    public Command processUnRegisterCommand(UnregisterCommand command){
        if (currentUser instanceof Admin) {
            return new ErrorCommand((short) 10, "Admin cannot request for UnRegistered command");
        }

        Student student = (Student) currentUser;
        int courseNum = command.getCourseNumber();
        Course course = Database.getInstance().getCourseByNumber(courseNum);

        //checks if the student is registered to the course
        if (!course.unregister(student)){
            return new ErrorCommand((short) 10, "student is not registered to this course");
        }

        //else
        return new ACKCommand((short) 10);
    }

    public Command processMyCoursesCommand(MyCoursesCommand command){
        if (currentUser instanceof Admin) {
            return new ErrorCommand((short) 11, "Admin cannot request for MyCourses command");
        }

        //list of courses
        LinkedList<Course> courses = Database.getInstance().getStudentCourseMap().get(currentUser);
        LinkedList<Integer> coursesNums = new LinkedList<>();

        //build the list of numbers out of the courses list
        for (Course c : courses){
            coursesNums.add(c.getCourseNum());
        }

        return new ACKCommand((short) 11, coursesNums.toString());
    }


    @Override
    public boolean shouldTerminate() {
        return false;
    }
}