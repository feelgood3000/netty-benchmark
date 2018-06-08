package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import com.benchmark.util.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午10:37
 */
public class RequestDecoder extends MessageToMessageDecoder<DatagramPacket> {
    AtomicLong count=new AtomicLong(0);
    @Override
    protected void decode(
            ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        System.out.println(count.addAndGet(1));
//        ByteBuf data=datagramPacket.content();
//        byte[] dataByte=new byte[data.readableBytes()];
//        data.readBytes(dataByte);
//        UdpRequest udpRequest=KryoSerializer.deCode(dataByte,UdpRequest.class);
//        list.add(udpRequest);
    }
}
