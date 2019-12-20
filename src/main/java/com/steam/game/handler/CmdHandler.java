package com.steam.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * 指令处理器接口
 * @param <TCmd>
 */
public interface CmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 指令处理
     * @param ctx
     * @param cmd
     */
    void handle(ChannelHandlerContext ctx, TCmd cmd);
}
