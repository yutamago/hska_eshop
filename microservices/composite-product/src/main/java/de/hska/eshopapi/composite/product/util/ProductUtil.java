package de.hska.eshopapi.composite.product.util;

import de.hska.eshopapi.composite.product.model.Category;
import de.hska.eshopapi.composite.product.model.Product;
import de.hska.eshopapi.composite.product.viewmodels.ProductView;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductUtil {

    public static final ParameterizedTypeReference<List<Product>> ProductListTypeRef = new ParameterizedTypeReference<>() {
    };
    public static final ParameterizedTypeReference<List<Category>> CategoryListTypeRef = new ParameterizedTypeReference<>() {
    };

    public static List<ProductView> extendProducts(List<Product> products, RestTemplate restTemplate, HttpHeaders headers) throws URISyntaxException {
        URI uri = makeCategoryURI("multiple-id").build();
        List<ProductView> productViews = new ArrayList<>();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String[]> categoryIdsBody = new HttpEntity<>(products.stream()
                .map(Product::getCategoryId)
                .map(UUID::toString)
                .collect(Collectors.toList())
                .toArray(new String[]{}), headers);

        System.out.println("PRODUCT UTIL HEADERS =======================================\n" + headers);

        Category[] categories = restTemplate.exchange(uri, HttpMethod.POST, categoryIdsBody, Category[].class).getBody();

        products.forEach(product -> {
            Category productCategory = null;
            for (Category category : categories) {
                if (category.getCategoryId().equals(product.getCategoryId())) {
                    productCategory = category;
                    break;
                }
            }
            productViews.add(ProductView.FromProduct(product, productCategory));
        });

        return productViews;
    }

    private static URIBuilder makeCategoryURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICategory);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreCategory).setPathSegments(segments);
    }

}
