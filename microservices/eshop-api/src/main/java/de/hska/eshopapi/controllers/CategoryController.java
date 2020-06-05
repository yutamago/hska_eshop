package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.cachemodels.CategoryViewList;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.exceptions.NotFoundInDatabaseException;
import de.hska.eshopapi.model.Category;
import de.hska.eshopapi.viewmodels.CategoryView;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/category", name = "Category", produces = {"application/json"})
@Api(tags = "Category")
public class CategoryController {

    private final Cache<Long, CategoryViewList> categoryViewListCache;
    private final Cache<UUID, CategoryView> categoryViewCache;
    private RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<CategoryView>> CategoryListTypeRef = new ParameterizedTypeReference<List<CategoryView>>() {
    };

    @Autowired
    public CategoryController(
            RestTemplate restTemplate,
            Cache<Long, CategoryViewList> categoryViewListCache,
            Cache<UUID, CategoryView> categoryViewCache) {
        this.restTemplate = restTemplate;
        this.categoryViewListCache = categoryViewListCache;
        this.categoryViewCache = categoryViewCache;
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICategory);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICompositeCategory).setPathSegments(segments);
    }

    @HystrixCommand(fallbackMethod = "getCategoriesFromCache")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryView>> getCategories() throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI().build();
        ResponseEntity<List<CategoryView>> categories;
        try {
            categories = this.restTemplate.exchange(uri, HttpMethod.GET, null, CategoryListTypeRef);
            CategoryViewList list = new CategoryViewList(categories.getBody());
            this.categoryViewListCache.put(0L, list);
            this.categoryViewCache.putAll(list.stream().collect(Collectors.toMap(CategoryView::getCategoryId, v -> v)));
        } catch (Exception e) {
            throw new NotFoundInDatabaseException(Category.class, e);
        }

        return new ResponseEntity<>(categories.getBody(), HttpStatus.OK);
    }

    public ResponseEntity<List<CategoryView>> getCategoriesFromCache() {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.categoryViewListCache.get(0L), customHeaders, HttpStatus.OK);
    }


    @HystrixCommand(fallbackMethod = "getCategoryFromCache")
    @RequestMapping(method = RequestMethod.GET, path = "/id/{categoryId}")
    public ResponseEntity<CategoryView> getCategoryById(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) throws URISyntaxException {
        URI uri = makeURI("id", categoryId.toString()).build();
        CategoryView categoryView = this.restTemplate.exchange(uri, HttpMethod.GET, null, CategoryView.class).getBody();
        this.categoryViewCache.put(categoryId, categoryView);

        return new ResponseEntity<>(categoryView, HttpStatus.OK);
    }

    public ResponseEntity<CategoryView> getCategoryFromCache(UUID categoryId) {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.categoryViewCache.get(categoryId), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryView> addCategory(
            @ApiParam(value = "Category", required = true)
            @RequestBody(required = true)
                    Category category) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Category> body = new HttpEntity<>(category);

        return this.restTemplate.postForEntity(uri, body, CategoryView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) throws URISyntaxException {
        URI uri = makeURI(categoryId.toString()).build();
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        return response;
    }
}
