package com.steam.game.coder;


import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.steam.game.protocol.MessageProtocol;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息识别器
 */
@Slf4j
public class MessageRecognizer {

    private static final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap();
    private static final Map<Class<?>, Integer> _msgClazzAndMsgCodeMap = new HashMap();

    /**
     * 构建私有构造器
     */
    private MessageRecognizer(){}


    public static void init() {
        _msgCodeAndMsgBodyMap.put(MessageProtocol.MsgCode.USER_ENTRY_CMD_VALUE, MessageProtocol.UserEntryCmd.getDefaultInstance());
        _msgCodeAndMsgBodyMap.put(MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, MessageProtocol.WhoElseIsHereCmd.getDefaultInstance());
        _msgCodeAndMsgBodyMap.put(MessageProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, MessageProtocol.UserMoveToCmd.getDefaultInstance());

        _msgClazzAndMsgCodeMap.put(MessageProtocol.UserEntryResult.class, MessageProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(MessageProtocol.WhoElseIsHereResult.class, MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(MessageProtocol.UserMoveToResult.class, MessageProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(MessageProtocol.UserQuitResult.class, MessageProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }


    public static Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        } else {
            GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);
            return null == msg ? null : msg.newBuilderForType();
        }
    }

    public static Integer getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if (null == msgClazz) {
            return -1;
        } else {
            Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
            return msgCode != null ? msgCode.intValue() : -1;
        }
    }

    /**
     * 消息创建器
     * @param msgCode
     * @return
     */
    public static Message.Builder getBuilder(int msgCode){

        switch (msgCode){
            //解码
            case MessageProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                return MessageProtocol.UserEntryCmd.newBuilder();

            //编码
            case MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                return MessageProtocol.WhoElseIsHereCmd.newBuilder();

            case MessageProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                return MessageProtocol.UserMoveToCmd.newBuilder();

            default:
                return null;
        }
    }


    public static int getMsgCodeByMsgClazz(Object msg) {

        if(msg instanceof MessageProtocol.UserEntryResult){
            return MessageProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.WhoElseIsHereResult){
            return MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.UserMoveToResult){
            return MessageProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.UserQuitResult){
            return MessageProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        }else {
            log.info("无法识别的消息类型，myClazz：{}",msg.getClass().getName());
            return -1;
        }
    }
}
