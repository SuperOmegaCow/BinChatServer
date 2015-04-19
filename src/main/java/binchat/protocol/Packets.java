package binchat.protocol;

import binchat.exception.BadPacketException;
import binchat.protocol.packet.chat.Chat;
import binchat.protocol.packet.chat.Command;
import binchat.protocol.packet.chat.ConnectedClient;
import binchat.protocol.packet.handshake.Handshake;
import binchat.protocol.packet.login.*;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.lang.reflect.Constructor;

public enum Packets {

    HANDSHAKE {
        {
            INBOUND.registerPacket(0, Handshake.class);
        }
    },

    LOGIN {
        {
            INBOUND.registerPacket(0, LoginStart.class);
            INBOUND.registerPacket(1, PasswordResponse.class);

            OUTBOUND.registerPacket(0, PasswordRequest.class);
            OUTBOUND.registerPacket(1, LoginSuccess.class);
        }
    },

    CHAT {
        {
            INBOUND.registerPacket(0, Chat.class);
            INBOUND.registerPacket(1, Command.class);

            OUTBOUND.registerPacket(0, Chat.class);
            OUTBOUND.registerPacket(1, Command.class);
            OUTBOUND.registerPacket(2, ConnectedClient.class);
        }
    };

    public static final int MAX_PACKET_ID = 0;

    public final ProtocolData INBOUND = new ProtocolData();
    public final ProtocolData OUTBOUND = new ProtocolData();

    public class ProtocolData {

        public boolean hasPacket(int id) {
            return id < MAX_PACKET_ID && packetConstructors[id] != null;
        }

        public final DefinedPacket createPacket(int id) {
            if (id > MAX_PACKET_ID) {
                throw new BadPacketException("Packet with id " + id + " outside of range ");
            }
            if (packetConstructors[id] == null) {
                throw new BadPacketException("No packet with id " + id);
            }
            try {
                return packetConstructors[id].newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new BadPacketException("Could not construct packet with id " + id, ex);
            }
        }

        private final TObjectIntMap<Class<? extends DefinedPacket>> packetMap = new TObjectIntHashMap<Class<? extends DefinedPacket>>(MAX_PACKET_ID);

        protected final void registerPacket(int id, Class<? extends DefinedPacket> packetClass) {
            try {
                packetConstructors[id] = packetClass.getDeclaredConstructor();
            } catch (NoSuchMethodException ex) {
                throw new BadPacketException("No NoArgsConstructor for packet class " + packetClass);
            }
            packetClasses[id] = packetClass;
            packetMap.put(packetClass, id);
        }

        protected final void unregisterPacket(int id) {
            packetMap.remove(packetClasses[id]);
            packetClasses[id] = null;
            packetConstructors[id] = null;
        }

        private final Class<? extends DefinedPacket>[] packetClasses = new Class[MAX_PACKET_ID];

        final int getId(Class<? extends DefinedPacket> packet) {
            return packetMap.get(packet);
        }

        private final Constructor<? extends DefinedPacket>[] packetConstructors = new Constructor[MAX_PACKET_ID];


    }

}
