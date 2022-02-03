package cn.itcast.netty.c4_future_promise;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/19 11:31
 */
public class NettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup executors = new NioEventLoopGroup();
        Future<Integer> future = executors.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return 90;
            }
        });

        System.out.println(Thread.currentThread().getName()+":get now:"+future.getNow());
        System.out.println(Thread.currentThread().getName()+":get :"+future.get());

        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                System.out.println(Thread.currentThread().getName()+":get now:"+future.getNow());
            }
        });

    }
}
