package de.hska.eshopapi.core.user.controllers;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> setup() {
        Role adminRole = new Role();
        adminRole.setLevel(100);
        adminRole.setType("Admin");
        Role userRole = new Role();
        userRole.setLevel(1);
        userRole.setType("User");

        adminRole = this.roleDAO.save(adminRole);
        userRole = this.roleDAO.save(userRole);

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

        admin = this.userDAO.save(admin);
        user = this.userDAO.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
