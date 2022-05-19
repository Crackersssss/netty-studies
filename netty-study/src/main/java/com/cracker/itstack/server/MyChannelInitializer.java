package com.cracker.itstack.server;

import com.cracker.itstack.codec.MyDecoder;
import com.cracker.itstack.codec.MyEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(final SocketChannel channel) {
        //解码器
        //基于换行符
//        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
//        // 基于指定字符串【换行符，这样功能等同于LineBasedFrameDecoder】
//        // e.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, Delimiters.lineDelimiter()));
//        // 基于最大长度
//        // e.pipeline().addLast(new FixedLengthFrameDecoder(4));
//        // 解码转String，注意调整自己的编码格式GBK、UTF-8
//        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
//        //在管道中添加我们自己的接收数据实现方法
//        channel.pipeline().addLast(new MyServerHandler());
        //自定义解码器
        channel.pipeline().addLast(new MyDecoder());
        //自定义编码器
        channel.pipeline().addLast(new MyEncoder());
        //在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}
