package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.util.UUID;


public interface UserManager {
    
    public void registerUser(String username, String name, String lastname, String password);
    
    public User getUserByUsername(String username);

    public boolean deleteUserById(UUID id);
}
