package com.sand.server.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Just a dummy protocol mainly to show the ServerBootstrap being initialized.
 *
 * @author Abraham Menacherry
 */
@Component
@Qualifier("serverProtocolInitalizer")
public class ServerProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private StringDecoder stringDecoder;

    @Autowired
    private StringEncoder stringEncoder;

    @Autowired
    private ServerHandler serverHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", stringDecoder);
        pipeline.addLast(new ChannelHandler[]{serverHandler});
        pipeline.addLast("encoder", stringEncoder);
    }
}