package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.UserManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import hska.iwi.eShopMaster.model.sessionFactory.util.OAuth2Manager;
import org.springframework.web.client.RestTemplate;

public class LoginAction extends ActionSupport {

	private final OAuth2Manager o2;
	private RestTemplate restTemplate;


	public LoginAction() {
		this.restTemplate = new RestTemplate();
		this.o2 = OAuth2Manager.getInstance();
	}
	/**
     *
     */
	private static final long serialVersionUID = -983183915002226000L;
	private String username = null;
	private String password = null;
	private String firstname;
	private String lastname;
	private String role;

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}


	@Override
	public String execute() throws Exception {

		// Return string:
		String result = "input";

		UserManager myCManager = new UserManagerImpl(restTemplate);

		String token = o2.authorize(username, password);
		System.out.println(">>> SERVER RESPONSE!!!! AUTH TOKEN: " + token);
		
		// Get user from DB:
		User user = myCManager.getUserByUsername(getUsername());

		// Does user exist?
		if (user != null) {
			// Is the password correct?

			String pwHash = bytesToHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8)));
//			System.out.println("Password Bytes Self: " + new String(user.getPassword().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
//			System.out.println("Password Raw Self: " + password);
//			System.out.println("Password Hash Self: " + pwHash);
//			System.out.println("Password Hash Server: " + user.getPassword());

			if (user.getPassword().equals(pwHash)) {
				// Get session to save user role and login:
				Map<String, Object> session = ActionContext.getContext().getSession();
				
				// Save user object in session:
				session.put("webshop_user", user);
				session.put("message", "");
				session.put("auth", OAuth2Manager.getInstance().getAuthToken());
				firstname= user.getFirstname();
				lastname = user.getLastname();
				role = user.getRole().getTyp();
				result = "success";
			}
			else {
				addActionError(getText("error.password.wrong"));
			}
		}
		else {
			addActionError(getText("error.username.wrong"));
		}

		return result;
	}
	
	@Override
	public void validate() {
		if (getUsername().length() == 0) {
			addActionError(getText("error.username.required"));
		}
		if (getPassword().length() == 0) {
			addActionError(getText("error.password.required"));
		}
	}

	public String getUsername() {
		return (this.username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return (this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
