package de.hska.eshopapi.composite.category.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.category.util.ProductUtil;
import de.hska.eshopapi.composite.category.util.RoutesUtil;
import de.hska.eshopapi.composite.category.model.Category;
import de.hska.eshopapi.composite.category.model.Product;
import de.hska.eshopapi.composite.category.viewmodels.CategoryView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/category", name = "Category", produces = {"application/json"})
@Api(tags = "Category")
public class CategoryController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<Category>> CategoryListTypeRef = new ParameterizedTypeReference<List<Category>>() {
    };

    private static final ParameterizedTypeReference<List<Product>> ProductListTypeRef = new ParameterizedTypeReference<List<Product>>() {
    };

    @Autowired
    public CategoryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICategory);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreCategory).setPathSegments(segments);
    }

    private static URIBuilder makeAbsoluteURI(String host, String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(host).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed("category.read")
    public ResponseEntity<List<CategoryView>> getCategories(
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI().build();

        List<Category> categories = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), CategoryListTypeRef).getBody();

        if (categories != null) {
            return new ResponseEntity<>(extendCategories(categories, headers), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    List<CategoryView> extendCategories(List<Category> categories, HttpHeaders headers) throws URISyntaxException {
        List<CategoryView> categoryViews = new ArrayList<>();
        URI uri = makeAbsoluteURI(RoutesUtil.APICoreProduct, RoutesUtil.APIProduct, "categoryIds").build();
        List<UUID> uuids = categories.stream().map(Category::getCategoryId).collect(Collectors.toList());
        HttpEntity<List<UUID>> body = new HttpEntity<>(uuids, headers);

        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.POST, body, new ParameterizedTypeReference<List<Product>>() {
        }).getBody();

        if (products != null) {
            categories.forEach(c -> {
                List<Product> productsCategory = new ArrayList<>();
                for (int i = 0; i < products.size(); i++) {
                    Product p = products.get(i);
                    if (p.getCategoryId().equals(c.getCategoryId())) {
                        productsCategory.add(p);
                    }
                }
                categoryViews.add(CategoryView.FromCategory(c, productsCategory));
            });
        }

        return categoryViews;
    }

    CategoryView extendCategory(Category category, HttpHeaders headers) throws URISyntaxException {

        URI uri = makeAbsoluteURI(RoutesUtil.APICoreProduct, RoutesUtil.APIProduct, "categoryId", category.getCategoryId().toString()).build();
        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), ProductListTypeRef).getBody();
        CategoryView categoryView = CategoryView.FromCategory(category, products);

        return categoryView;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{categoryId}")
    @RolesAllowed("category.read")
    public ResponseEntity<CategoryView> getCategoryById(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId") UUID categoryId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI("id", categoryId.toString()).build();
        Category category = this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(null, headers), Category.class).getBody();
        if (category != null) {
            CategoryView categoryView = extendCategory(category, headers);
            return new ResponseEntity<>(categoryView, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("category.write")
    public ResponseEntity<CategoryView> addCategory(
            @ApiParam(value = "Category", required = true)
            @RequestBody(required = true) Category category,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Category> body = new HttpEntity<>(category, headers);

        return this.restTemplate.postForEntity(uri, body, CategoryView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> deleteCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId") UUID categoryId,
            @RequestHeader HttpHeaders headers
    ) throws URISyntaxException {
        URI getCategoryUrl = makeURI(categoryId.toString()).build();
        URI deleteCategoryUrl = makeURI(categoryId.toString()).build();

        URI getProductsByCategoryUrl = makeAbsoluteURI("http://core-product", "product", "categoryId", categoryId.toString()).build();
        URI deleteProductsByCategoryUrl = makeAbsoluteURI("http://core-product","product", "deleteByCategoryId", categoryId.toString()).build();

        URI restoreCategoryUrl = makeURI("restore", categoryId.toString()).build();
        URI restoreProductsByCategoryUrl = makeAbsoluteURI("http://core-product", "restoreByCategoryId", categoryId.toString()).build();

        Category category = null;
        List<Product> products = new ArrayList<>();

        try {
            category = this.restTemplate.exchange(getCategoryUrl, HttpMethod.GET, new HttpEntity<>(null, headers), Category.class).getBody();
            products = this.restTemplate.exchange(getProductsByCategoryUrl, HttpMethod.GET, new HttpEntity<>(null, headers), ProductUtil.ProductListTypeRef).getBody();
        } catch (Exception ex) {
            ResponseEntity<String> response = new ResponseEntity<>("Category does not exist: " + categoryId, HttpStatus.NOT_FOUND);
        }

        try {
            this.restTemplate.exchange(deleteCategoryUrl, HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);
            if (!products.isEmpty()) {
                this.restTemplate.exchange(deleteProductsByCategoryUrl, HttpMethod.DELETE, new HttpEntity<>(null, headers), Void.class);
            }
        } catch (Exception ex) {
            this.restTemplate.exchange(restoreCategoryUrl, HttpMethod.PUT, new HttpEntity<>(null, headers), Void.class);
            if (!products.isEmpty()) {
                this.restTemplate.exchange(restoreProductsByCategoryUrl, HttpMethod.PUT, new HttpEntity<>(null, headers), Void.class);
            }

            ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.CONFLICT);
            return response;
        }

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }
}
