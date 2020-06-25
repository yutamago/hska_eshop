package hska.iwi.eShopMaster.model.converters;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.viewmodels.CategoryView;
import hska.iwi.eShopMaster.viewmodels.ProductView;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryRestModelConverter {
    public static Category ConvertFromRestView(CategoryView restCategory) {
        Category category = new Category(restCategory.getName());
        category.setId(restCategory.getCategoryId());

        Set<Product> products = new HashSet<>();
        for (hska.iwi.eShopMaster.model.Product restProduct : restCategory.getProducts()) {
            Product product = ProductRestModelConverter.ConvertFromRestCore(restProduct, category);
            products.add(product);
        }

        return category;
    }


    public static Category ConvertFromRestCore(hska.iwi.eShopMaster.model.Category restCategory) {
        Category category = new Category(restCategory.getName(), new HashSet<>());
        return category;
    }

    public static CategoryView ConvertToRestView(Category category) {
        CategoryView restCategory = new CategoryView();
        restCategory.setCategoryId(category.getId());
        restCategory.setName(category.getName());
        restCategory.setProducts(category.getProducts().stream().map(ProductRestModelConverter::ConvertToRestCore).collect(Collectors.toList()));
        return restCategory;
    }


    public static hska.iwi.eShopMaster.model.Category ConvertToRestCore(Category category) {
        hska.iwi.eShopMaster.model.Category restCategory = new hska.iwi.eShopMaster.model.Category();
        restCategory.setName(category.getName());
        restCategory.setCategoryId(category.getId());

        List<UUID> products = category.getProducts().stream().map(Product::getId).collect(Collectors.toList());
        restCategory.setProductIds(products);

        return restCategory;
    }

}
