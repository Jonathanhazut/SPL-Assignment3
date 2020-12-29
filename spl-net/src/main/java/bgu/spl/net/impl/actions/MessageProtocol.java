package bgu.spl.net.impl.actions;

import bgu.spl.net.api.MessagingProtocol;

public class MessageProtocol implements MessagingProtocol<Command> {


    private Command processedCommand;
    @Override
    public Command process(Command msg) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}