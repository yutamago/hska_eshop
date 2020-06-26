package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.ProductSearchOptions;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.converters.CategoryRestModelConverter;
import hska.iwi.eShopMaster.model.converters.ProductRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.ProductDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.sessionFactory.util.OAuth2Manager;
import hska.iwi.eShopMaster.viewmodels.CategoryView;
import hska.iwi.eShopMaster.viewmodels.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductManagerImpl implements ProductManager {

	private static final ParameterizedTypeReference<List<ProductView>> ProductListTypeRef = new ParameterizedTypeReference<>() {};

	private final OAuth2Manager o2;
	private RestTemplate restTemplate;

	@Autowired
	public ProductManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.o2 = OAuth2Manager.getInstance();
	}

	@Override
	public List<Product> getProducts() {
		System.out.println("<<<<<<<<<<<<<<< GET PRODUCTS >>>>>>>>>>>>>>>");
		ResponseEntity<List<ProductView>> products = this.restTemplate.exchange("http://eshop-api:8080/product", HttpMethod.GET, o2.getAuthBody(), ProductListTypeRef);
		List<ProductView> listOfRestingCats = products.getBody();
		List<Product> listOfProducts = listOfRestingCats.stream().map(ProductRestModelConverter::ConvertFromRestView).collect(Collectors.toList());
		return listOfProducts;
	}

	@Override
	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {

		ProductSearchOptions searchOptions = new ProductSearchOptions();
		searchOptions.setDescription(searchDescription);
		searchOptions.setMinPrice(searchMinPrice.toString());
		searchOptions.setMaxPrice(searchMaxPrice.toString());
		HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions, o2.getAuthHeader());

		List<ProductView> products = this.restTemplate.exchange("http://eshop-api:8080/search/", HttpMethod.GET, body, ProductListTypeRef).getBody();
		List<Product> productList = products.stream().map(ProductRestModelConverter::ConvertFromRestView).collect(Collectors.toList());

		return productList;
	}

	@Override
	public Product getProductById(UUID id) {
		ProductView productView = this.restTemplate.exchange("http://eshop-api:8080/product/" + id, HttpMethod.GET, o2.getAuthBody(), ProductView.class).getBody();
		return ProductRestModelConverter.ConvertFromRestView(productView);
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

		ResponseEntity<ProductView> responseEntity = this.restTemplate.postForEntity("http://eshop-api:8080/product/", body, ProductView.class);
		return responseEntity.getBody().getProductId();
	}

	@Override
	public void deleteProductById(UUID id) {
		this.restTemplate.exchange("http://eshop-api:8080/product/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
	}

	@Override
	public boolean deleteProductsByCategoryId(UUID categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
