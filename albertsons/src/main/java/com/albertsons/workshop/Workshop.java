package com.albertsons.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import soya.framework.action.ActionContext;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

@SpringBootApplication
@EnableCaching
public class Workshop {
    public static final String WORKSPACE_HOME = "workspace.home";

    public static void main(String[] args) {
        //init();
        SpringApplication.run(Workshop.class, args);
    }

    private static void init() {
        String url = Workshop.class.getProtectionDomain().getCodeSource().getLocation().toString();
        if(url.indexOf("!") > 0) {
            url = url.substring(0, url.indexOf("!"));
        }

        if(url.startsWith("jar:")) {
            url = url.substring("jar:".length());
        }

        File file = Paths.get(URI.create(url)).toFile();
        File home = file.getParentFile().getParentFile();

        System.setProperty(WORKSPACE_HOME, home.getAbsolutePath());
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }
}
