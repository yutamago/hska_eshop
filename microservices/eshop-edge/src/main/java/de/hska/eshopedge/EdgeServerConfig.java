package de.hska.eshopedge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
// @EnableEurekaClient
@SpringBootApplication
public class EdgeServerConfig {

    public static void main(String[] args) {
        SpringApplication.run(EdgeServerConfig.class, args);
    }

    @Bean
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter();
    }

}
