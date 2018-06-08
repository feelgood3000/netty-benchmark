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
    private final AtomicLong count=new AtomicLong();
    public RequestEncoder(){
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, UdpRequest udpRequest, List<Object> list) throws Exception {
        udpRequest.setId(count.addAndGet(1));
        System.out.println(udpRequest.getId());
        byte[] data=KryoSerializer.encode(udpRequest);

        ByteBuf byteBuf=channelHandlerContext.alloc().heapBuffer();
        byteBuf.writeBytes(data);
        list.add(new DatagramPacket(byteBuf,new InetSocketAddress("127.0.0.1",8080)));
    }
}
