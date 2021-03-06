package timeserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import timeserver.handler.TimerClientHandler;

public class TimerClient {

    public void connect(int port, String host){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new TimerClientHandler());
                        }

                    });
            System.out.println("发起异步链接");
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            System.out.println("异步链接完成,等待客户端链路关闭");
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("优雅关闭");
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
       new TimerClient().connect(8082,"127.0.0.1");
       //System.out.println(System.getProperty("line.separator"));
    }
}
