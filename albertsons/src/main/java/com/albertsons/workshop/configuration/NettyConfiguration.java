package com.albertsons.workshop.configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class NettyConfiguration {

    ServerBootstrap serverBootstrap() {

        Object obj;
        Class<?> cls;
        ClassLoader classLoader;

        Thread thread = new Thread();

        Runtime runtime;
        System system;

        Process process;

        //
        ThreadPoolExecutor threadPoolExecutor;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });

        return new ServerBootstrap().group(new NioEventLoopGroup(), new NioEventLoopGroup());
    }

}
