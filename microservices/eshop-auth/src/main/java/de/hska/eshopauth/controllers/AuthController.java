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

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @RolesAllowed("user.write")
    public ResponseEntity<UserView> register(
            @RequestBody User user
    ) throws NoSuchAlgorithmException {

        List<Role> role = roleDAO.findByType("user");
        if(role.isEmpty())
            return new ResponseEntity<>(HttpStatus.TOO_EARLY);

        if(!userDAO.findByUsername(user.getUsername()).isEmpty())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        String pwHash = bytesToHex(MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setPassword(pwHash);

        User newUser = new User();
        newUser.setRoleId(role.get(0).getRoleId());
        newUser.setPassword(pwHash);
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setUsername(user.getUsername());
        newUser.setDeleted(false);
        newUser = userDAO.save(user);

        UserView ret = UserView.FromUser(newUser, RoleView.FromRole(role.get(0)));

        userDAO.flush();

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }


    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
