package binchat.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public class ChannelWrapper {

    private final Channel channel;
    private volatile boolean closed;

    public ChannelWrapper(Channel channel) {
        this.channel = channel;
    }

    public boolean isClosed() {
        return closed;
    }


    public void write(Object packet) {
        if (!closed) {
            if (packet instanceof PacketWrapper) {
                ((PacketWrapper) packet).setReleased(true);
                channel.write(((PacketWrapper) packet).buf, channel.voidPromise());
            } else {
                channel.write(packet, channel.voidPromise());
            }
            channel.flush();
        }
    }

    public void close() {
        if (!closed) {
            closed = true;
            channel.flush();
            channel.close();
        }
    }

    public void addBefore(String baseName, String name, ChannelHandler handler) {
        channel.pipeline().flush();
        channel.pipeline().addBefore(baseName, name, handler);
    }

    public void addAfter(String baseName, String name, ChannelHandler handler) {
        channel.pipeline().flush();
        channel.pipeline().addAfter(baseName, name, handler);
    }

    public Channel getHandle() {
        return channel;
    }


}
