package com.steam.game;

import com.steam.game.coder.MessageRecognizer;
import com.steam.game.coder.MessageDecoder;
import com.steam.game.coder.MessageEncoder;
import com.steam.game.handler.CmdHandlerFactory;
import com.steam.game.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class ServerMain {

    /**
     *
     * http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=123
     */
    public static void main(String[] args) {
        CmdHandlerFactory.init();
        MessageRecognizer.init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();// linked
        EventLoopGroup workerGroup = new NioEventLoopGroup();// read write

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                channel.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),// WebSocket 协议处理器, 在这里处理握手、ping、pong 等消息
                        new MessageDecoder(), //自定义消息解码器
                        new MessageEncoder(), //自定义消息编码器
                        new MessageHandler()// 自定义的消息处理器
                );
            }
        });



        try {
            ChannelFuture future = bootstrap.bind(12345).sync();

            if(future.isSuccess()){
                System.out.println("start successful...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
