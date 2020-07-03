package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.cachemodels.CategoryViewList;
import de.hska.eshopapi.cachemodels.ProductViewList;
import de.hska.eshopapi.exceptions.NotFoundInDatabaseException;
import de.hska.eshopapi.model.Product;
import de.hska.eshopapi.model.ProductSearchOptions;
import de.hska.eshopapi.viewmodels.CategoryView;
import de.hska.eshopapi.viewmodels.ProductView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
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

    private static final ParameterizedTypeReference<List<ProductView>> ProductListTypeRef = new ParameterizedTypeReference<List<ProductView>>() {
    };
    private Cache<Long, ProductViewList> productViewListCache;
    private Cache<UUID, ProductView> productViewCache;

    @Autowired
    public ProductController(RestTemplate restTemplate,
                             Cache<Long, ProductViewList> productViewListCache,
                             Cache<UUID, ProductView> productViewCache) {
        this.restTemplate = restTemplate;
        this.productViewListCache = productViewListCache;
        this.productViewCache = productViewCache;
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APIProduct);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICompositeProduct).setPathSegments(segments);
    }

    @HystrixCommand(fallbackMethod = "getProductsFromCache")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductView>> getProducts(
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI().build();
        List<ProductView> productViews;
        try {
            productViews = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), ProductListTypeRef).getBody();
            ProductViewList list = new ProductViewList(productViews);

            productViewListCache.put(0L, new ProductViewList(productViews));
            productViewCache.putAll(list.stream().collect(Collectors.toMap(ProductView::getProductId, v -> v)));
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(ProductViewList.class, ex);
        }

        return new ResponseEntity<>(productViews, HttpStatus.OK);
    }

    public ResponseEntity<List<ProductView>> getProductsFromCache(@RequestHeader HttpHeaders headers) {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<List<ProductView>>(this.productViewListCache.get(0L), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "getProductFromCache")
    @RequestMapping(method = RequestMethod.GET, path = "/{productId}")
    public ResponseEntity<ProductView> getProductById(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId") UUID productId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI(productId.toString()).build();
        ProductView productView;

        try {
            productView = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), ProductView.class).getBody();
            this.productViewCache.put(productView.getProductId(), productView);
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(ProductView.class, ex);
        }

        return new ResponseEntity<>(productView, HttpStatus.OK);
    }

    public ResponseEntity<ProductView> getProductFromCache(UUID productId, @RequestHeader HttpHeaders headers) {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.productViewCache.get(productId), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public ResponseEntity<List<ProductView>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @RequestBody ProductSearchOptions searchOptions,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI("search").build();
        HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions, headers);

        return this.restTemplate.exchange(uri, HttpMethod.POST, body, ProductListTypeRef);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProductView> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody Product product,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Product> body = new HttpEntity<>(product, headers);

        return this.restTemplate.postForEntity(uri, body, ProductView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    public ResponseEntity<ProductView> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId") UUID productId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI(productId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(null, headers), ProductView.class);
    }
}
