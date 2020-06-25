package hska.iwi.eShopMaster.model.converters;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.viewmodels.ProductView;

import java.math.BigDecimal;

public class ProductRestModelConverter {

    public static Product ConvertFromRestView(ProductView restProduct) {
        Product product = new Product();
        product.setId(restProduct.getProductId());
        product.setName(restProduct.getName());
        product.setPrice(restProduct.getPrice().doubleValue());
        product.setDetails(restProduct.getDetails());
        product.setCategory(CategoryRestModelConverter.ConvertFromRestCore(restProduct.getCategory()));

        return product;
    }

    public static Product ConvertFromRestCore(hska.iwi.eShopMaster.model.Product restProduct, Category category) {
        Product product = new Product(restProduct.getName(), Double.parseDouble(restProduct.getPrice()), category, restProduct.getDetails());
        return product;
    }

    public static ProductView ConvertToRestView(Product product) {
        ProductView restProduct = new ProductView();
        restProduct.setProductId(product.getId());
        restProduct.setName(product.getName());
        restProduct.setDetails(product.getDetails());
        restProduct.setPrice(BigDecimal.valueOf(product.getPrice()));
        restProduct.setCategory(CategoryRestModelConverter.ConvertToRestCore(product.getCategory()));
        return restProduct;
    }

    public static hska.iwi.eShopMaster.model.Product ConvertToRestCore(Product product) {
        hska.iwi.eShopMaster.model.Product restProduct = new hska.iwi.eShopMaster.model.Product();
        restProduct.setProductId(product.getId());
        restProduct.setCategoryId(product.getCategory().getId());
        restProduct.setName(product.getName());
        restProduct.setDetails(product.getDetails());
        restProduct.setPrice(Double.toString(product.getPrice()));
        return restProduct;
    }

}
