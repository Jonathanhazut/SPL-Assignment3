package bgu.spl.net.impl.actions;

public class ErrorCommand extends Command{
    private short messageOpcode;

    //constructor
    public ErrorCommand(short messageOpcode) {
        super((short) 13);
        this.messageOpcode = messageOpcode;
    }

}
