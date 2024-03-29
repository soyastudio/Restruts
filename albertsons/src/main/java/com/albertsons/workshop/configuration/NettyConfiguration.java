package com.albertsons.workshop.configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import soya.framework.asm.visitors.DefaultClassVisitor;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class NettyConfiguration {

    @PostConstruct
    void init() throws Exception {

        URLClassLoader classLoader;

        System.out.println("========================== " + ClassLoader.getSystemClassLoader().getClass().getName());



        DispatcherServlet dispatcherServlet;
        AnnotationConfigWebApplicationContext ctx;

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("soya/framework/action/actions/reflect/AbcAction.class");

        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);

        inputStream.close();

        ClassReader classReader = new ClassReader(data);
        classReader.accept(new DefaultClassVisitor(), 0);
    }

    @Bean
    ServerBootstrap serverBootstrap() {

        ClassReader classReader;
        ClassVisitor classVisitor;

        ClassPathScanningCandidateComponentProvider provider;
        PathMatchingResourcePatternResolver resolver;

        ReferenceQueue referenceQueue;

        Object obj;
        Class<?> cls;
        ClassLoader classLoader;

        Thread thread = new Thread();

        Runtime runtime;
        System system;

        Process process;

        AtomicInteger atomicInteger;

        Math math;
        StrictMath strictMath;

        EventLoop eventLoop;

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
