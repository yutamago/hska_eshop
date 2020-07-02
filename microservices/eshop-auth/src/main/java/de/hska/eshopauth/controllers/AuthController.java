package de.hska.eshopauth.controllers;

import de.hska.eshopauth.dao.RoleDAO;
import de.hska.eshopauth.dao.UserDAO;
import de.hska.eshopauth.model.Role;
import de.hska.eshopauth.model.User;
import de.hska.eshopauth.viewmodels.RoleView;
import de.hska.eshopauth.viewmodels.UserView;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/auth", name = "Auth", produces = {"application/json"})
@Api(tags = "Auth")
public class AuthController {

    private RoleDAO roleDAO;
    private UserDAO userDAO;

    @Autowired
    public AuthController(RoleDAO roleDAO, UserDAO userDAO) {
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/user/me", method = RequestMethod.GET)
    public Principal user(Principal principal) {
        return principal;
    }


}
