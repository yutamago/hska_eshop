package de.hska.eshopapi.cachemodels;

import de.hska.eshopapi.viewmodels.CategoryView;

import java.util.ArrayList;
import java.util.Collection;

public class CategoryViewList extends ArrayList<CategoryView> {
    public CategoryViewList(final Collection<? extends CategoryView> c) {
        super(c);
    }
}
