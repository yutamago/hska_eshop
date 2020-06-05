package de.hska.eshopapi.cachemodels;

import de.hska.eshopapi.viewmodels.UserView;

import java.util.ArrayList;
import java.util.Collection;

public class UserViewList extends ArrayList<UserView> {
    public UserViewList(final Collection<? extends UserView> c) {
        super(c);
    }
}
