package bgu.spl.net.impl.actions;

import bgu.spl.net.api.MessageEncoderDecoder;

public class MessageEncDec implements MessageEncoderDecoder<Command> {

    //fields
    private byte[] message = new byte[1 << 10];
    private int len = 0;
    private byte[] opcodeBytes = new byte[2];
    private short opcode = 2; // change to -1

    //constructor


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
        //TODO - put in different assistant function to make our code more elegant
//        if (opcode == 5){
//
//        }
        return null;
    }

    @Override
    public byte[] encode(Command message) {
        return new byte[0];
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

}