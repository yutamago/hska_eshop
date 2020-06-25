package hska.iwi.eShopMaster.model.converters;

import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.viewmodels.UserView;

public class UserRestModelConverter {
    public static User ConvertFromRestView(UserView restUser) {
        User user = new User();
        user.setId(restUser.getUserId());
        user.setFirstname(restUser.getFirstname());
        user.setLastname(restUser.getLastname());
        user.setPassword(restUser.getPassword());
        user.setUsername(restUser.getUsername());
        user.setRole(RoleRestModelConverter.ConvertFromRestCore(restUser.getRole()));
        return user;
    }
    public static User ConvertFromRestCore(hska.iwi.eShopMaster.model.User restUser) {
        User user = new User();
        user.setId(restUser.getUserId());
        user.setFirstname(restUser.getFirstname());
        user.setLastname(restUser.getLastname());
        user.setPassword(restUser.getPassword());
        user.setUsername(restUser.getUsername());
        user.setRole(null);
        return user;
    }

    public static UserView ConvertToRestView(User user) {
        UserView restUser = new UserView();
        restUser.setUserId(user.getId());
        restUser.setFirstname(user.getFirstname());
        restUser.setLastname(user.getLastname());
        restUser.setPassword(user.getPassword());
        restUser.setUsername(restUser.getUsername());
        restUser.setRole(RoleRestModelConverter.ConvertToRestCore(user.getRole()));
        return restUser;
    }
    public static hska.iwi.eShopMaster.model.User ConvertToRestCore(User user) {
        hska.iwi.eShopMaster.model.User restUser = new hska.iwi.eShopMaster.model.User();
        restUser.setUserId(user.getId());
        restUser.setFirstname(user.getFirstname());
        restUser.setLastname(user.getLastname());
        restUser.setPassword(user.getPassword());
        restUser.setUsername(restUser.getUsername());
        restUser.setRoleId(user.getId());
        return restUser;
    }
}
