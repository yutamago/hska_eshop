package de.hska.eshopapi.core.user.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.user.dao.RoleDAO;
import de.hska.eshopapi.core.user.dao.UserDAO;
import de.hska.eshopapi.core.user.model.Role;
import de.hska.eshopapi.core.user.model.User;
import de.hska.eshopapi.core.user.viewmodels.RoleView;
import de.hska.eshopapi.core.user.viewmodels.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user", name = "User", produces = {"application/json"})
@Api(tags = "User")
@Transactional
public class UserController {

    private final RoleDAO roleDAO;
    private final UserDAO userDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public UserController(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed("user.read")
    public ResponseEntity<List<UserView>> getUsers() {
        List<User> users = this.userDAO.findAll();
        List<UserView> userViews = new ArrayList<>();

        users.forEach(user -> {
            Role role = null;
            RoleView roleView = null;
            if (user.getRoleId() != null && roleDAO.existsById(user.getRoleId())) {
                role = Role.makeNew(roleDAO.getOne(user.getRoleId()));
                roleView = RoleView.FromRole(role);
            }
            userViews.add(UserView.FromUser(user, roleView));
        });

        return new ResponseEntity<>(userViews, HttpStatus.OK);
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

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("user.write")
    public ResponseEntity<UserView> addUser(
            @ApiParam(value = "User", required = true)
            @RequestBody(required = true)
                    User user
    ) throws NoSuchAlgorithmException {

        List<User> users = userDAO.findByUsername(user.getUsername());
        if(users.size() > 0)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        if(user.getRoleId() == null || !roleDAO.existsById(user.getRoleId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String pwHash = bytesToHex(MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setPassword(pwHash);

        Role role = Role.makeNew(roleDAO.getOne(user.getRoleId()));
        RoleView roleView = RoleView.FromRole(role);
        User newUser = userDAO.save(User.makeNew(user));

        UserView newUserView = UserView.FromUser(newUser, roleView);

        userDAO.flush();
        roleDAO.flush();
        return new ResponseEntity<>(newUserView, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{userId}")
    @RolesAllowed("user.read")
    public ResponseEntity<UserView> getUser(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) {
        Optional<User> userOptional = this.userDAO.findById(userId);
        return userOptional
                .map(user -> {
                    Role role = Role.makeNew(roleDAO.getOne(user.getRoleId()));
                    RoleView roleView = RoleView.FromRole(role);
                    UserView newUserView = UserView.FromUser(user, roleView);
                    return new ResponseEntity<>(newUserView, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    @RolesAllowed("user.read")
    public ResponseEntity<UserView> getUserByUsername(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("username") String username
    ) {
        List<User> users = this.userDAO.findByUsername(username);
        if(!users.isEmpty()) {
            User user = users.get(0);
            Role role = Role.makeNew(roleDAO.getOne(user.getRoleId()));
            RoleView roleView = RoleView.FromRole(role);
            UserView newUserView = UserView.FromUser(user, roleView);

            return new ResponseEntity<>(newUserView, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
//    public ResponseEntity<User> getUserByUsernamePassword(
//            @ApiParam(value = "user Id", required = true)
//            @PathVariable("username")
//                    String username
//    ) {
//        List<User> users = this.userDAO.findByUsername(username);
//        if(!users.isEmpty())
//            return new ResponseEntity<>(users.get(0), HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    @RolesAllowed("user.write")
    public ResponseEntity<UserView> deleteUser(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) {
        final Optional<User> user = userDAO.findById(userId);

        if(user.isPresent()) {
            user.get().setDeleted(true);
            userDAO.save(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
