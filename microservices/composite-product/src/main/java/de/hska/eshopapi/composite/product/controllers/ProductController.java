package de.hska.eshopapi.composite.product.controllers;

import de.hska.eshopapi.composite.product.RoutesUtil;
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

@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
public class ProductController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<ProductView>> ProductListTypeRef = new ParameterizedTypeReference<List<ProductView>>() {
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductView>> getProducts() throws URISyntaxException {
        URI uri = makeURI().build();

        return this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductListTypeRef);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public ResponseEntity<List<ProductView>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @Valid @RequestBody ProductSearchOptions searchOptions
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions);

        return this.restTemplate.exchange(uri, HttpMethod.GET, body, ProductListTypeRef);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProductView> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody(required = true)
                    Product product) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Product> body = new HttpEntity<>(product);

        return this.restTemplate.postForEntity(uri, body, ProductView.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{productId}")
    public ResponseEntity<ProductView> getProductById(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) throws URISyntaxException {
        URI uri = makeURI("id", productId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductView.class);
    }

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
