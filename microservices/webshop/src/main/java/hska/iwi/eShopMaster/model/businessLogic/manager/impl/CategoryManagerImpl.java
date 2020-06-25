package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.converters.CategoryRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
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

	private RestTemplate restTemplate;

	@Autowired
	public CategoryManagerImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Category> getCategories() {
		ResponseEntity<List<CategoryView>> categories = this.restTemplate.exchange("http://eshop-api:8080/category", HttpMethod.GET, null, CategoryListTypeRef);
		List<CategoryView> listOfRestingCats = categories.getBody();
		List<Category> listOfCats = listOfRestingCats.stream().map(CategoryRestModelConverter::ConvertFromRestView).collect(Collectors.toList());
		return listOfCats;
	}

	@Override
	public Category getCategory(UUID id) {
		CategoryView categoryView = this.restTemplate.exchange("http://eshop-api:8080/category/" + id, HttpMethod.GET, null, CategoryView.class).getBody();
		Category category = CategoryRestModelConverter.ConvertFromRestView(categoryView);
		return category;
	}

	@Override
	public Category getCategoryByName(String name) {
		// TODO: implement
		return null;
	}

	@Override
	public void addCategory(String name) {
		Category cat = new Category(name);
		HttpEntity<hska.iwi.eShopMaster.model.Category> body = new HttpEntity<>(CategoryRestModelConverter.ConvertToRestCore(cat));

		this.restTemplate.postForEntity("http://eshop-api:8080/category/", body, CategoryView.class);
	}

	@Override
	public void delCategory(Category cat) {
		delCategoryById(cat.getId());
	}

	@Override
	public void delCategoryById(UUID id) {
		this.restTemplate.exchange("http://eshop-api:8080/category/" + id, HttpMethod.DELETE, null, String.class);
	}
}
