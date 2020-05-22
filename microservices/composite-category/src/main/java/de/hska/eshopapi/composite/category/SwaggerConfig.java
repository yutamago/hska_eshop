package de.hska.eshopapi.composite.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.client.id}") private String clientId;
    @Value("${app.client.secret}") private String clientSecret;
    @Value("${info.build.name}") private String infoBuildName;
    @Value("${host.full.dns.auth.link}") private String authLink;

    @Value("${zuul.routes.core-category.url}") private String coreCategory;
    @Value("${zuul.routes.core-product.url}") private String coreProduct;


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
                .apis(RequestHandlerSelectors.basePackage("de.hska.eshopapi.composite.category.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
//                .pathProvider(new RelativePathProvider(servletContext) {
//                                  @Override
//                                  public String getApplicationBasePath() {
//                                      return "/api";
//                                  }
//                              });
//                .securitySchemes(Collections.singletonList(securitySchema()));


    }

    @Bean
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Category Composite API")
                .description("Category Composite API")
                .version("0.1")
                .build();
    }
}