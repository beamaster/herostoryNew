package com.steam.game.caster;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Broadcaster {

    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 私有化构造器
     */
    private Broadcaster(){}

    /**
     * 添加信道
     * @param channel
     */
    public static void addChannel(Channel channel){
        _channelGroup.add(channel);

    }

    /**
     * 移除信道
     * @param channel
     */
    public static void removeChannel(Channel channel){
        _channelGroup.remove(channel);

    }

    /**
     * 消息广播
     * @param msg
     */
    public static void broadcast(Object msg){
        if(msg == null){
            return;
        }
        _channelGroup.writeAndFlush(msg);
    }

}
