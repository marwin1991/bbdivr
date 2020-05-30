package pl.marwin1991.bbdivr.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.marwin1991.bbdivr"})
public class BbdivrEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BbdivrEngineApplication.class, args);
    }

}
