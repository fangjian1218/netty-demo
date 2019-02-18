package com.sand.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Configuration
public class NettyServerConfig {
    @Value("${netty.boss.thread.count}")
    private int bossCount;

    @Value("${netty.worker.thread.count}")
    private int workerCount;

    @Value("${netty.tcp.port}")
    private int tcpPort;

    @Value("${netty.so.keepalive}")
    private boolean keepAlive;

    @Value("${netty.so.backlog}")
    private int backlog;

    private EventLoopGroup boss;
    private EventLoopGroup work;

    @Autowired
    @Qualifier("serverProtocolInitalizer")
    private ServerProtocolInitalizer serverProtocolInitalizer;


    @PostConstruct
    public void start() {
        if (boss == null)
            boss = new NioEventLoopGroup(bossCount);
        if (work == null)
            work = new NioEventLoopGroup(workerCount);

        InetSocketAddress serverAddress = new InetSocketAddress(tcpPort);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(serverAddress)
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(serverProtocolInitalizer);
        try {
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                System.out.println("启动 Netty 成功");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        System.out.println("关闭 Netty 成功");
    }
}