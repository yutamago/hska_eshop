package de.hska.eshopapi.core.user.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.user.dao.RoleDAO;
import de.hska.eshopapi.core.user.dao.UserDAO;
import de.hska.eshopapi.core.user.model.User;
import de.hska.eshopapi.core.user.viewmodels.UserView;
import de.hska.eshopapi.core.user.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/user", name = "User", produces = {"application/json"})
@Api(tags = "User")
public class UserController {

    private final RoleDAO roleDAO;
    private final UserDAO userDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public UserController(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserView>> getUsers() {
        List<User> users = this.userDAO.findAll();
        List<UserView> userViews = new ArrayList<>();

        users.forEach(user -> {
            Role role = null;
            if (user.getRoleId() != null && roleDAO.existsById(user.getRoleId())) {
                role = roleDAO.getOne(user.getRoleId());
            }
            userViews.add(UserView.FromUser(user, role));
        });

        return new ResponseEntity<>(userViews, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
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

        user.setUserId(null);

        String pwHash = new String(MessageDigest.getInstance("SHA-384").digest(user.getPassword().getBytes()));
        user.setPassword(pwHash);

        Role role = roleDAO.getOne(user.getRoleId());
        User newUser = userDAO.save(user);

        UserView newUserView = UserView.FromUser(newUser, role);

        return new ResponseEntity<>(newUserView, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/id/{userId}")
    public ResponseEntity<UserView> getUser(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) {
        Optional<User> userOptional = this.userDAO.findById(userId);
        return userOptional
                .map(user -> {
                    Role role = roleDAO.getOne(user.getRoleId());
                    UserView newUserView = UserView.FromUser(user, role);
                    return new ResponseEntity<>(newUserView, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    public ResponseEntity<UserView> getUserByUsername(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("username")
                    String username
    ) {
        List<User> users = this.userDAO.findByUsername(username);
        if(!users.isEmpty()) {
            User user = users.get(0);
            Role role = roleDAO.getOne(user.getRoleId());
            UserView newUserView = UserView.FromUser(user, role);

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

    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    public ResponseEntity<UserView> deleteUser(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) {
        Optional<User> userOptional = this.userDAO.findById(userId);
        if(userOptional.isPresent()) {
            userDAO.delete(userOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
