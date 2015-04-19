package binchat.protocol.packet.handler;

import binchat.logic.BinChatManager;
import binchat.logic.State;
import binchat.logic.UserConnection;
import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.packet.handshake.Handshake;

public class HandshakeHandler extends AbstractPacketHandler {

    public HandshakeHandler(UserConnection userConnection, BinChatManager binChatManager) {
        super(userConnection, binChatManager);
    }

    @Override
    public void handle(Handshake handshake) throws Exception {
        if (handshake.getNextState() == 2) {
            this.getUserConnection().setState(State.LOGIN);
        }
    }

}
