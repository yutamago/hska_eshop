package de.hska.eshopapi;


import static springfox.documentation.builders.PathSelectors.regex;

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
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.client.id}") private String clientId;
    @Value("${app.client.secret}") private String clientSecret;
    @Value("${info.build.name}") private String infoBuildName;
    @Value("${host.full.dns.auth.link}") private String authLink;

    @Value("${zuul.routes.composite-category.url}") private String compositeCategory;
    @Value("${zuul.routes.composite-product.url}") private String compositeProduct;
    @Value("${zuul.routes.composite-user.url}") private String compositeUser;
    @Value("${zuul.routes.core-category.url}") private String coreCategory;
    @Value("${zuul.routes.core-product.url}") private String coreProduct;
    @Value("${zuul.routes.core-user.url}") private String coreUser;
    @Value("${zuul.routes.core-role.url}") private String coreRole;


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
                .apis(RequestHandlerSelectors.basePackage("de.hska.eshopapi.controllers"))
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
                .title("eShop API")
                .description("eShop Facade API")
                .version("0.1")
                .build();
    }

    @Bean
    public RoutesUtil routesUtil() {
        final RoutesUtil ret = new RoutesUtil();

        ret.setCompositeCategory(this.compositeCategory);
        ret.setCompositeProduct(this.compositeProduct);
        ret.setCompositeUser(this.compositeUser);
        ret.setCoreCategory(this.coreCategory);
        ret.setCoreProduct(this.coreProduct);
        ret.setCoreUser(this.coreUser);
        ret.setCoreRole(this.coreRole);

        return ret;
    }

}