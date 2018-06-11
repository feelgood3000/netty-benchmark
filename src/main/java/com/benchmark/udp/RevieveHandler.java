package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午11:03
 */
public class RevieveHandler extends SimpleChannelInboundHandler<UdpRequest> {
    private static final Logger logger=LoggerFactory.getLogger(RevieveHandler.class);
    private final AtomicLong count=new AtomicLong();
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UdpRequest udpRequest) throws Exception {
        logger.info("{} 耗时:{}ms udpRequest:{}",count.addAndGet(1),udpRequest.getReceiveTime()-udpRequest.getSendTime(),udpRequest);
    }
}
