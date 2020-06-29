package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.converters.CategoryRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.sessionFactory.util.OAuth2Manager;
import hska.iwi.eShopMaster.viewmodels.CategoryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryManagerImpl implements CategoryManager{
//	private CategoryDAO helper;

	private static final ParameterizedTypeReference<List<CategoryView>> CategoryListTypeRef = new ParameterizedTypeReference<>() {};

	private final OAuth2Manager o2;
	private RestTemplate restTemplate;

	@Autowired
	public CategoryManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.o2 = OAuth2Manager.getInstance();
	}

	@Override
	public List<Category> getCategories() {
		try {
			ResponseEntity<List<CategoryView>> categories = this.restTemplate.exchange("http://eshop-api:8080/category", HttpMethod.GET, o2.getAuthBody(), CategoryListTypeRef);
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
			CategoryView categoryView = this.restTemplate.exchange("http://eshop-api:8080/category/" + id, HttpMethod.GET, o2.getAuthBody(), CategoryView.class).getBody();
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
			this.restTemplate.postForEntity("http://eshop-api:8080/category/", body, CategoryView.class);
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

	}

	@Override
	public void delCategory(Category cat) {
		delCategoryById(cat.getId());
	}

	@Override
	public void delCategoryById(UUID id) {
		try {
			this.restTemplate.exchange("http://eshop-api:8080/category/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
		} catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

	}
}
