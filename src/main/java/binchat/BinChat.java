package binchat;

import binchat.logic.BinChatManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.swing.*;

public class BinChat extends JFrame {

    public BinChat() {
        this.setSize(800, 800);
        this.setTitle("BinChat Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        BinChat binChat = new BinChat();
        BinChatManager binChatManager = new BinChatManager();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(binChatManager);
            serverBootstrap.bind(60010).syncUninterruptibly().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
