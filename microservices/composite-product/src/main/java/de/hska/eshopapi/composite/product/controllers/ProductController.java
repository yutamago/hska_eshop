package de.hska.eshopapi.composite.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.product.util.ProductUtil;
import de.hska.eshopapi.composite.product.util.RoutesUtil;
import de.hska.eshopapi.composite.product.model.Category;
import de.hska.eshopapi.composite.product.model.Product;
import de.hska.eshopapi.composite.product.model.ProductSearchOptions;
import de.hska.eshopapi.composite.product.viewmodels.ProductView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static URIBuilder makeAbsoluteURI(String host, String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(host).setPathSegments(segments);
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
            @RequestBody Product product) throws URISyntaxException {
        URI addProductUrl = makeURI().build();
        HttpEntity<Product> body = new HttpEntity<>(product);
        ProductView productView = this.restTemplate.postForEntity(addProductUrl, body, ProductView.class).getBody();

        URI addProductToCategoryUrl = makeAbsoluteURI(
                RoutesUtil.APICoreCategory, RoutesUtil.APICategory, product.getCategoryId().toString(), "addProduct", productView.getProductId().toString())
                .build();
        this.restTemplate.put(addProductToCategoryUrl, body);

        return new ResponseEntity<>(productView, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    public ResponseEntity<String> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) throws URISyntaxException {
        URI getProductUrl = makeURI("id", productId.toString()).build();
        URI getCategoryByProductIdUrl = makeAbsoluteURI("http://core-category", "category", "productid", productId.toString()).build();
        URI deleteProductUrl = makeURI(productId.toString()).build();
        URI restoreProductUrl = makeURI("restore", productId.toString()).build();

        URI deleteProductInCategoryUrl = makeAbsoluteURI("http://core-category", "category", "deleteProductId", productId.toString()).build();


        Category category = null;
        Product product = null;

        try {
            product = this.restTemplate.getForEntity(getProductUrl, Product.class).getBody();
            List<Category> categories = this.restTemplate.exchange(getCategoryByProductIdUrl, HttpMethod.GET, null, ProductUtil.CategoryListTypeRef).getBody();
            if (categories.isEmpty()) {
                ResponseEntity<String> response = new ResponseEntity<>("Category on Product does not exist! Product: " + productId + ", Category: " + product.getCategoryId().toString(), HttpStatus.NOT_FOUND);
                return response;
            }
            category = categories.get(0);
        } catch (Exception ex) {
            ResponseEntity<String> response = new ResponseEntity<>("Product does not exist: " + productId, HttpStatus.NOT_FOUND);
            return response;
        }

        URI restoreProductInCategoryUrl = makeAbsoluteURI("http://core-category",
                "category",
                "restoreProductId", productId.toString(),
                "fromCategory", category.getCategoryId().toString()).build();

        try {
            this.restTemplate.delete(deleteProductUrl);
            this.restTemplate.delete(deleteProductInCategoryUrl);
        } catch (Exception ex) {
            this.restTemplate.put(restoreProductUrl, null);
            this.restTemplate.put(restoreProductInCategoryUrl, null);

            ResponseEntity<String> failResponse = new ResponseEntity<>(HttpStatus.CONFLICT);
            return failResponse;
        }

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }
}
