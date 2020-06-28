package de.hska.eshopapi.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.model.Category;
import de.hska.eshopapi.model.Product;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.model.User;
import io.swagger.annotations.Api;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final RestTemplate restTemplate;

    @Autowired
    public DevController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static URIBuilder makeURI(String host, String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>(Arrays.asList(path));
        return new URIBuilder(host).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DevModelAll> setup(
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        ResponseEntity<UserRoleDevModel> userDevModel = this.restTemplate.exchange(makeURI(RoutesUtil.APICoreUser, RoutesUtil.APIDev).build(), HttpMethod.POST, new HttpEntity<>(null, headers), UserRoleDevModel.class);
        ResponseEntity<CatProdDevModel> catsDevModel = this.restTemplate
                .exchange(makeURI(RoutesUtil.APICompositeCategory, RoutesUtil.APIDev).build(), HttpMethod.POST, new HttpEntity<>(null, headers), CatProdDevModel.class);

        DevModelAll devModelAll = new DevModelAll();
        devModelAll.categories = catsDevModel.getBody().getCategories();
        devModelAll.products = catsDevModel.getBody().getProducts();
        devModelAll.users = userDevModel.getBody().getUsers();
        devModelAll.roles = userDevModel.getBody().getRoles();

        return new ResponseEntity<>(devModelAll, HttpStatus.OK);
    }

    public static class DevModelAll {
        @JsonProperty private List<Category> categories;
        @JsonProperty private List<Product> products;
        @JsonProperty private List<Role> roles;
        @JsonProperty private List<User> users;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public static class CatProdDevModel {
        @JsonProperty private List<Category> categories;
        @JsonProperty private List<Product> products;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public static class UserRoleDevModel {
        @JsonProperty private List<User> users;
        @JsonProperty private List<Role> roles;

        public List<User> getUsers() {
            return users;
        }

        public void setCategories(List<User> users) {
            this.users = users;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
    }
}
