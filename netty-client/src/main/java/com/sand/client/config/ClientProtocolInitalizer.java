package com.sand.client.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("clientProtocolInitalizer")
public class ClientProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    @Autowired
    StringDecoder stringDecoder;

    @Autowired
    StringEncoder stringEncoder;

    @Autowired
    ClientHandler clientHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast("ping", new IdleStateHandler(300L, 200L, 100L, TimeUnit.SECONDS));
        pipeline.addLast("decoder", stringDecoder);
        pipeline.addLast(new ChannelHandler[]{clientHandler});
        pipeline.addLast("encoder", stringEncoder);
    }
}