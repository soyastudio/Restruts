package soya.framework.restruts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RestrutsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestrutsApplication.class, args);
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String url = RestrutsApplication.class.getProtectionDomain().getCodeSource().getLocation().toString();
        System.out.println("-------------- " + url);
    }
}
