package com.steam.game.handler;

import com.steam.game.entity.UserEntity;
import com.steam.game.entity.UserManager;
import com.steam.game.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;

/**
 * 谁在场 指令处理器
 */
public class WhoElseIsHereCmdHandler implements CmdHandler<MessageProtocol.WhoElseIsHereCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, MessageProtocol.WhoElseIsHereCmd cmd) {
        MessageProtocol.WhoElseIsHereResult.Builder resultBuilder = MessageProtocol.WhoElseIsHereResult.newBuilder();
        for(UserEntity currentUser : UserManager.listUser()){
            if (null == currentUser) {
                continue;
            }

            MessageProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = MessageProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currentUser.getUserId());
            userInfoBuilder.setHeroAvatar(currentUser.getHeroAvatar());
            resultBuilder.addUserInfo(userInfoBuilder);
        }

        MessageProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}
