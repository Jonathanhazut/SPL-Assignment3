package bgu.spl.net.impl.actions;

public class ACKCommand extends Command{
    private short messageOpcode;

    //constructor
    public ACKCommand(short messageOpcode) {
        super((short) 12);
        this.messageOpcode = messageOpcode;
    }
}
