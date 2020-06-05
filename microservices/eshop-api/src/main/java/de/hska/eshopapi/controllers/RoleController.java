package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.cachemodels.RoleList;
import de.hska.eshopapi.exceptions.NotFoundInDatabaseException;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.viewmodels.ProductView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/role", name = "Role", produces = {"application/json"})
@Api(tags = "Role")
public class RoleController {

    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<Role>> RoleListTypeRef = new ParameterizedTypeReference<List<Role>>() {
    };
    private Cache<Long, RoleList> roleListCache;
    private Cache<UUID, Role> roleCache;

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APIRole);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreUser).setPathSegments(segments);
    }

    @Autowired
    public RoleController(RestTemplate restTemplate, Cache<Long, RoleList> roleListCache, Cache<UUID, Role> roleCache) {
        this.restTemplate = restTemplate;
        this.roleListCache = roleListCache;
        this.roleCache = roleCache;
    }

    @HystrixCommand(fallbackMethod = "getRolesFromCache")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoles() throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI().build();
        List<Role> roles;
        try {
            roles = this.restTemplate.exchange(uri, HttpMethod.GET, null, RoleController.RoleListTypeRef).getBody();
            RoleList list = new RoleList(roles);
            this.roleListCache.put(0L, list);
            roleCache.putAll(list.stream().collect(Collectors.toMap(Role::getRoleId, v -> v)));
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(RoleList.class, ex);
        }

        return new ResponseEntity(roles, HttpStatus.OK);
    }


    public ResponseEntity<List<Role>> getRolesFromCache() {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.roleListCache.get(0L), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "getRoleFromCache")
    @RequestMapping(method = RequestMethod.GET, path = "/id/{roleId}")
    public ResponseEntity<Role> getRoleById(
            @ApiParam(value = "role Id", required = true)
            @PathVariable("roleId")
                    UUID roleId
    ) throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI("id", roleId.toString()).build();
        Role role;

        try {
            role = this.restTemplate.exchange(uri, HttpMethod.GET, null, Role.class).getBody();
            this.roleCache.put(role.getRoleId(), role);
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(Role.class, ex);
        }

        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    public ResponseEntity<Role> getRoleFromCache(UUID roleId) {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.roleCache.get(roleId), customHeaders, HttpStatus.OK);
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
