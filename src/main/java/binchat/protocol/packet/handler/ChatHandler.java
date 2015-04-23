package binchat.protocol.packet.handler;

import binchat.logic.BinChatManager;
import binchat.logic.UserConnection;
import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.packet.chat.Chat;
import binchat.protocol.packet.chat.Command;
import binchat.protocol.packet.chat.ConnectedClient;

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
        String arg1 = command.getMessage().substring(0, command.getMessage().indexOf(":"));
        if (arg1.equalsIgnoreCase("kick")) {
            String name = command.getMessage().substring(command.getMessage().indexOf(":"), occurrence(command.getMessage(), ':', 2) - 1);
            String reason = command.getMessage().substring(occurrence(command.getMessage(), ':', 2), command.getMessage().length());
            if (this.getUserConnection().isAdmin()) {
                this.getBinChatManager().kickPlayer(name, reason);
            } else {
                this.getUserConnection().sendPacket(new Chat("Console: You do not have permission to execute commands."));
            }
        } else if (arg1.equalsIgnoreCase("list")) {
            UserConnection user = this.getUserConnection();
            for (UserConnection userConnection : this.getBinChatManager().getUsers()) {
                if (!userConnection.equals(user)) {
                    this.getUserConnection().sendPacket(new ConnectedClient(userConnection.getName()));
                }
            }
        } else {
            if (this.getUserConnection().isAdmin()) {
                this.getBinChatManager().sendToAllBut(this.getUserConnection(), command);
            } else {
                this.getUserConnection().sendPacket(new Chat("Console: You do not have permission to execute commands."));
            }
        }
    }

    public int occurrence(String string, char c, int n) {
        int pos = string.indexOf(c, 0);
        while (n-- > 0 && pos != -1) {
            pos = string.indexOf(c, pos + 1);
        }
        return pos;
    }

}
