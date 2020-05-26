package de.hska.eshopapi.core.user.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.user.dao.RoleDAO;
import de.hska.eshopapi.core.user.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/role", name = "Role", produces = {"application/json"})
@Api(tags = "Role")
public class RoleController {

    private RoleDAO roleDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public RoleController(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = StreamSupport.stream(this.roleDAO.findAll().spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Role> addRole(
            @ApiParam(value = "Role", required = true)
            @RequestBody(required = true)
            Role role
    ) {

        List<Role> roles = roleDAO.findByType(role.getType());
        if(roles.size() > 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        role.setRoleId(null);
        Role newRole = roleDAO.save(role);

        return new ResponseEntity<>(newRole, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{roleId}")
    public ResponseEntity<Role> getRole(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) {
        Optional<Role> roleOptional = this.roleDAO.findById(roleId);
        return roleOptional
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/type/{roleType}")
    public ResponseEntity<Role> getRole(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleType")
                    String roleType
    ) {
        List<Role> roles = this.roleDAO.findByType(roleType);
        if(!roles.isEmpty())
            return new ResponseEntity<>(roles.get(0), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{roleId}")
    public ResponseEntity<Role> deleteRole(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) {
        if(this.roleDAO.existsById(roleId)) {
            roleDAO.deleteById(roleId);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
