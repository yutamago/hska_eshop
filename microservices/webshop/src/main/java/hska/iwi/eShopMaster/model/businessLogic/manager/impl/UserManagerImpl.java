package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.Product;
import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.converters.ProductRestModelConverter;
import hska.iwi.eShopMaster.model.converters.UserRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.RoleDAO;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.UserDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.viewmodels.ProductView;
import hska.iwi.eShopMaster.viewmodels.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {

    private static final ParameterizedTypeReference<List<UserView>> UserListTypeRef = new ParameterizedTypeReference<>() {
    };

    private RestTemplate restTemplate;

    @Autowired
    public UserManagerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void registerUser(String username, String name, String lastname, String password, Role role) {

        hska.iwi.eShopMaster.model.User restUser = new hska.iwi.eShopMaster.model.User();
        restUser.setRoleId(role.getId());
        restUser.setFirstname(name);
        restUser.setLastname(lastname);
        restUser.setPassword(password);
        restUser.setUsername(username);

        HttpEntity<hska.iwi.eShopMaster.model.User> body = new HttpEntity<>(restUser);

        ResponseEntity<UserView> responseEntity = this.restTemplate.postForEntity("http://eshop-api:8080/user/", body, UserView.class);

    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.equals("")) {
            return null;
        }

        UserView userView = this.restTemplate.exchange("http://eshop-api:8080/user/username/" + username, HttpMethod.GET, null, UserView.class).getBody();
        return UserRestModelConverter.ConvertFromRestView(userView);
    }

    @Override
    public boolean deleteUserById(UUID id) {
        this.restTemplate.exchange("http://eshop-api:8080/user/" + id, HttpMethod.DELETE, null, String.class);

        return true;
    }

    @Override
    public Role getRoleByLevel(int level) {
        // TODO
        return null;
    }

    @Override
    public boolean doesUserAlreadyExist(String username) {

        User dbUser = this.getUserByUsername(username);

        if (dbUser != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validate(User user) {
        if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
            return false;
        }

        return true;
    }

}
