package de.hska.eshopdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EshopDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopDiscoveryApplication.class, args);
    }

}
