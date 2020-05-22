package de.hska.eshopapi.composite.product.controllers;

import de.hska.eshopapi.composite.product.RoutesUtil;
import de.hska.eshopapi.composite.product.model.Category;
import de.hska.eshopapi.composite.product.viewmodels.CategoryView;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/category", name = "Category", produces = {"application/json"})
@Api(tags = "Category")
public class CategoryController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<CategoryView>> CategoryListTypeRef = new ParameterizedTypeReference<List<CategoryView>>() {
    };

    @Autowired
    public CategoryController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICoreCategory);
        segments.add(RoutesUtil.APICategory);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.Localhost).setPathSegments(segments);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryView>> getCategories() throws URISyntaxException {
        URI uri = makeURI().build();

        return this.restTemplate.exchange(uri, HttpMethod.GET, null, CategoryListTypeRef);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryView> addCategory(
            @ApiParam(value = "Category", required = true)
            @RequestBody(required = true)
                    Category category) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Category> body = new HttpEntity<>(category);

        return this.restTemplate.postForEntity(uri, body, CategoryView.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{categoryId}")
    public ResponseEntity<CategoryView> getCategoryById(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) throws URISyntaxException {
        URI uri = makeURI("id", categoryId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, CategoryView.class);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    public ResponseEntity<CategoryView> deleteCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) throws URISyntaxException {
        URI uri = makeURI(categoryId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, null, CategoryView.class);
    }
}
