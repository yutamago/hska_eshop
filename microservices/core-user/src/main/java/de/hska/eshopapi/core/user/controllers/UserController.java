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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
    private final EntityManagerFactory entityManagerFactory;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public UserController(UserDAO userDAO, RoleDAO roleDAO, EntityManagerFactory entityManagerFactory) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed("user.read")
    public ResponseEntity<List<UserView>> getUsers() {
        List<User> users = this.userDAO.findAll();
        List<UserView> userViews = new ArrayList<>();

        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

        users.forEach(user -> {
            Role role = null;
            RoleView roleView = null;
            if (user.getRoleId() != null && roleDAO.existsById(user.getRoleId())) {
                role = session.byId(Role.class).load(user.getRoleId());
                roleView = RoleView.FromRole(role);
            }
            userViews.add(UserView.FromUser(user, roleView));
        });

        session.close();
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
//    @RolesAllowed("user.write")
    @Transactional
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

        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        Role role = session.byId(Role.class).load(user.getRoleId());
//        Role role = roleDAO.getOne(user.getRoleId()).makeNew();
        RoleView roleView = RoleView.FromRole(role);
        User newUser = userDAO.save(User.makeNew(user));

        UserView newUserView = UserView.FromUser(newUser, roleView);

        userDAO.flush();
        session.close();
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
                    Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

                    Role role = session.byId(Role.class).load(user.getRoleId());
                    RoleView roleView = RoleView.FromRole(role);
                    UserView newUserView = UserView.FromUser(user, roleView);

                    session.close();

                    return new ResponseEntity<>(newUserView, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
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
        newUser = userDAO.save(newUser);

        UserView ret = UserView.FromUser(newUser, RoleView.FromRole(role.get(0)));

        userDAO.flush();

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    @RolesAllowed("user.read")
    public ResponseEntity<UserView> getUserByUsername(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("username") String username
    ) {
        ResponseEntity<UserView> response;
        List<User> users = this.userDAO.findByUsername(username);
        if(!users.isEmpty()) {
            User user = users.get(0);
            Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
            Role daoRole = session.byId(Role.class).load(user.getRoleId());

            Role role = (daoRole).makeNew();
            RoleView roleView = RoleView.FromRole(role);
            UserView newUserView = UserView.FromUser(user, roleView);

            System.out.println("::::::::::::::::::::::: GET USER BY USERNAME ::::::::::::::::::::::::::");
            System.out.println("USER Role ID      = " + user.getRoleId());
            System.out.println("ROLE Role ID      = " + daoRole.getRoleId());
            System.out.println("NEW ROLE Role ID  = " + role.getRoleId());
            System.out.println("ROLE VIEW Role ID = " + roleView.getRoleId());
            System.out.println("USER VIEW Role ID = " + newUserView.getRole().getRoleId());


            response = new ResponseEntity<>(newUserView, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return response;
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

        if(!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDAO.deleteById(userId);
        userDAO.flush();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
