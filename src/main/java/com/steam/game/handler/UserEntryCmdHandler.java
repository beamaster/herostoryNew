package com.steam.game.handler;

import com.steam.game.caster.Broadcaster;
import com.steam.game.entity.UserEntity;
import com.steam.game.entity.UserManager;
import com.steam.game.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserEntryCmdHandler implements CmdHandler<MessageProtocol.UserEntryCmd>{

    @Override
    public void handle(ChannelHandlerContext ctx, MessageProtocol.UserEntryCmd cmd) {

        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();
        MessageProtocol.UserEntryResult.Builder resultBuilder = MessageProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        //将用户加入字典
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setHeroAvatar(heroAvatar);
        UserManager.addUser(userEntity);

        //将用户ID附着到 Channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        //构建结果并发送
        MessageProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
