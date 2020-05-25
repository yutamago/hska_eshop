package de.hska.eshopapi.core.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@EnableEurekaClient
@EnableCircuitBreaker
@EnableSwagger2
@SpringBootApplication
public class SwaggerConfig {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerConfig.class, args);
    }

    private final ServletContext servletContext;

    @Autowired
    public SwaggerConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.hska.eshopapi.core.category.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Category CoreService API")
                .description("Category CoreService API")
                .version("0.1")
                .build();
    }


}