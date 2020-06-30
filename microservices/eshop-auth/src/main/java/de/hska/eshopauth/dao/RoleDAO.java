package de.hska.eshopauth.dao;


import de.hska.eshopauth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleDAO extends JpaRepository<Role, UUID> {
    List<Role> findByType(String type);
}
