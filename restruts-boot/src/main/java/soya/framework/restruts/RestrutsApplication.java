package soya.framework.restruts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import soya.framework.action.ActionContext;
import soya.framework.action.patterns.ActionProxyFactory;
import soya.framework.restruts.service.Workshop;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

@SpringBootApplication
public class RestrutsApplication {
    public static final String WORKSPACE_HOME = "workspace.home";

    public static void main(String[] args) {
        init();
        SpringApplication.run(RestrutsApplication.class, args);
    }

    private static void init() {
        String url = RestrutsApplication.class.getProtectionDomain().getCodeSource().getLocation().toString();
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
        ApplicationContext applicationContext = event.getApplicationContext();

        ActionProxyFactory factory = applicationContext.getBean(ActionProxyFactory.class);
        Workshop workshop = factory.create(Workshop.class);

        String encoded = workshop.base64Encode();
        String decoded = workshop.base64Decode(encoded);

        System.out.println("-------------------- workspace.home = " + ActionContext.getInstance().getProperty("workspace.home"));
        System.out.println("---------------------- encoded: " + encoded);
        System.out.println("---------------------- decoded: " + decoded);
    }
}
