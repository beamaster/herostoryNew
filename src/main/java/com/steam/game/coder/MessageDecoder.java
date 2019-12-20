package com.steam.game.coder;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ChannelInboundHandlerAdapter {


    /**
     * 重构
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if( !(msg instanceof BinaryWebSocketFrame)){
            return;
        }

        //WebSocket 二进制消息回通过HttpServerCodec 解码成 BinaryWebSocketFrame
        BinaryWebSocketFrame frame  = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //?为啥用short
        byteBuf.readShort();//读取消息的长度

        int msgCode = byteBuf.readShort();//读取消息的编号

//        Message.Builder msgBuilder = MessageRecognizer.getBuilder(msgCode);
        Message.Builder msgBuilder = MessageRecognizer.getBuilderByMsgCode(msgCode);
        if(null == msgBuilder){
            log.error("\n 消息无法识别，msgCode: {}",msgCode);
            return;
        }

        //拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        msgBuilder.clear();
        msgBuilder.mergeFrom(msgBody);

        Message currentMsg = msgBuilder.build();

        if (currentMsg != null) {
            ctx.fireChannelRead(currentMsg);
        }
    }


    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if( !(msg instanceof BinaryWebSocketFrame)){
            return;
        }

        //WebSocket 二进制消息回通过HttpServerCodec 解码成 BinaryWebSocketFrame
        BinaryWebSocketFrame frame  = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //?为啥用short
        byteBuf.readShort();//读取消息的长度

        int msgCode = byteBuf.readShort();//读取消息的编号

        //拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        *//************解码开始***********//*

        GeneratedMessageV3 cmd = null;

        switch (msgCode){
            //解码
            case MessageProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = MessageProtocol.UserEntryCmd.parseFrom(msgBody);
                break;

                //编码
            case MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = MessageProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;

             case MessageProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = MessageProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
        }

        if (cmd == null) {
            return;
        }
        ctx.fireChannelRead(cmd);//为何是ctx
    }*/
}
