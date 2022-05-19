package com.cracker.itstack.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据.
     *
     * @param ctx ***
     * @throws UnsupportedEncodingException ***
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws UnsupportedEncodingException {
        //当有客户端链接后，添加到channelGroup通信组
        ChannelHandler.getChannelGroup().add(ctx.channel());
        //日志信息
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + channel.localAddress().getHostString() + "\r\n";
        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据.
     *
     * @param ctx ***
     */
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
        //当有客户端退出后，从channelGroup中移除
        ChannelHandler.getChannelGroup().remove(ctx.channel());
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws UnsupportedEncodingException {
        //接受msg信息
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] msgByte = new byte[buf.readableBytes()];
//        buf.readBytes(msgByte);
//        System.out.println(new Date() + "接收到消息：");
//        System.out.println(new String(msgByte, Charset.forName("GBK")));

        //接收msg消息{与上一章节相比，此处已经不需要自己进行解码}
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "接收消息" + msg);

        //通知客户端链接消息发送成功
        String str = "服务器收到：" + new Date() + " " + msg + "\r\n";
//        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//        buf.writeBytes(str.getBytes("GBK"));
//        ctx.writeAndFlush(buf);
//        ctx.writeAndFlush(str);
        ChannelHandler.getChannelGroup().writeAndFlush(str);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接.
     * @param ctx ***
     * @param cause ***
     */
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
