package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImpl;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.ProductManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class SearchAction extends ActionSupport {

    private RestTemplate restTemplate;

    public SearchAction() {
        this.restTemplate = new RestTemplate();
    }

    /**
     *
     */
    private static final long serialVersionUID = -6565401833074694229L;


    private String searchDescription = null;
    private String searchMinPrice;
    private String searchMaxPrice;

    private User user;
    private List<Product> products;
    private List<Category> categories;


    public String execute() throws Exception {

        String result = "input";

        // Get user:
        Map<String, Object> session = ActionContext.getContext().getSession();
        user = (User) session.get("webshop_user");
        ActionContext.getContext().setLocale(Locale.GERMAN);

        if (user != null) {
            cleanSearchOptions();

            if (isSearchOptionsValid()){
                // Search products and show results:
                ProductManager productManager = new ProductManagerImpl(restTemplate);
                this.products = productManager.getProductsForSearchValues(this.searchDescription, searchMinPrice, searchMaxPrice);
            } else {
                this.products = new ArrayList<>();
            }

            // Show all categories:
            CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);
            this.categories = categoryManager.getCategories();
            result = "success";
        }

        return result;
    }

    private boolean isSearchOptionsValid() {

        if(searchMinPrice != null) {
            try {
                new BigDecimal(searchMinPrice);
            } catch (Exception nfe) {
                addActionError("Mindestpreis ist keine gültige Dezimal-Zahl!");
                return false;
            }
        }
        if(searchMaxPrice != null) {
            try {
                new BigDecimal(searchMaxPrice);
            } catch (Exception nfe) {
                addActionError("Maximalpreis ist keine gültige Dezimal-Zahl!");
                return false;
            }
        }

        return true;
    }

    private void cleanSearchOptions() {
        if (searchMinPrice != null) {
            searchMinPrice = searchMinPrice.trim();
            if(searchMinPrice.length() == 0)
                searchMinPrice = null;
        }
        if(searchMaxPrice != null) {
            searchMaxPrice = searchMaxPrice.trim();
            if(searchMaxPrice.length() == 0)
                searchMaxPrice = null;
        }

        if (searchDescription != null) {
            searchDescription = searchDescription.trim();
        } else {
            searchDescription = "";
        }
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }


    public String getSearchValue() {
        return searchDescription;
    }


    public void setSearchValue(String searchValue) {
        this.searchDescription = searchValue;
    }


    public String getSearchMinPrice() {
        return searchMinPrice;
    }


    public void setSearchMinPrice(String searchMinPrice) {
        this.searchMinPrice = searchMinPrice;
    }


    public String getSearchMaxPrice() {
        return searchMaxPrice;
    }


    public void setSearchMaxPrice(String searchMaxPrice) {
        this.searchMaxPrice = searchMaxPrice;
    }


//	public Double getSearchMinPrice() {
//		return searchMinPrice;
//	}
//
//
//	public void setSearchMinPrice(Double searchMinPrice) {
//		this.searchMinPrice = searchMinPrice;
//	}
//
//
//	public Double getSearchMaxPrice() {
//		return searchMaxPrice;
//	}
//
//
//	public void setSearchMaxPrice(Double searchMaxPrice) {
//		this.searchMaxPrice = searchMaxPrice;
//	}
}
