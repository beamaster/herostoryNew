package com.steam.game.handler;

import com.steam.game.caster.Broadcaster;
import com.steam.game.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户移动 指令处理器
 */
public class UserMoveToCmdHandler implements CmdHandler<MessageProtocol.UserMoveToCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, MessageProtocol.UserMoveToCmd cmd) {
        Integer userId = (Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        MessageProtocol.UserMoveToResult.Builder resultBuilder = MessageProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());

        MessageProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
