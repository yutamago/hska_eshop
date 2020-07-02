package de.hska.eshopapi.core.category.dao;

import de.hska.eshopapi.core.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
//
public interface CategoryDAO extends JpaRepository<Category, UUID> {
    @Query("select c from Category c where c.name = :name")
    List<Category> findByName(@Param("name") String name);

    @Query("select c from Category c where :productId MEMBER OF c.productIds")
    List<Category> findByProductId(@Param("productId") UUID productId);

    @Query("Select c from Category c WHERE c.categoryId = :categoryId")
    Optional<Category> findDeletedById(@Param("categoryId") UUID categoryId);
}
