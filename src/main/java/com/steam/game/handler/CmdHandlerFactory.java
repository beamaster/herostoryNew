package com.steam.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.protocol.MessageProtocol;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.*;

@Slf4j
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
        //获取 {MessageProtocol}类 下所有定义的内部类
        Class<?>[] innerClassArray = MessageProtocol.class.getDeclaredClasses();


        Class<?> clazz = CmdHandler.class;



        for (Class<?> innerClazz : innerClassArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }

            String clazzName = innerClazz.getSimpleName().toLowerCase();

            Reflections reflections = new Reflections(CmdHandlerFactory.class.getPackage().getName());

            Set<Class<? extends CmdHandler>> clazzSets = reflections.getSubTypesOf(CmdHandler.class);

            for (Class<? extends CmdHandler> clazzSet : clazzSets) {
                String cmdHandlerName = clazzSet.getName().toLowerCase();
                //log.info("\n clazzName: {}",cmdHandlerName);
                if(cmdHandlerName.contains(clazzName)){

                    try {
                        _handlerMap.put(innerClazz,clazzSet.newInstance() );

                        log.debug("info");
                        log.info("{}<=2=>{}",innerClazz.getName(),cmdHandlerName);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }



            ServiceLoader<CmdHandler> loaders = ServiceLoader.load(CmdHandler.class);
            Iterator<? extends CmdHandler> iterator = loaders.iterator();
            while(iterator.hasNext()){
//                iterator.next();
                String cmdName = ((CmdHandler)iterator.next()).getClass().getName().toLowerCase();

            }
        }

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
