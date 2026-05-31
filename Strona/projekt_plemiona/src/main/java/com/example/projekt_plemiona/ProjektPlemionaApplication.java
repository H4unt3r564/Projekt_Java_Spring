package com.example.projekt_plemiona;

import com.example.projekt_plemiona.services.CustomUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjektPlemionaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjektPlemionaApplication.class, args);
    }

}
