package de.hska.eshopapi.composite.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.product.RoutesUtil;
import de.hska.eshopapi.composite.product.model.Category;
import de.hska.eshopapi.composite.product.model.Product;
import de.hska.eshopapi.composite.product.model.ProductSearchOptions;
import de.hska.eshopapi.composite.product.viewmodels.ProductView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
public class ProductController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<Product>> ProductListTypeRef = new ParameterizedTypeReference<List<Product>>() {
    };
    private static final ParameterizedTypeReference<List<Category>> CategoryListTypeRef = new ParameterizedTypeReference<List<Category>>() {
    };

    @Autowired
    public ProductController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICoreProduct);
        segments.add(RoutesUtil.APIProduct);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.Localhost).setPathSegments(segments);
    }
    private static URIBuilder makeCategoryURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICoreCategory);
        segments.add(RoutesUtil.APICategory);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.Localhost).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductView>> getProducts() throws URISyntaxException {
        URI uri = makeURI().build();

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductListTypeRef).getBody();
        List<ProductView> productViews = extendProducts(products);

        return new ResponseEntity<>(productViews, HttpStatus.OK);
    }

    /*
    * @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserView>> getUsers() {
        List<User> users = this.userDAO.findAll();
        List<UserView> userViews = new ArrayList<>(users.size());

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Role role = null;
            if (user.getRoleId() != null && roleDAO.existsById(user.getRoleId())) {
                role = roleDAO.getOne(user.getRoleId());
            }
            userViews.set(i, UserView.FromUser(user, role));
        }

        return new ResponseEntity<>(userViews, HttpStatus.OK);
    }
    * */

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public ResponseEntity<List<ProductView>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @Valid @RequestBody ProductSearchOptions searchOptions
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions);

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, body, ProductListTypeRef).getBody();
        List<ProductView> productViews = extendProducts(products);

        return new ResponseEntity<>(productViews, HttpStatus.OK);
    }

    private List<ProductView> extendProducts(List<Product> products) throws URISyntaxException {
        URI uri = makeCategoryURI("multiple-id").build();
        List<ProductView> productViews = new ArrayList<>();
        HttpEntity<List<UUID>> categoryIdsBody = new HttpEntity<>(products.stream().map(Product::getCategoryId).collect(Collectors.toList()));
        List<Category> categories = this.restTemplate.exchange(uri, HttpMethod.POST, categoryIdsBody, CategoryListTypeRef).getBody();


        products.forEach(product -> {
            Category productCategory = null;
            for (Category category : categories) {
                if (category.getCategoryId().equals(product.getCategoryId())) {
                    productCategory = category;
                    break;
                }
            }
            productViews.add(ProductView.FromProduct(product, productCategory));
        });

        return productViews;
    }

    /*
    * @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserView>> getUsers() {
        List<User> users = this.userDAO.findAll();
        List<UserView> userViews = new ArrayList<>(users.size());

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Role role = null;
            if (user.getRoleId() != null && roleDAO.existsById(user.getRoleId())) {
                role = roleDAO.getOne(user.getRoleId());
            }
            userViews.set(i, UserView.FromUser(user, role));
        }

        return new ResponseEntity<>(userViews, HttpStatus.OK);
    }
    * */

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProductView> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody(required = true)
                    Product product) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Product> body = new HttpEntity<>(product);

        return this.restTemplate.postForEntity(uri, body, ProductView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/{productId}")
    public ResponseEntity<ProductView> getProductById(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) throws URISyntaxException {
        URI uri = makeURI("id", productId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    public ResponseEntity<ProductView> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) throws URISyntaxException {
        URI uri = makeURI(productId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, null, ProductView.class);
    }

}
