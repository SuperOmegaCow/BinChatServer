package binchat.protocol.packet.handler;

import binchat.logic.BinChatManager;
import binchat.logic.State;
import binchat.logic.UserConnection;
import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.packet.chat.ConnectedClient;
import binchat.protocol.packet.login.LoginStart;
import binchat.protocol.packet.login.PasswordRequest;
import binchat.protocol.packet.login.PasswordResponse;

public class LoginHandler extends AbstractPacketHandler {

    public LoginHandler(UserConnection userConnection, BinChatManager binChatManager) {
        super(userConnection, binChatManager);
    }

    @Override
    public void handle(LoginStart loginStart) throws Exception {
        this.getUserConnection().setName(loginStart.getName());
        this.getUserConnection().sendPacket(new PasswordRequest());
    }

    @Override
    public void handle(PasswordResponse passwordResponse) throws Exception {

        if (BinChatManager.database.exists(this.getUserConnection().getName())) {
            if (!BinChatManager.database.getPassword(this.getUserConnection().getName()).equalsIgnoreCase(passwordResponse.getPassword())) {
                this.getUserConnection().disconnect("Password doesn't match your username");
                return;
            }
        } else {
            BinChatManager.database.add(this.getUserConnection().getName(), passwordResponse.getPassword());
        }

        this.getUserConnection().setState(State.CHAT);
        this.getUserConnection().setAdmin(BinChatManager.database.isAdmin(this.getUserConnection().getName()));
        this.getBinChatManager().clientConnected(this.getUserConnection());
        for(UserConnection userConnection : this.getBinChatManager().getUsers()) {
            if(!userConnection.equals(this.getUserConnection())) {
               this.getUserConnection().sendPacket(new ConnectedClient(userConnection.getName()));
            }
        }
    }


}
