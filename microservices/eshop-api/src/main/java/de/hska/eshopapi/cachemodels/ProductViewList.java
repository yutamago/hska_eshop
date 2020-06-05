package de.hska.eshopapi.cachemodels;

import de.hska.eshopapi.viewmodels.ProductView;

import java.util.ArrayList;
import java.util.Collection;

public class ProductViewList extends ArrayList<ProductView> {
    public ProductViewList(final Collection<? extends ProductView> c) {
        super(c);
    }
}
