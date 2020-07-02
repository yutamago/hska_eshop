package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryManager {

	public List<Category> getCategories();
	
	public Category getCategory(UUID id);
	
	public Category getCategoryByName(String name);
	
	public void addCategory(String name);
	
	public ResponseEntity<String> delCategory(Category cat);
	
	public ResponseEntity<String> delCategoryById(UUID id);

	
}
