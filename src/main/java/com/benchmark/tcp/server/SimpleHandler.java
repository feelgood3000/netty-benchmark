package com.benchmark.tcp.server;

import com.benchmark.pojo.UdpRequest;
import com.benchmark.util.ChannelHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mofei@yixia.com
 * @date 2018/6/11 上午11:33
 */
public class SimpleHandler extends SimpleChannelInboundHandler<UdpRequest> {
    private static final Logger logger=LoggerFactory.getLogger(SimpleHandler.class);
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UdpRequest msg) throws Exception {
        logger.info("{}",msg);
        ChannelHolder.group.add(ctx.channel());
    }
}
