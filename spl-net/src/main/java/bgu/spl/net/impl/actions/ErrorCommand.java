package bgu.spl.net.impl.actions;

public class ErrorCommand extends Command{
    private short messageOpcode;
    private String errorMessage;


    //constructor
    public ErrorCommand(short messageOpcode, String errorMessage) {
        super((short) 13);
        this.messageOpcode = messageOpcode;
        this.errorMessage = errorMessage;

    }

}
