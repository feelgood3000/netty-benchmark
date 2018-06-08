package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import com.benchmark.util.KryoSerializer;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;


import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:48
 */
@ChannelHandler.Sharable
public class RequestEncoder extends MessageToMessageEncoder<UdpRequest> {
    private final InetSocketAddress remoteAddress;
    private final AtomicLong count=new AtomicLong();
    public RequestEncoder(InetSocketAddress recieveAddress){
        this.remoteAddress=recieveAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, UdpRequest udpRequest, List<Object> list) throws Exception {
        byte[] data=KryoSerializer.encode(udpRequest);
        System.out.println(count.addAndGet(1));
        ByteBuf byteBuf=channelHandlerContext.alloc().buffer(data.length);
        byteBuf.writeBytes(data);
        list.add(new DatagramPacket(byteBuf,remoteAddress));
    }
}
