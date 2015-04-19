package binchat.protocol.packet.handshake;

import binchat.protocol.AbstractPacketHandler;
import binchat.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class Handshake extends DefinedPacket {

    //Sever bound
    // 2 login

    private int nextState;

    public Handshake() {

    }

    public Handshake(int nextState) {
        this.nextState = nextState;
    }

    @Override
    public void read(ByteBuf buf) {
        this.nextState = readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeVarInt(this.nextState, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }

}
