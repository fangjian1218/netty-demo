package com.sand.client.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Component
public class NettyClientConfig {

    @Value("${netty.server.address}")
    private String address;
    @Value("${netty.server.port}")
    private Integer port;

    private EventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel channel;

    @Autowired
    private ClientProtocolInitalizer clientProtocolInitalizer;

    @PostConstruct
    public void start() {
        InetSocketAddress serverAddress = new InetSocketAddress(address, port);
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(clientProtocolInitalizer);
        try {
            ChannelFuture future = bootstrap.connect(address, port).sync();
            if (future.isSuccess()) {
                System.out.println("启动 Netty 成功");
            }
            channel = (SocketChannel) future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
