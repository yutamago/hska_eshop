package de.hska.eshopapi.core.user.dao;

import de.hska.eshopapi.core.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleDAO extends JpaRepository<Role, UUID> {
    List<Role> findByType(String type);
}
