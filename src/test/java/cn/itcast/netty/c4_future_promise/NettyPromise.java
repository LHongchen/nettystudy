package cn.itcast.netty.c4_future_promise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author liaohongchen
 * @Description Promise相当于一个容器，可以用于存放各个线程中的结果，然后让其他线程去获取该结果
 * @date 2022/1/19 11:42
 */
public class NettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        //让eventLoop线程去放结果 main线程去取
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            promise.setSuccess(8998);
        }).start();


        System.out.println(Thread.currentThread().getName()+":"+promise.get());

        //当然 也可以通过添加监听器 去取

    }
}
