package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by simon.liu on 2017/1/4.
 */
public class Client {
    private EventLoopGroup workEventLoopGroup;
    private Channel channel;
    private static String ip = "192.168.30.70";
    private static int port = 8888;

    /**
     * run
     */
    public void run() {
        Bootstrap client = new Bootstrap();
        workEventLoopGroup = new NioEventLoopGroup();
        client.group(workEventLoopGroup);
        client.option(ChannelOption.SO_KEEPALIVE, true);
        client.channel(NioSocketChannel.class);
        client.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(new StringDecoder());
                channelPipeline.addLast(new StringEncoder());
            }
        });
        try {
            ChannelFuture f = client.connect(ip, port).sync();
//            System.out.println("connect to server : " + ip + " : " + port);
            channel = f.channel();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("connect to server :" + channel.remoteAddress() + " successful!");
                    }
                }
            });
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        Client testClient = new Client();
        testClient.run();
    }
}
