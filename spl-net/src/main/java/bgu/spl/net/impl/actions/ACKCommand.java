package bgu.spl.net.impl.actions;

public class ACKCommand extends Command{
    private short messageOpcode;
    private String ackMessage;

    //constructor
    public ACKCommand(short messageOpcode) {
        super((short) 12);
        this.messageOpcode = messageOpcode;
    }

    public ACKCommand(short messageOpcode, String ackMessage) {
        super((short) 12);
        this.messageOpcode = messageOpcode;
        this.ackMessage = ackMessage;

    }

}
