package de.hska.eshopapi.dao;

import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleDAO extends JpaRepository<Role, UUID> {
    List<Role> findByType(String type);
}
