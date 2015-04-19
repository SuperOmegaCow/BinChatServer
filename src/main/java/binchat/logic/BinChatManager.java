package binchat.logic;

import binchat.protocol.*;
import binchat.protocol.packet.chat.ConnectedClient;
import binchat.sql.Database;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BinChatManager extends ChannelInitializer<Channel> {

    public static Database database = new Database("root", "binchat", "default", "3306", "localhost");
    private Connection connection;
    private List<UserConnection> users = new ArrayList<UserConnection>();

    public BinChatManager() {
        try {
            this.connection = database.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAllBut(UserConnection userConnection, DefinedPacket definedPacket) {
        for (UserConnection user : this.users) {
            if (userConnection != user)
                user.sendPacket(definedPacket);
        }
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
        pipeline.addLast("packet_decoder", new Encoder(Packets.LOGIN));
        pipeline.addLast("packet_handler", userConnection);
        this.addClient(userConnection);
    }

}
