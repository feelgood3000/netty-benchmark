package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import com.benchmark.util.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:42
 */
public class Sender {
    private static  EventLoopGroup group;
    private static  Bootstrap bootstrap;
    private static Channel channel;

    static CountDownLatch countDownLatch=new CountDownLatch(5);

    public Sender(InetSocketAddress address) throws InterruptedException {

    }

    public static void send(String remoteIp,int port) throws InterruptedException {
        byte[] data=new byte[50];
        byte[] dataObj=KryoSerializer.encode(new UdpRequest(System.currentTimeMillis(),data));
        ByteBuf buffer=channel.alloc().heapBuffer();
        buffer.writeBytes(dataObj);
        io.netty.channel.socket.DatagramPacket datagramPacket = new io.netty.channel.socket.DatagramPacket
                (buffer, new InetSocketAddress(remoteIp, port));
        channel.writeAndFlush(datagramPacket);
    }

    public static void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args){
        if (args.length!=2){
            System.out.println("args error usage: ip port");
            System.exit(1);
        }
        InetSocketAddress address=new InetSocketAddress(args[0],Integer.parseInt(args[1]));
        group=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new RevieveHandler());
        try {
            channel=bootstrap.bind(0).sync().channel();
            for (int i = 0; i < 100; i++) {
                Sender.send(args[0],NumberUtils.toInt(args[1]));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Sender.stop();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
