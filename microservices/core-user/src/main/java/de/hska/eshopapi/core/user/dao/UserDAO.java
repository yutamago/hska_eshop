package de.hska.eshopapi.core.user.dao;

import de.hska.eshopapi.core.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
    List<User> findByUsername(String username);
    List<User> findByUsernamePassword(String username, String password);

    @Query("SELECT u FROM User u WHERE u.userId = :roleId")
    List<User> findByRole(@Param("roleId") UUID roleId);
}
