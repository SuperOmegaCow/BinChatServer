package binchat.protocol.packet;

import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class Kick extends DefinedPacket {

    private String name;
    private String message;

    public Kick() {

    }

    public Kick(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public void read(ByteBuf buf) {
        this.name = readString(buf);
        this.message = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(this.name, buf);
        writeString(this.message, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
