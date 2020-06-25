package hska.iwi.eShopMaster.model.businessLogic.manager;

import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.util.UUID;


public interface UserManager {
    
    public void registerUser(String username, String name, String lastname, String password, Role role);
    
    public User getUserByUsername(String username);
    
    public boolean deleteUserById(UUID id);
    
    public Role getRoleByLevel(int level);
    
    public boolean doesUserAlreadyExist(String username);
}
