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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed("product.read")
    public ResponseEntity<List<ProductView>> getProducts(
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI().build();

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), ProductUtil.ProductListTypeRef).getBody();
        List<ProductView> productViews = ProductUtil.extendProducts(products, this.restTemplate, headers);

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
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    @RolesAllowed("product.read")
    public ResponseEntity<List<ProductView>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @RequestBody ProductSearchOptions searchOptions,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI("search").build();
        HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions, headers);

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.POST, body, ProductUtil.ProductListTypeRef).getBody();
        List<ProductView> productViews = ProductUtil.extendProducts(products, this.restTemplate, headers);

        return new ResponseEntity<>(productViews, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/{productId}")
    @RolesAllowed("product.read")
    public ResponseEntity<ProductView> getProductById(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId") UUID productId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI("id", productId.toString()).build();
        ResponseEntity<Product> responseProduct = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), Product.class);
        Product product = responseProduct.getBody();
        if(product == null || responseProduct.getStatusCode() == HttpStatus.NOT_FOUND)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        URI catUrl = makeAbsoluteURI(RoutesUtil.APICoreCategory, RoutesUtil.APICategory, "id", product.getCategoryId().toString()).build();
        ResponseEntity<Category> responseCategory = this.restTemplate.exchange(catUrl, HttpMethod.GET, new HttpEntity<>(null, headers), Category.class);
        Category category = responseCategory.getBody();
        if(category == null || responseCategory.getStatusCode() == HttpStatus.NOT_FOUND)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(responseCategory.getStatusCode().isError())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ProductView productView = ProductView.FromProduct(product, category);

        return new ResponseEntity<>(productView, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("product.write")
    public ResponseEntity<ProductView> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody Product product,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI addProductUrl = makeURI().build();
        HttpEntity<Product> body = new HttpEntity<>(product, headers);
        Product newProduct = this.restTemplate.postForEntity(addProductUrl, body, Product.class).getBody();

        URI addProductToCategoryUrl = makeAbsoluteURI(
                RoutesUtil.APICoreCategory, RoutesUtil.APICategory, product.getCategoryId().toString(), "addProduct", newProduct.getProductId().toString())
                .build();
        ResponseEntity<Category> responseCategory = this.restTemplate.exchange(addProductToCategoryUrl, HttpMethod.PUT, new HttpEntity<>(null, headers), Category.class);
        Category updatedCategory = responseCategory.getBody();

        if(updatedCategory == null || responseCategory.getStatusCode() == HttpStatus.NOT_FOUND)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ProductView newProductView = ProductView.FromProduct(newProduct, updatedCategory);

        return new ResponseEntity<>(newProductView, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    @RolesAllowed("product.write")
    public ResponseEntity<String> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId") UUID productId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI getProductUrl = makeURI("id", productId.toString()).build();
        URI getCategoryByProductIdUrl = makeAbsoluteURI("http://core-category", "category", "productid", productId.toString()).build();
        URI deleteProductUrl = makeURI(productId.toString()).build();
        URI deleteProductInCategoryUrl = makeAbsoluteURI("http://core-category", "category", "deleteProductId", productId.toString()).build();


        Product product;

        try {
            product = this.restTemplate.exchange(getProductUrl, HttpMethod.GET, new HttpEntity<>(null, headers), Product.class).getBody();
            Category[] categories = this.restTemplate.exchange(getCategoryByProductIdUrl, HttpMethod.GET, new HttpEntity<>(null, headers), Category[].class).getBody();
            if (categories.length == 0) {
                ResponseEntity<String> response = new ResponseEntity<>("Category on Product does not exist! Product: " + productId + ", Category: " + product.getCategoryId().toString(), HttpStatus.NOT_FOUND);
                return response;
            }
        } catch (Exception ex) {
            ResponseEntity<String> response = new ResponseEntity<>("Product does not exist: " + productId, HttpStatus.NOT_FOUND);
            return response;
        }

        this.restTemplate.exchange(deleteProductInCategoryUrl, HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);
        this.restTemplate.exchange(deleteProductUrl, HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }
}
