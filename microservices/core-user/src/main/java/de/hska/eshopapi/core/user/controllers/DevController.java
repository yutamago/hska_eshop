package de.hska.eshopapi.core.user.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.user.dao.RoleDAO;
import de.hska.eshopapi.core.user.dao.UserDAO;
import de.hska.eshopapi.core.user.model.Role;
import de.hska.eshopapi.core.user.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DevController(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserRoleDevModel> setup() {
        Role adminRole = new Role();
        adminRole.setLevel(100);
        adminRole.setType("Admin");
        Role userRole = new Role();
        userRole.setLevel(1);
        userRole.setType("User");
        List<Role> roles = new ArrayList<>(List.of(adminRole, userRole));
        List<Role> newRoles = this.roleDAO.saveAll(roles);

        User admin = new User();
        admin.setPassword("test");
        admin.setUsername("admin");
        admin.setFirstname("test");
        admin.setLastname("test");
        admin.setRoleId(adminRole.getRoleId());

        User user = new User();
        user.setPassword("test");
        user.setUsername("user");
        user.setFirstname("test");
        user.setLastname("test");
        user.setRoleId(userRole.getRoleId());

        List<User> users = new ArrayList<>(List.of(admin, user));
        List<User> newUsers = this.userDAO.saveAll(users);

        UserRoleDevModel userRoleDevModel = new UserRoleDevModel();
        userRoleDevModel.users = newUsers;
        userRoleDevModel.roles = newRoles;

        return new ResponseEntity<>(userRoleDevModel, HttpStatus.OK);
    }

    public static class UserRoleDevModel {
        @JsonProperty
        private List<User> users;
        @JsonProperty private List<Role> roles;

        public List<User> getUsers() {
            return users;
        }

        public void setCategories(List<User> users) {
            this.users = users;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
    }
}
