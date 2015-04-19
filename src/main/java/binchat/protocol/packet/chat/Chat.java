package binchat.protocol.packet.chat;

import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class Chat extends DefinedPacket {

    private String message;

    public Chat() {

    }

    public Chat(String message) {
        this.message = message;
    }

    @Override
    public void read(ByteBuf buf) {

    }

    @Override
    public void write(ByteBuf buf) {

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
}
