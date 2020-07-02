package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class DeleteCategoryAction extends ActionSupport {

	private RestTemplate restTemplate;

	public DeleteCategoryAction() {
		this.restTemplate = new RestTemplate();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1254575994729199914L;
	
	private String catId;
	private List<Category> categories;
	private String error;

	public String execute() throws Exception {
		
		String res = "input";
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");
		
		if(user != null && (user.getRole().getTyp().equals("admin"))) {

			// Helper inserts new Category in DB:
			CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);

			try {
				categoryManager.delCategoryById(UUID.fromString(catId));
				categories = categoryManager.getCategories();
				res = "success";
			} catch (HttpClientErrorException e) {
				if(e.getStatusCode() == HttpStatus.BAD_REQUEST) {
					this.error = "Die Kategorie darf keine Produkte beinhalten!";
				} else {
					this.error = "Unbekannter Fehler...";
				}
			}


		}
		
		return res;
		
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
