package de.hska.eshopapi.cachemodels;

import de.hska.eshopapi.model.Role;

import java.util.ArrayList;
import java.util.Collection;

public class RoleList extends ArrayList<Role> {
    public RoleList(final Collection<? extends Role> c) {
        super(c);
    }
}
