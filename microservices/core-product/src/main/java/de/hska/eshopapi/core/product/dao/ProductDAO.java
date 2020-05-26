package de.hska.eshopapi.core.product.dao;

import de.hska.eshopapi.core.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDAO extends JpaRepository<Product, UUID> {
    Product findByName(String name);
    ArrayList<Product> findByCategoryId(UUID categoryId);

    @Query("SELECT p FROM Product p WHERE p.categoryId in :categoryIds")
    ArrayList<Product> findByCategoryIds(@Param("categoryIds") List<UUID> categoryIds);
}
