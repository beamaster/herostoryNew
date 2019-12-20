package com.steam.game.processor;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


@Slf4j
public class MessageMainProcessor {

    private MessageMainProcessor(){}

    private static class InnerMessageProcessor{
        private static final MessageMainProcessor INSTANCE = new MessageMainProcessor();
    }

    ExecutorService local = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread mainThread = new Thread(r);
            mainThread.setName("MainThreadProcessor");
            return mainThread;
        }
    });

    public static MessageMainProcessor getInstance(){
        return InnerMessageProcessor.INSTANCE;
    }

    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg ){

    }
}
