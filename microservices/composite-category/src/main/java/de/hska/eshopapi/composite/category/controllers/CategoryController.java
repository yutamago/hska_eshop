package de.hska.eshopapi.composite.category.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.category.ProductUtil;
import de.hska.eshopapi.composite.category.RoutesUtil;
import de.hska.eshopapi.composite.category.model.Category;
import de.hska.eshopapi.composite.category.model.Product;
import de.hska.eshopapi.composite.category.viewmodels.CategoryView;
import de.hska.eshopapi.composite.category.viewmodels.ProductView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<List<CategoryView>> getCategories() throws URISyntaxException {
        URI uri = makeURI().build();

        List<Category> categories = this.restTemplate.exchange(uri, HttpMethod.GET, null, CategoryListTypeRef).getBody();

        if (categories != null) {
            return new ResponseEntity<>(extendCategories(categories), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    List<CategoryView> extendCategories(List<Category> categories) throws URISyntaxException {
        List<CategoryView> categoryViews = new ArrayList<>();
        URI uri = makeAbsoluteURI(RoutesUtil.APICoreProduct, RoutesUtil.APIProduct, "categoryIds").build();
        List<UUID> uuids = categories.stream().map(Category::getCategoryId).collect(Collectors.toList());
        HttpEntity<List<UUID>> body = new HttpEntity<>(uuids);

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

    CategoryView extendCategory(Category category) throws URISyntaxException {

        URI uri = makeAbsoluteURI(RoutesUtil.APICoreProduct, RoutesUtil.APIProduct, "categoryId", category.getCategoryId().toString()).build();
        List<Product> products = this.restTemplate.exchange(uri, HttpMethod.GET, null, ProductListTypeRef).getBody();
        CategoryView categoryView = CategoryView.FromCategory(category, products);

        return categoryView;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{categoryId}")
    public ResponseEntity<CategoryView> getCategoryById(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) throws URISyntaxException {
        URI uri = makeURI("id", categoryId.toString()).build();
        Category category = this.restTemplate.exchange(uri, HttpMethod.GET, null, Category.class).getBody();
        if (category != null) {
            CategoryView categoryView = extendCategory(category);
            return new ResponseEntity<>(categoryView, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


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
        URI getCategoryUrl = makeURI(categoryId.toString()).build();
        URI deleteCategoryUrl = makeURI(categoryId.toString()).build();

        URI getProductsByCategoryUrl = makeURI("http://core-product", "categoryId", categoryId.toString()).build();
        URI deleteProductsByCategoryUrl = makeAbsoluteURI("http://core-product", "deleteByCategoryId", categoryId.toString()).build();

        URI restoreCategoryUrl = makeURI("restore", categoryId.toString()).build();
        URI restoreProductsByCategoryUrl = makeURI("http://core-product", "restoreByCategoryId", categoryId.toString()).build();

        Category category = null;
        List<Product> products = new ArrayList<>();

        try {
            category = this.restTemplate.getForEntity(getCategoryUrl, Category.class).getBody();
            products = this.restTemplate.exchange(getProductsByCategoryUrl, HttpMethod.GET, null, ProductUtil.ProductListTypeRef).getBody();
        } catch (Exception ex) {
            ResponseEntity<String> response = new ResponseEntity<>("Category does not exist: " + categoryId, HttpStatus.NOT_FOUND);
        }

        try {
            this.restTemplate.delete(deleteCategoryUrl);
            if (!products.isEmpty()) {
                this.restTemplate.delete(deleteProductsByCategoryUrl);
            }
        } catch (Exception ex) {
            this.restTemplate.put(restoreCategoryUrl, null);
            if (!products.isEmpty()) {
                this.restTemplate.put(restoreProductsByCategoryUrl, null);
            }

            ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.CONFLICT);
            return response;
        }

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }
}
