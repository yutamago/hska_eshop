package hska.iwi.eShopMaster.model.database.dataobjects;


import javax.persistence.*;
import java.util.UUID;

/**
 * This class contains the users of the webshop.
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private UUID id;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private Role role;

	public User() {
	}

	public User(String username, String firstname, String lastname,
			String password, Role role) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.role = role;
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
