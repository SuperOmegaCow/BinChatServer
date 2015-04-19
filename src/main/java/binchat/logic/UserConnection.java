package binchat.logic;

import binchat.exception.BadPacketException;
import binchat.protocol.*;
import binchat.protocol.packet.Kick;
import binchat.protocol.packet.handler.ChatHandler;
import binchat.protocol.packet.handler.HandshakeHandler;
import binchat.protocol.packet.handler.LoginHandler;
import binchat.sql.Database;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutException;

import java.io.IOException;

public class UserConnection extends ChannelInboundHandlerAdapter {

    private final ChannelWrapper channel;
    private final BinChatManager binChatManager;
    private AbstractPacketHandler packetHandler;
    private String name;
    private boolean admin = false;
    private State state;

    public UserConnection(ChannelWrapper channel, BinChatManager binChatManager) {
        this.channel = channel;
        this.binChatManager = binChatManager;
        this.state = State.HANDSHAKE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        this.setProtocolState(state);
    }

    private void setProtocolState(State state) {
        ChannelPipeline pipeline = this.channel.getHandle().pipeline();
        if(state == State.HANDSHAKE) {
            this.setHandler(new HandshakeHandler(this, this.binChatManager));
            ((Decoder)pipeline.get("packet_decoder")).setProtocolData(Packets.HANDSHAKE);
            ((Encoder)pipeline.get("packet_encoder")).setProtocolData(Packets.HANDSHAKE);
        } else if(state == State.LOGIN) {
            this.setHandler(new LoginHandler(this, this.binChatManager));
            ((Decoder)pipeline.get("packet_decoder")).setProtocolData(Packets.LOGIN);
            ((Encoder)pipeline.get("packet_encoder")).setProtocolData(Packets.LOGIN);
        } else if(state == State.CHAT) {
            this.setHandler(new ChatHandler(this, this.binChatManager));
            ((Decoder)pipeline.get("packet_decoder")).setProtocolData(Packets.CHAT);
            ((Encoder)pipeline.get("packet_encoder")).setProtocolData(Packets.CHAT);
        }
    }

    private void disconnect() {
        this.channel.close();
        this.getBinChatManager().removeClient(this);
    }

    public BinChatManager getBinChatManager() {
        return binChatManager;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setHandler(AbstractPacketHandler handler) {
        this.packetHandler = handler;
    }

    public void disconnect(String reason) {
        this.getBinChatManager().sendToAll(new Kick(this.getName(), reason));
        this.disconnect();
    }

    public void sendPacket(DefinedPacket definedPacket) {
        this.channel.write(definedPacket);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.disconnect("Connection timed out");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketWrapper packet = (PacketWrapper) msg;
        try {
            if (packet.packet != null) {
                packet.packet.handle(packetHandler);
            }
        } finally {
            packet.trySingleRelease();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            if (cause instanceof ReadTimeoutException) {
                //TODO error logger remember the cause
            } else if (cause instanceof BadPacketException) {
                //TODO error logger remember the cause
            } else if (cause instanceof IOException) {
                //TODO error logger remember the cause
            } else {
                //TODO error logger remember the cause
            }
            ctx.close();
        }
    }


}
