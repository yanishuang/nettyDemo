package echo;

import echo.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {

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
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));//第一个参数，单条消息最大长度，没有找到分隔符，抛出TooLongframeException，第二个参数为分隔符
                            socketChannel.pipeline().addLast(new StringDecoder());// 自动把字节码装换为字符串
                            socketChannel.pipeline().addLast(new EchoClientHandler());
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
       new EchoClient().connect(8082,"127.0.0.1");
       //System.out.println(System.getProperty("line.separator"));
    }
}
