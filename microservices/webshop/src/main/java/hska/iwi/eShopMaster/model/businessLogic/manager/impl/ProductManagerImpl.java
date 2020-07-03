package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import com.opensymphony.xwork2.inject.Inject;
import hska.iwi.eShopMaster.model.ProductSearchOptions;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.converters.ProductRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.sessionFactory.util.PasswordOAuth2Manager;
import hska.iwi.eShopMaster.model.sessionFactory.util.PropertiesLoader;
import hska.iwi.eShopMaster.viewmodels.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	private String eshopApiEdgeUrl = PropertiesLoader.get("eshop-api.edge-url");

	@Autowired
	public ProductManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.o2 = PasswordOAuth2Manager.getInstance();
	}

	@Override
	public List<Product> getProducts() {
		try {
			ResponseEntity<List<ProductView>> products = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/product", HttpMethod.GET, o2.getAuthBody(), ProductListTypeRef);
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
			String searchMinPrice, String searchMaxPrice) {

		ProductSearchOptions searchOptions = new ProductSearchOptions();
		searchOptions.setDescription(searchDescription);
		searchOptions.setMinPrice(searchMinPrice == null ? null : new BigDecimal(searchMinPrice).subtract(new BigDecimal("0.001")));
		searchOptions.setMaxPrice(searchMaxPrice == null ? null : new BigDecimal(searchMaxPrice).add(new BigDecimal("0.001")));
		HttpEntity<ProductSearchOptions> body = new HttpEntity<>(searchOptions, o2.getAuthHeader());

		try {
			List<ProductView> products = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/product/search", HttpMethod.POST, body, ProductListTypeRef).getBody();
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
			ProductView productView = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/product/" + id, HttpMethod.GET, o2.getAuthBody(), ProductView.class).getBody();
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
	public UUID addProduct(String name, String price, UUID categoryId, String details) {
		hska.iwi.eShopMaster.model.Product product = new hska.iwi.eShopMaster.model.Product();
		product.setName(name);
		product.setPrice(new BigDecimal(price).toString());
		product.setDetails(details);
		product.setCategoryId(categoryId);

		HttpEntity<hska.iwi.eShopMaster.model.Product> body = new HttpEntity<>(product, o2.getAuthHeader());

		try {
			ResponseEntity<ProductView> responseEntity = this.restTemplate.postForEntity(eshopApiEdgeUrl + "/eshop-api/product/", body, ProductView.class);
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
			this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/product/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
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
