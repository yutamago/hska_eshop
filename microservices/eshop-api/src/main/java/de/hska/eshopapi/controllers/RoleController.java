package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping(path = "/role", name = "Role", produces = {"application/json"})
@Api(tags = "Role")
public class RoleController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<Role>> RoleListTypeRef = new ParameterizedTypeReference<List<Role>>() {
    };

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APIRole);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreUser).setPathSegments(segments);
    }

    @Autowired
    public RoleController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoles() throws URISyntaxException {
        URI uri = makeURI().build();

        return this.restTemplate.exchange(uri, HttpMethod.GET, null, RoleController.RoleListTypeRef);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{roleId}")
    public ResponseEntity<Role> getRoleById(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) throws URISyntaxException {
        URI uri = makeURI("id", roleId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, Role.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/type/{type}")
    public ResponseEntity<Role> getRoleByType(
            @ApiParam(value = "role type", required = true)
            @PathVariable("type")
                    String type
    ) throws URISyntaxException {
        URI uri = makeURI("type", type).build();

        // TODO: Try-catch nachziehen für alle anderen Stellen
        try {
            ResponseEntity<Role> response = this.restTemplate.exchange(uri, HttpMethod.GET, null, Role.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(exception.getStatusCode());
        }
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Role> addRole(
            @ApiParam(value = "Role", required = true)
            @RequestBody(required = true)
                    Role role
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<Role> body = new HttpEntity<>(role);

        return this.restTemplate.postForEntity(uri, body, Role.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{roleId}")
    public ResponseEntity<Role> deleteRole(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) throws URISyntaxException {
        URI uri = makeURI(roleId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, null, Role.class);
    }
    
}
