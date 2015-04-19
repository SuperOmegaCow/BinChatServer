package binchat.protocol.packet.login;

import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class LoginSuccess extends DefinedPacket {

    public LoginSuccess() {

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

}
