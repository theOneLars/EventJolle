package ch.zuehlke.hatch.sailingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
public class SailingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SailingServerApplication.class, args);
    }

}
