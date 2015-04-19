package binchat.protocol;

import binchat.logic.BinChatManager;
import binchat.logic.UserConnection;
import binchat.protocol.packet.Kick;
import binchat.protocol.packet.chat.Chat;
import binchat.protocol.packet.chat.Command;
import binchat.protocol.packet.chat.ConnectedClient;
import binchat.protocol.packet.handshake.Handshake;
import binchat.protocol.packet.login.*;

public class AbstractPacketHandler {

    private UserConnection userConnection;
    private BinChatManager binChatManager;

    public AbstractPacketHandler(UserConnection userConnection, BinChatManager binChatManager) {
        this.userConnection = userConnection;
        this.binChatManager = binChatManager;
    }

    public void handle(Handshake handshake) throws Exception {

    }

    public void handle(LoginStart loginStart) throws Exception {

    }

    public void handle(PasswordRequest passwordRequest) throws Exception {

    }

    public void handle(PasswordResponse passwordResponse) throws Exception {

    }

    public void handle(LoginSuccess loginSuccess) throws Exception {

    }

    public void handle(ConnectedClient connectedClient) throws Exception {

    }

    public void handle(Chat chat) throws Exception {

    }

    public void handle(Command command) throws Exception {

    }

    public void handle(Kick kick) throws Exception {

    }

    public UserConnection getUserConnection() {
        return userConnection;
    }

    public BinChatManager getBinChatManager() {
        return binChatManager;
    }
}
