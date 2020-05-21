package de.hska.eshopapi.controllers;

import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.viewmodels.UserView;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/role", name = "Role", produces = {"application/json"})
@Api(tags = "Role")
public class RoleController {

    private final RestTemplate restTemplate;
    private final RoutesUtil routesUtil;

    private static final String APIName = "role";
    private static final ParameterizedTypeReference<List<Role>> RoleListTypeRef = new ParameterizedTypeReference<List<Role>>() {
    };

    @Autowired
    public RoleController(RestTemplateBuilder restTemplateBuilder, RoutesUtil routesUtil) {
        this.restTemplate = restTemplateBuilder.build();
        this.routesUtil = routesUtil;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoles() throws URISyntaxException {
        URI uri = new URIBuilder(routesUtil.getCoreRole())
                .setPathSegments(APIName).build();

        return this.restTemplate.exchange(uri, HttpMethod.GET, null, RoleController.RoleListTypeRef);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Role> addRole(
            @ApiParam(value = "Role", required = true)
            @RequestBody(required = true)
                    Role role
    ) throws URISyntaxException {
        URI uri = new URIBuilder(routesUtil.getCoreRole())
                .setPathSegments(APIName).build();
        HttpEntity<Role> body = new HttpEntity<>(role);

        return this.restTemplate.postForEntity(uri, body, Role.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/id/{roleId}")
    public ResponseEntity<Role> getRoleById(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) throws URISyntaxException {
        URI uri = new URIBuilder(routesUtil.getCoreRole())
                .setPathSegments(APIName, "id", roleId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, Role.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/type/{type}")
    public ResponseEntity<Role> getRoleByType(
            @ApiParam(value = "role type", required = true)
            @PathVariable("type")
                    String type
    ) throws URISyntaxException {
        URI uri = new URIBuilder(routesUtil.getCoreRole())
                .setPathSegments(APIName, "type", type).build();

        // TODO: Try-catch nachziehen für alle anderen Stellen
        try {
            ResponseEntity<Role> response = this.restTemplate.exchange(uri, HttpMethod.GET, null, Role.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(exception.getStatusCode());
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{roleId}")
    public ResponseEntity<Role> deleteRole(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) throws URISyntaxException {
        URI uri = new URIBuilder(routesUtil.getCoreRole())
                .setPathSegments(APIName, roleId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, null, Role.class);
    }
    
}
