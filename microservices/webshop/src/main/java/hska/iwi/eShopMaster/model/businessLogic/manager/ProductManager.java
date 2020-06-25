package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Product;

import java.util.List;
import java.util.UUID;

public interface ProductManager {

	public List<Product> getProducts();

	public Product getProductById(UUID id);

	public Product getProductByName(String name);

	public UUID addProduct(String name, double price, UUID categoryId, String details);

	public List<Product> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice);
	
	public boolean deleteProductsByCategoryId(UUID categoryId);
	
    public void deleteProductById(UUID id);
    
	
}
