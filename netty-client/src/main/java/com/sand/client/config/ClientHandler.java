package com.sand.client.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Channel channel;

    private Object result;

    private Lock lock = new ReentrantLock();
    private Condition done;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    public Object sendRequest(Object msg, int timeout) {
//        MessageResponseFuture future = new MessageResponseFuture(request, timeout);
        byte[] bytes = "string".getBytes();
//        this.channel.writeAndFlush(msg);
        this.channel.writeAndFlush("string");
        return getResult(timeout);
    }

    private Object getResult(int timeout) {
        done = this.lock.newCondition();
        if (result == null) {
            long start = System.currentTimeMillis();
            lock.lock();

            try {
                while (result == null) {
                    this.done.await((long) timeout, TimeUnit.MILLISECONDS);
                    if (result != null || System.currentTimeMillis() - start > (long) timeout) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return result;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg;
        this.done.signal();
    }
}
