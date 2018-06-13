package com.benchmark.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


import java.net.InetSocketAddress;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午10:58
 */
public class Reciever {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public Reciever(InetSocketAddress address){
        group=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .option(ChannelOption.SO_RCVBUF,10*1024 * 1024)
                .handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pipeline=channel.pipeline();
                pipeline.addLast(new RequestDecoder());
                pipeline.addLast(new RevieveHandler());
            }
        }).localAddress(address);
    }

    public Channel bind(){
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void start(String[] args){
        if (args.length!=2){
            System.out.println("args error usage:reciver port");
            System.exit(1);
        }
        InetSocketAddress address=new InetSocketAddress(Integer.parseInt(args[1]));
        Reciever reciever=new Reciever(address);

        try {
            Channel channel= reciever.bind();
            System.out.println("Reciver is running");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            reciever.stop();
        }
    }

}
