package cn.itcast.netty.c2_eventloop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/10 17:58
 */

public class TestEventLoop {

    public static void main(String[] args) {

//        System.out.println(NettyRuntime.availableProcessors());

        EventLoopGroup group = new NioEventLoopGroup(2);//可处理io事件、普通任务、定时任务
//        EventLoopGroup group = new DefaultEventLoopGroup(2);//不可处理io事件，只能处理 普通任务、定时任务

        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //普通任务
        group.next().execute(()->{
            System.out.println(Thread.currentThread().getName() + ": ok");
        });

        //定时任务
        group.next().scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName() + ": ok");
        },0, 1, TimeUnit.SECONDS );

        //优雅的关闭
        group.shutdownGracefully();

    }
}
