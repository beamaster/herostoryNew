package com.steam.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.protocol.MessageProtocol;

import java.util.HashMap;
import java.util.Map;

public class CmdHandlerFactory {
    /**
     * 处理器字典
     */
    private static Map<Class<?>, CmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap();

    private CmdHandlerFactory() {
    }

    /**
     * 初始化
     */
    public static void init() {
        _handlerMap.put(MessageProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        _handlerMap.put(MessageProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        _handlerMap.put(MessageProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }

    /**
     * 创建指令处理器工厂
     * @param msgClazz
     * @return
     */
    public static CmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz) {
        if( null == msgClazz){
            return null;
        }
        return _handlerMap.get(msgClazz);
    }

    /**
     * 传统创建指令处理器方式
     * @param msg
     * @return
     */
    public static CmdHandler<? extends GeneratedMessageV3> create(Object msg) {
        if (msg instanceof MessageProtocol.UserEntryCmd) {
            return new UserEntryCmdHandler();
        } else if (msg instanceof MessageProtocol.WhoElseIsHereCmd) {
            return new WhoElseIsHereCmdHandler();
        } else if(msg instanceof MessageProtocol.UserMoveToCmd ){
            return new UserMoveToCmdHandler() ;
        }else {
            return null;

        }

    }
}
