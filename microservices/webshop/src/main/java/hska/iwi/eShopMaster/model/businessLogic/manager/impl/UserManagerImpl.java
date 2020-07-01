package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.converters.UserRestModelConverter;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.sessionFactory.util.OAuth2Manager;
import hska.iwi.eShopMaster.viewmodels.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {

    private static final ParameterizedTypeReference<List<UserView>> UserListTypeRef = new ParameterizedTypeReference<>() {
    };

    private OAuth2Manager o2;
    private RestTemplate restTemplate;

    @Autowired
    public UserManagerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.o2 = OAuth2Manager.getInstance();
    }

    @Override
    public void registerUser(String username, String name, String lastname, String password) throws HttpClientErrorException {

        hska.iwi.eShopMaster.model.User restUser = new hska.iwi.eShopMaster.model.User();
        restUser.setFirstname(name);
        restUser.setLastname(lastname);
        restUser.setPassword(password);
        restUser.setUsername(username);

        HttpEntity<hska.iwi.eShopMaster.model.User> body = new HttpEntity<>(restUser);

        try {
            ResponseEntity<UserView> responseEntity = this.restTemplate.exchange("http://eshop-auth:8090/auth/register", HttpMethod.POST, body, UserView.class);
            if(responseEntity.getStatusCode().isError()) {
                throw new HttpClientErrorException(responseEntity.getStatusCode());
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.equals("")) {
            return null;
        }

        try {
            UserView userView = this.restTemplate.exchange("http://eshop-api:8080/user/username/" + username, HttpMethod.GET, o2.getAuthBody(), UserView.class).getBody();
            System.out.println("============= GET USER RESPONSE :::: " + userView);

            return UserRestModelConverter.ConvertFromRestView(userView);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteUserById(UUID id) {
        try {
            this.restTemplate.exchange("http://eshop-api:8080/user/" + id, HttpMethod.DELETE, o2.getAuthBody(), String.class);
            return true;
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public boolean validate(User user) {
        if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
            return false;
        }

        return true;
    }

}
