package timeserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimerClientHandler  extends ChannelHandlerAdapter {
    private  byte[] req;

    public TimerClientHandler(){
        String request= "hello server "+ System.getProperty("line.separator");
        req = request .getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("向服务器发送数据");
        ByteBuf firstMessage;
        for (int i = 0; i < 100; i++) {
            try {
                firstMessage =  Unpooled.buffer(req.length);
                firstMessage.writeBytes(req);
                ctx.writeAndFlush(firstMessage);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;
        System.out.println("now is " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        ctx.close();
    }
}
