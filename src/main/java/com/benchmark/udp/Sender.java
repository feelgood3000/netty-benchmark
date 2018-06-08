package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:42
 */
public class Sender {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private Channel channel;

    static CountDownLatch countDownLatch=new CountDownLatch(5);

    public Sender(InetSocketAddress address) throws InterruptedException {
        group=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new RequestEncoder(address));
        channel=bootstrap.bind(0).sync().channel();
    }

    public void run() throws InterruptedException {
        byte[] data=new byte[30];
        channel.writeAndFlush(new UdpRequest(System.currentTimeMillis(),data));
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args){
        if (args.length!=2){
            System.out.println("args error usage: ip port");
            System.exit(1);
        }
        InetSocketAddress address=new InetSocketAddress(args[0],Integer.parseInt(args[1]));
        Sender sender=null;
        try {
            sender=new Sender(address);
            for (int i = 0; i < 100; i++) {
                sender.run();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            sender.stop();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
