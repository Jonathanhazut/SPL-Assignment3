package bgu.spl.net.impl.actions;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;

public class MessageEncDec implements MessageEncoderDecoder<Command> {

    //fields
    private byte[] message = new byte[1 << 10];
    private int len = 0;
    private byte[] opcodeBytes = new byte[2]; //length is 2 because opcode is 2 bytes.
    private short opcode = -1;
    private int zeroCounter = 2;
    private String userName = "";
    private String password = "";




    @Override
    public Command decodeNextByte(byte nextByte) {
        if (opcode == -1) { //we don't know the opcode yet
            opcodeBytes[len++] = nextByte;
            if (len == 2) {
                opcode = bytesToShort(opcodeBytes);
                len = 0;
            }
            return null;
        }
        if (opcode == 1 | opcode == 2 | opcode == 3 | opcode == 8) {

            if (nextByte != 0) { //maybe should be /0
                message[len++] = nextByte;
            }
            else{
                zeroCounter--; //never bigger than 2.
                if(zeroCounter == 1) {
                    userName = getUsername(userName);
                    if(opcode == 8){
                        return new StudentStatCommand(userName);
                    }
                }
                if(zeroCounter == 0){
                    password = getPassword(password);
                }
                zeroCounter = 2;
            }
            return getCommandByUsernameAndPassword(opcode, userName, password);
        }

        else if(opcode == 5 | opcode == 6 | opcode == 7 | opcode == 9 | opcode == 10){
            message[len++] = nextByte;
            if(len == 2){
                short courseNumber = bytesToShort(message);
                len = 0;
                return getCommandByCourseNumber(opcode, courseNumber);
            }
        }

        else if(opcode == 4 | opcode == 11){
            return getCommand(opcode);
        }

        return null;
    }

    public String getUsername(String output){
        output = new String(message, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return output;
    }

    public String getPassword(String output){ // is it stupid ??
        output = new String(message, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return output;
    }

    public Command getCommandByUsernameAndPassword(short opcode, String username, String password){
//        if(opcode == 1){
//            return new AdminRegCommand(username, password);
//        }
//        else if(opcode == 2){
//            return new StudentRegCommand(username, password);
//        }
//        else{ // opcode is 3
//            return new LoginCommand(username, password);
//        }
        switch (opcode){
            case 1:
                return new AdminRegCommand(username,password);
            case 2:
                return new StudentRegCommand(username,password);
            case 3:
                return new LoginCommand(username,password);
        }
        return null;
    }

    public Command getCommandByCourseNumber(short opcode, short courseNumber){
//        if(opcode == 5){
//            return new CourseRegCommand(courseNumber);
//        }
//        else if(opcode == 6){
//            return new KdamCheckCommand(courseNumber);
//        }
//        else if(opcode == 7){
//            return new CourseStatCommand(courseNumber);
//        }
//        else if(opcode == 9){
//            return new IsRegisteredCommand(courseNumber);
//        }
//        else{ //opcode is 10
//            return new UnregisterCommand(courseNumber);
//        }
        switch (opcode) {
            case 5:
                return new CourseRegCommand(courseNumber);
            case 6:
                return new KdamCheckCommand(courseNumber);
            case 7:
                return new CourseStatCommand(courseNumber);
            case 9:
                return new IsRegisteredCommand(courseNumber);
            case 10:
                return new UnregisterCommand(courseNumber);
        }
        return null;
    }

    public Command getCommand(short opcode){
//        if(opcode == 4){
//            return new LogoutCommand();
//        }
//        return new MyCoursesCommand();
        switch (opcode){
            case 4:
                return new LogoutCommand();
            case 11:
                return new MyCoursesCommand();
        }
        return null;
    }

    @Override
    public byte[] encode(Command msg) {
        return new byte[0];
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

}