package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午11:03
 */
public class RevieveHandler extends SimpleChannelInboundHandler<UdpRequest> {
    private final AtomicLong count=new AtomicLong();
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UdpRequest udpRequest) throws Exception {
        System.out.println(count.addAndGet(1));
//        System.out.println(udpRequest);
    }
}
