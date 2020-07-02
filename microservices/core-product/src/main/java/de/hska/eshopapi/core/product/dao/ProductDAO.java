package de.hska.eshopapi.core.product.dao;

import de.hska.eshopapi.core.product.model.Product;
import de.hska.eshopapi.core.product.model.ProductSearchOptions;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductDAO extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.name = :name and p.isDeleted = false")
    Product findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId and p.isDeleted = false")
    ArrayList<Product> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT p FROM Product p WHERE p.categoryId in :categoryIds")
    ArrayList<Product> findByCategoryIds(@Param("categoryIds") List<UUID> categoryIds);

    @Query("Select p from Product p WHERE p.productId = :productId and p.isDeleted = false")
    Optional<Product> findDeletedById(@Param("productId") UUID productId);

    @Override
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    List<Product> findAll();

    @Override
    List<Product> findAll(Sort sort);

    @Override
    List<Product> findAllById(Iterable<UUID> uuids);

    @Override
    @Query("SELECT p FROM Product p WHERE p.productId = :uuid and p.isDeleted = false")
    Product getOne(@Param("uuid") UUID uuid);

    @Override
    <S extends Product> List<S> findAll(Example<S> example);

    @Override
    <S extends Product> List<S> findAll(Example<S> example, Sort sort);

    @Query( "SELECT p " +
            "FROM Product p " +
            "WHERE (p.name like concat('%', :description, '%') " +
                "or p.details like concat('%', :description, '%')) " +
                "and p.price >= :minPrice and p.price <= :maxPrice")
    List<Product> search(
            @Param("description") String description,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
