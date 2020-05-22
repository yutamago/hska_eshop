package de.hska.eshopapi.core.category.dao;

import de.hska.eshopapi.core.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryDAO extends JpaRepository<Category, UUID> {
    List<Category> findByName(String name);
    List<Category> findByProductId(UUID productId);
}
