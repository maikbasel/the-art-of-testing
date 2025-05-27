package io.squer.theartoftesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TheArtOfTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheArtOfTestingApplication.class, args);
    }

}
