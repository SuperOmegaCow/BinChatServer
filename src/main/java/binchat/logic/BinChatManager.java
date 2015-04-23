package binchat.logic;

import binchat.protocol.*;
import binchat.protocol.packet.chat.ConnectedClient;
import binchat.sql.Database;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.util.ArrayList;
import java.util.List;

public class BinChatManager extends ChannelInitializer<Channel> {

    public static Database database = new Database("root", "binchat", "default", "3306", "localhost");
    private List<UserConnection> users = new ArrayList<UserConnection>();

    public BinChatManager() {
        try {
            database.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<UserConnection> getUsers() {
        return users;
    }

    public void sendToAllBut(UserConnection userConnection, DefinedPacket definedPacket) {
        for (UserConnection user : this.users) {
            if (!userConnection.equals(user))
                user.sendPacket(definedPacket);
        }
    }

    public void kickPlayer(String name, String reason) {

    }

    public void sendToAll(DefinedPacket definedPacket) {
        for (UserConnection user : this.users) {
            user.sendPacket(definedPacket);
        }
    }

    public void addClient(UserConnection userConnection) {
        this.users.add(userConnection);
    }

    public void removeClient(UserConnection userConnection) {
        this.users.remove(userConnection);
    }

    public void removeClient(String name) {
        for (UserConnection user : this.users) {
            if (user.getName().equalsIgnoreCase(name)) {
                this.users.remove(user);
                return;
            }
        }
    }

    public void clientConnected(UserConnection userConnection) {
        sendToAllBut(userConnection, new ConnectedClient(userConnection.getName()));
    }

    @Override
    public void initChannel(Channel ch) throws Exception {
        UserConnection userConnection = new UserConnection(new ChannelWrapper(ch), this);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frame_decoder", new FrameDecoder());
        pipeline.addLast("packet_decoder", new Decoder(Packets.HANDSHAKE));
        pipeline.addLast("frame_encoder", new FieldPrepender());
        pipeline.addLast("packet_handler", userConnection);
        this.addClient(userConnection);
    }

}
