package timeserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.Date;


public class TimerServerHandler extends ChannelHandlerAdapter {

    private int counter;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("the time serverreceiver order : " + body);
        String currentTime = "server response :".concat( ++ counter + "次：").concat(body).concat(System.getProperty("line.separator"));
         currentTime = currentTime + System.getProperty("line.separator");
        ctx.write(Unpooled.copiedBuffer(currentTime.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
