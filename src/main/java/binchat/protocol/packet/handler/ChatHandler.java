package binchat.protocol.packet.handler;

import binchat.logic.BinChatManager;
import binchat.logic.UserConnection;
import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.packet.chat.Chat;
import binchat.protocol.packet.chat.Command;

public class ChatHandler extends AbstractPacketHandler {

    public ChatHandler(UserConnection userConnection, BinChatManager binChatManager) {
        super(userConnection, binChatManager);
    }

    @Override
    public void handle(Chat chat) {
        this.getBinChatManager().sendToAllBut(this.getUserConnection(), chat);
    }

    @Override
    public void handle(Command command) {
        if(this.getUserConnection().isAdmin()) {
            this.getBinChatManager().sendToAllBut(this.getUserConnection(), command);
        } else {
            this.getUserConnection().sendPacket(new Chat("Console:You do not have permission to execute commands."));
        }
    }

}
