package com.steam.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.caster.Broadcaster;
import com.steam.game.entity.UserEntity;
import com.steam.game.entity.UserManager;
import com.steam.game.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    /**
     * 用户离开广播消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Broadcaster.removeChannel(ctx.channel());

        Integer removeTargetUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(null == removeTargetUserId) return;

        UserManager.removeUser(removeTargetUserId);
        MessageProtocol.UserQuitResult.Builder resultBuilder = MessageProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(removeTargetUserId);

        MessageProtocol.UserQuitResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.info("\n get msg from client, myClazz:{} \n msg:{}", msg.getClass().getName(), msg);
        CmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
        if (null != cmdHandler) {
            cmdHandler.handle(ctx, cast(msg));
        }

    }

    /**
     * 消息对象转型，以适应编译
     * @param msg
     * @param <TCmd>
     * @return
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if(null == msg){
            return null;
        }else {
            return (TCmd) msg;
        }
    }


    /*
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {


        //入场之后群发
        if(msg instanceof MessageProtocol.UserEntryCmd) {

            //从指令对象中获取userId和heroAvatar
            MessageProtocol.UserEntryCmd cmd = (MessageProtocol.UserEntryCmd) msg;

            int userId = cmd.getUserId();
            String heroAvatar = cmd.getHeroAvatar();


            MessageProtocol.UserEntryResult.Builder resultBuilder = MessageProtocol.UserEntryResult.newBuilder();

            resultBuilder.setUserId(userId);
            resultBuilder.setHeroAvatar(heroAvatar);

            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(userId) ;
            userEntity.setHeroAvatar(heroAvatar);
            _userMap.put(userId,userEntity);


            //构建消息结果并发送
            MessageProtocol.UserEntryResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);
        }else if(msg instanceof MessageProtocol.WhoElseIsHereCmd){//还有谁？
            MessageProtocol.WhoElseIsHereResult.Builder resultBuilder = MessageProtocol.WhoElseIsHereResult.newBuilder();

            for(UserEntity currentUser:_userMap.values()){
                if (null == currentUser) return;

                MessageProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = MessageProtocol.WhoElseIsHereResult.UserInfo.newBuilder();

                userInfoBuilder.setUserId(currentUser.getUserId());
                userInfoBuilder.setHeroAvatar(currentUser.getHeroAvatar());
                resultBuilder.addUserInfo(userInfoBuilder);
            }

            MessageProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
            ctx.writeAndFlush(newResult);
        }else if(msg instanceof MessageProtocol.UserMoveToCmd){
            //获取当前移动用户Id
            Integer currentMoveToUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (null == currentMoveToUserId) return ;

            MessageProtocol.UserMoveToCmd cmd = (MessageProtocol.UserMoveToCmd)msg;
            MessageProtocol.UserMoveToResult.Builder resultBuilder = MessageProtocol.UserMoveToResult.newBuilder();
            resultBuilder.setMoveUserId(currentMoveToUserId);
            resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
            resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
            MessageProtocol.UserMoveToResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);
        }
    }
*/
    /*
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("get msg from client:"+msg);

        BinaryWebSocketFrame frame  = (BinaryWebSocketFrame) msg;
        ByteBuf buf = frame.content();

        byte[] byteArray = new byte[buf.readableBytes()];
        buf.readBytes(byteArray);

        System.out.println("get...");

        for(byte b:byteArray){
            System.out.print(b);
            System.out.print(", ");
        }
        System.out.println();
    }
    */
}
