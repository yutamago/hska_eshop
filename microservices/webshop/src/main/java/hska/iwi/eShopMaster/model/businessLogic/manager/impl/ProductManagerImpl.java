package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.ProductSearchOptions;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.converters.ProductRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.sessionFactory.util.PasswordOAuth2Manager;
import hska.iwi.eShopMaster.viewmodels.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductManagerImpl implements ProductManager {

	private static final ParameterizedTypeReference<List<ProductView>> ProductListTypeRef = new ParameterizedTypeReference<>() {};

	private final PasswordOAuth2Manager o2;
	private RestTemplate restTemplate;

	@Autowired
	public ProductManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.o2 = PasswordOAuth2Manager.getInstance();
	}

	@Override
	public List<Product> getProducts() {
		try {
			ResponseEntity<List<ProductView>> products = this.restTemplate.exchange("http://eshop-api:8080/product", HttpMethod.GET, o2.getAuthBody(), ProductListTypeRef);
			List<ProductView> listOfRestingCats = products.getBody();
			System.out.printf("GET PRODUCTS = Status Code: %d\n", products.getStatusCode().value());
			System.out.println("GET PRODUCTS RET == null: " + (listOfRestingCats == null));

			List<Product> listOfProducts = listOfRestingCats.stream().map(ProductRestModelConverter::ConvertFromRestView).collect(Collectors.toList());
			return listOfProducts;
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

		return null;
	}

	@Override
	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {

		ProductSearchOptions searchOptions = new ProductSearchOptions();
		searchOptions.setDescription(searchDescription);
		searchOptions.setMinPrice(new BigDecimal(searchMinPrice));
		searchOptions.setMaxPrice(new BigDecimal(searchMaxPrice));
		HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions, o2.getAuthHeader());

		try {
			List<ProductView> products = this.restTemplate.exchange("http://eshop-api:8080/product/search", HttpMethod.POST, body, ProductListTypeRef).getBody();
			List<Product> productList = products.stream().map(ProductRestModelConverter::ConvertFromRestView).collect(Collectors.toList());
			return productList;
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

		return new ArrayList<>();
	}

	@Override
	public Product getProductById(UUID id) {
		try {
			ProductView productView = this.restTemplate.exchange("http://eshop-api:8080/product/" + id, HttpMethod.GET, o2.getAuthBody(), ProductView.class).getBody();
			return ProductRestModelConverter.ConvertFromRestView(productView);
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

		return null;
	}

	@Override
	public Product getProductByName(String name) {
		// TODO
		return null;
	}

	@Override
	public UUID addProduct(String name, double price, UUID categoryId, String details) {
		hska.iwi.eShopMaster.model.Product product = new hska.iwi.eShopMaster.model.Product();
		product.setName(name);
		product.setPrice(Double.toString(price));
		product.setDetails(details);
		product.setCategoryId(categoryId);

		HttpEntity<hska.iwi.eShopMaster.model.Product> body = new HttpEntity<>(product, o2.getAuthHeader());

		try {
			ResponseEntity<ProductView> responseEntity = this.restTemplate.postForEntity("http://eshop-api:8080/product/", body, ProductView.class);
			return responseEntity.getBody().getProductId();
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
		return null;
	}

	@Override
	public void deleteProductById(UUID id) {
		try {
			this.restTemplate.exchange("http://eshop-api:8080/product/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
	}

	@Override
	public boolean deleteProductsByCategoryId(UUID categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
