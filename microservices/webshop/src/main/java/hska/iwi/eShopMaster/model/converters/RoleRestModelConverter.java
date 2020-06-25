package hska.iwi.eShopMaster.model.converters;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.viewmodels.CategoryView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoleRestModelConverter {
    public static Role ConvertFromRestView(hska.iwi.eShopMaster.model.Role restRole) {
        Role role = new Role(restRole.getType(), restRole.getLevel());
        role.setId(restRole.getRoleId());
        return role;
    }
    public static Role ConvertFromRestCore(hska.iwi.eShopMaster.model.Role restRole) {
        return ConvertFromRestView(restRole);
    }

    public static hska.iwi.eShopMaster.model.Role ConvertToRestView(Role role) {
        hska.iwi.eShopMaster.model.Role restRole = new hska.iwi.eShopMaster.model.Role();
        restRole.setRoleId(role.getId());
        restRole.setLevel(role.getLevel());
        restRole.setType(role.getTyp());
        return restRole;
    }
    public static hska.iwi.eShopMaster.model.Role ConvertToRestCore(Role role) {
        return ConvertToRestView(role);
    }
}
