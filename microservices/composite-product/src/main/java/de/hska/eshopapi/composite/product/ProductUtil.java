package de.hska.eshopapi.composite.product;

import de.hska.eshopapi.composite.product.model.Category;
import de.hska.eshopapi.composite.product.model.Product;
import de.hska.eshopapi.composite.product.viewmodels.ProductView;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductUtil {

    public static final ParameterizedTypeReference<List<Product>> ProductListTypeRef = new ParameterizedTypeReference<List<Product>>() {
    };
    public static final ParameterizedTypeReference<List<Category>> CategoryListTypeRef = new ParameterizedTypeReference<List<Category>>() {
    };

    public static List<ProductView> ExtendProducts(List<Product> products, RestTemplate restTemplate) throws URISyntaxException {
        URI uri = makeCategoryURI("multiple-id").build();
        List<ProductView> productViews = new ArrayList<>();
        HttpEntity<List<UUID>> categoryIdsBody = new HttpEntity<>(products.stream().map(Product::getCategoryId).collect(Collectors.toList()));
        List<Category> categories = restTemplate.exchange(uri, HttpMethod.POST, categoryIdsBody, CategoryListTypeRef).getBody();

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
