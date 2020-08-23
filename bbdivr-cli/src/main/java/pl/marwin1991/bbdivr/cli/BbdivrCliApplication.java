package pl.marwin1991.bbdivr.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.marwin1991.bbdivr"})
public class BbdivrCliApplication {

    public static void main(String[] args) {
        SpringApplication.run(BbdivrCliApplication.class, args);
    }

}
