package com.benchmark.util;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author mofei@yixia.com
 * @date 2018/6/11 上午11:35
 */
public class ChannelHolder {
    public static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
