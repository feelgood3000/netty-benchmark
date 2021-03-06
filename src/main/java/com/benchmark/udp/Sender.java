package com.benchmark.udp;

import com.benchmark.pojo.UdpRequest;
import com.benchmark.util.KryoSerializer;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:42
 */
public class Sender {
    private static final Logger logger=LoggerFactory.getLogger(Sender.class);


    private static  EventLoopGroup group;
    private static  Bootstrap bootstrap;
    private static Channel channel;
    private static AtomicLong count=new AtomicLong(0);
    private static RateLimiter limiter=null;

    static CountDownLatch countDownLatch;

    public Sender() throws InterruptedException {

    }

    public static void send(String remoteIp,int port,int packageSize) throws InterruptedException {
        byte[] data=new byte[packageSize];
        byte[] dataObj=KryoSerializer.encode(new UdpRequest(count.addAndGet(1),System.currentTimeMillis(),data));
        ByteBuf buffer=channel.alloc().heapBuffer();
        buffer.writeBytes(dataObj);
        io.netty.channel.socket.DatagramPacket datagramPacket = new io.netty.channel.socket.DatagramPacket
                (buffer, new InetSocketAddress(remoteIp, port));
        channel.writeAndFlush(datagramPacket);
    }

    public static void stop(){
        group.shutdownGracefully();
    }

    private static Executor executor=null;


    public static void start(String[] args) throws InterruptedException {
        if (args.length!=7){
            System.out.println("args error usage:sender ip port threadNum packagePerThread packageSize rateLimiter");
            System.exit(1);
        }
        final String ip=args[1];
        final int port=NumberUtils.toInt(args[2]);
        final int threadNum=NumberUtils.toInt(args[3]);
        final int packagePerThread=NumberUtils.toInt(args[4]);
        final int packageSize=NumberUtils.toInt(args[5]);
        final long rateLimiter=NumberUtils.toLong(args[6]);
        limiter=RateLimiter.create(rateLimiter);
        Thread.sleep(3000);
        countDownLatch=new CountDownLatch(threadNum);

        group=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new RevieveHandler());
        try {
            channel=bootstrap.bind(0).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        final long startTime=System.currentTimeMillis();
        executor=Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            countDownLatch.countDown();
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < packagePerThread; i++) {
                        try {
                            limiter.acquire();
                            Sender.send(ip,port,packageSize);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    long end=System.currentTimeMillis();
                    logger.info("花费{}ms,threadNum:{},packagePerThread:{},packageSize:{}",(end-startTime),threadNum,packagePerThread,packageSize);
                }
            });
        }

        
    }
}
