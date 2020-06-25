package hska.iwi.eShopMaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductSearchOptions {
    @JsonProperty String description;
    @JsonProperty String minPrice;
    @JsonProperty String maxPrice;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
}
