package de.hska.eshopapi.composite.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.product.ProductUtil;
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
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
public class ProductController {

    private final RestTemplate restTemplate;

    @Autowired
    public ProductController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APIProduct);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreProduct).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductView>> getProducts() throws URISyntaxException {
        URI uri = makeURI().build();

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductUtil.ProductListTypeRef).getBody();
        List<ProductView> productViews = ProductUtil.ExtendProducts(products, this.restTemplate);

        return new ResponseEntity<>(productViews, HttpStatus.OK);
    }


//
//    List<ProductView> extendProducts(List<Product> products) throws URISyntaxException {
//        List<ProductView> productViews = new ArrayList<>();
//        URI uri = makeAbsoluteURI(RoutesUtil.APICoreCategory, RoutesUtil.APICategory, "productIds").build();
//        List<UUID> uuids = products.stream().map(Product::getProductId).collect(Collectors.toList());
//        HttpEntity<List<UUID>> body = new HttpEntity<>(uuids);
//
//        List<Category> categories = this.restTemplate.exchange(uri, HttpMethod.POST, body, new ParameterizedTypeReference<List<Category>>() {}).getBody();
//
//        if (categories != null) {
//            products.forEach(p -> {
//                Optional<Category> productCategory = categories.stream().filter(c -> c.getCategoryId().equals(p.getCategoryId())).findFirst();
//                if(productCategory.isPresent()) {
//
//                }
//
//                productViews.add(ProductView.FromProduct(c, productsCategory));
//            });
//        }
//
//        return productViews;
//    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public ResponseEntity<List<ProductView>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @Valid @RequestBody ProductSearchOptions searchOptions
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions);

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, body, ProductUtil.ProductListTypeRef).getBody();
        List<ProductView> productViews = ProductUtil.ExtendProducts(products, this.restTemplate);

        return new ResponseEntity<>(productViews, HttpStatus.OK);
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
