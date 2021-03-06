package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.converters.CategoryRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.sessionFactory.util.PasswordOAuth2Manager;
import hska.iwi.eShopMaster.model.sessionFactory.util.PropertiesLoader;
import hska.iwi.eShopMaster.viewmodels.CategoryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryManagerImpl implements CategoryManager{

	private String eshopApiEdgeUrl = PropertiesLoader.get("eshop-api.edge-url");

	private static final ParameterizedTypeReference<List<CategoryView>> CategoryListTypeRef = new ParameterizedTypeReference<>() {};

	private final PasswordOAuth2Manager o2;
	private RestTemplate restTemplate;

	@Autowired
	public CategoryManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.o2 = PasswordOAuth2Manager.getInstance();
	}

	@Override
	public List<Category> getCategories() {
		try {
			ResponseEntity<List<CategoryView>> categories = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/category", HttpMethod.GET, o2.getAuthBody(), CategoryListTypeRef);
//			System.out.println("Kategorien STATUS::::::::: " + categories.getStatusCode().toString());
//			System.out.println("Kategorien ::::::::: " + categories.getBody());
			List<CategoryView> listOfRestCats = categories.getBody();
			List<Category> listOfCats = listOfRestCats.stream().map(CategoryRestModelConverter::ConvertFromRestView).collect(Collectors.toList());
			return listOfCats;
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
		return null;
	}

	@Override
	public Category getCategory(UUID id) {
		try {
			CategoryView categoryView = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/category/" + id, HttpMethod.GET, o2.getAuthBody(), CategoryView.class).getBody();
			Category category = CategoryRestModelConverter.ConvertFromRestView(categoryView);
			return category;
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
		return null;
	}

	@Override
	public Category getCategoryByName(String name) {
		// TODO: implement
		return null;
	}

	@Override
	public void addCategory(String name) {
		Category cat = new Category(name);
		HttpEntity<hska.iwi.eShopMaster.model.Category> body = new HttpEntity<>(CategoryRestModelConverter.ConvertToRestCore(cat), o2.getAuthHeader());

		try {
			this.restTemplate.postForEntity(eshopApiEdgeUrl + "/eshop-api/category/", body, CategoryView.class);
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

	}

	@Override
	public ResponseEntity<String> delCategory(Category cat) throws HttpClientErrorException {
		try {
			 return delCategoryById(cat.getId());
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}

	@Override
	public ResponseEntity<String> delCategoryById(UUID id) throws HttpClientErrorException {
		try {
			ResponseEntity<String> ret = this.restTemplate.exchange(eshopApiEdgeUrl + "/eshop-api/category/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
			if(ret.getStatusCode().isError())
				throw new HttpClientErrorException(ret.getStatusCode());

			return ret;
		} catch(HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
	}
}
