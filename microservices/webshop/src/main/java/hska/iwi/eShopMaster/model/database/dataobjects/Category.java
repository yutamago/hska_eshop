package hska.iwi.eShopMaster.model.database.dataobjects;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This class contains details about categories.
 */
public class Category implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private UUID id;
	private String name;
	private Set<Product> products = new HashSet<Product>(0);

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, Set<Product> products) {
		this.name = name;
		this.products = products;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
