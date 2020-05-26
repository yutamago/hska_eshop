package de.hska.eshopapi.composite.category.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.composite.category.RoutesUtil;
import de.hska.eshopapi.composite.category.model.Category;
import de.hska.eshopapi.composite.category.model.Product;
import de.hska.eshopapi.composite.category.viewmodels.CategoryView;
import io.swagger.annotations.Api;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<Category>> CategoryListTypeRef = new ParameterizedTypeReference<List<Category>>() {
    };

    private static final ParameterizedTypeReference<List<Product>> ProductListTypeRef = new ParameterizedTypeReference<List<Product>>() {
    };

    @Autowired
    public DevController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static URIBuilder makeURI(String host, String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>(Arrays.asList(path));
        return new URIBuilder(host).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DevModel> setup() throws URISyntaxException {
        UUID categoryUUID1 = UUID.randomUUID();
        UUID categoryUUID2 = UUID.randomUUID();
        UUID categoryUUID3 = UUID.randomUUID();
        UUID productUUID1 = UUID.randomUUID();
        UUID productUUID2 = UUID.randomUUID();
        UUID productUUID3 = UUID.randomUUID();

        Category spielzeug = new Category();
        spielzeug.setCategoryId(categoryUUID1);
        spielzeug.setName("Spielzeug");
        spielzeug.setProductIds(new HashSet<>(Collections.singletonList(productUUID1)));

        Category unterwaesche = new Category();
        unterwaesche.setCategoryId(categoryUUID2);
        unterwaesche.setName("Unterwäsche");
        unterwaesche.setProductIds(new HashSet<>(Collections.singletonList(productUUID2)));

        Category buecher = new Category();
        buecher.setCategoryId(categoryUUID3);
        buecher.setName("Bücher");
        buecher.setProductIds(new HashSet<>(Collections.singletonList(productUUID3)));

        Product legoDrachenburg = new Product();
        legoDrachenburg.setProductId(productUUID1);
        legoDrachenburg.setCategoryId(categoryUUID1);
        legoDrachenburg.setName("LEGO Drachenburg");
        legoDrachenburg.setDetails("Ein Spaß für die ganze Familie");
        legoDrachenburg.setPrice(new BigDecimal("14.99"));

        Product wb = new Product();
        wb.setProductId(productUUID2);
        wb.setCategoryId(categoryUUID2);
        wb.setName("Unterhemd");
        wb.setDetails("formangepasstes weißes geripptes Tank-Top, das von Männern getragen wird; sieht auf gut gebauten Typen gut aus, auf mageren Typen erbärmlich und auf fetten Typen mit Bierbauch ekelhaft");
        wb.setPrice(new BigDecimal("4.79"));

        Product buch = new Product();
        buch.setProductId(productUUID3);
        buch.setCategoryId(categoryUUID3);
        buch.setName("Die Tribute von Panem X: Das Lied von Vogel und Schlange");
        buch.setDetails("Im Kapitol macht sich der 18-jährige Coriolanus Snow bereit, als Mentor bei den Hungerspielen zu Ruhm und Ehre zu gelangen.");
        buch.setPrice(new BigDecimal("26.00"));

        HttpEntity<ArrayList<Category>> bodyCategory = new HttpEntity<>(new ArrayList<>(Arrays.asList(spielzeug, unterwaesche, buecher)));
        HttpEntity<ArrayList<Product>> bodyProduct = new HttpEntity<>(new ArrayList<>(Arrays.asList(legoDrachenburg, wb, buch)));

        ResponseEntity<List<Category>> newCategories = this.restTemplate.exchange(makeURI(RoutesUtil.APICoreCategory, RoutesUtil.APIDev).build(),
                HttpMethod.POST, bodyCategory, CategoryListTypeRef);
        ResponseEntity<List<Product>> newProducts = this.restTemplate.exchange(makeURI(RoutesUtil.APICoreProduct, RoutesUtil.APIDev).build(),
                HttpMethod.POST, bodyProduct, ProductListTypeRef);

        DevModel ret = new DevModel();
        ret.categories = newCategories.getBody();
        ret.products = newProducts.getBody();

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    public static class DevModel {
        @JsonProperty private List<Category> categories;
        @JsonProperty private List<Product> products;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }
}
