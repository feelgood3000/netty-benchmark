package com.benchmark.udp;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author mofei@yixia.com
 * @date 2018/6/11 下午2:15
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("参数错误 usage: sender/reciever");
            return;
        }
        if ("sender".equals(args[0])) {
            Sender.start(args);
        } else if ("reciever".equals(args[0])) {
            Reciever.start(args);
        }
    }
}
