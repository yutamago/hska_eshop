package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.ProductManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.util.Map;
import java.util.UUID;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class ProductDetailsAction extends ActionSupport {

	private RestTemplate restTemplate;

	@Autowired
	public ProductDetailsAction(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	private User user;
	private UUID id;
	private String searchValue;
	private Integer searchMinPrice;
	private Integer searchMaxPrice;
	private Product product;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7708747680872125699L;

	public String execute() throws Exception {

		String res = "input";
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		user = (User) session.get("webshop_user");
		
		if(user != null) {
			ProductManager productManager = new ProductManagerImpl(restTemplate);
			product = productManager.getProductById(id);
			
			res = "success";			
		}
		
		return res;		
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Integer getSearchMinPrice() {
		return searchMinPrice;
	}

	public void setSearchMinPrice(Integer searchMinPrice) {
		this.searchMinPrice = searchMinPrice;
	}

	public Integer getSearchMaxPrice() {
		return searchMaxPrice;
	}

	public void setSearchMaxPrice(Integer searchMaxPrice) {
		this.searchMaxPrice = searchMaxPrice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
