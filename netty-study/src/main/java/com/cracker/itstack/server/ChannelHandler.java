package com.cracker.itstack.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;

public class ChannelHandler {

    //用于存放用户Channel信息，也可以建立map结构模拟不同的消息群
    @Getter
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
