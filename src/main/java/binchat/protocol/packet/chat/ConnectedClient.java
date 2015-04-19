package binchat.protocol.packet.chat;

import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class ConnectedClient extends DefinedPacket {

    private String name;

    public ConnectedClient() {

    }

    public ConnectedClient(String name) {
        this.name = name;
    }

    @Override
    public void read(ByteBuf buf) {
        this.name = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(this.name, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
