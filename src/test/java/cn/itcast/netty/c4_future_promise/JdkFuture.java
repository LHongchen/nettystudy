package cn.itcast.netty.c4_future_promise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author liaohongchen
 * @Description
 * @date 2022/1/18 22:09
 */
@Slf4j
public class JdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "jdkFuture");
            }
        };

        //创建线程池
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(5,
                        10,
                        10,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(10),
                        factory);

        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                return 50;
            }
        });

        //通过阻塞的方式获取结果
        System.out.println(future.get());
        log.debug(future.get()+"kkk");

    }
}
