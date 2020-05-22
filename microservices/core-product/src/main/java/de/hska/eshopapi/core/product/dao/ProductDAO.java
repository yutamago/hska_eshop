package de.hska.eshopapi.core.product.dao;

import de.hska.eshopapi.core.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDAO extends JpaRepository<Product, UUID> {
    List<Product> findByType(String type);
}
