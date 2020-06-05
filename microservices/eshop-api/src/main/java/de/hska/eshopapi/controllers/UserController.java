package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.cachemodels.UserViewList;
import de.hska.eshopapi.exceptions.NotFoundInDatabaseException;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.model.User;
import de.hska.eshopapi.viewmodels.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/user", name = "User", produces = {"application/json"})
@Api(tags = "User")
public class UserController {
    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<UserView>> UserListTypeRef = new ParameterizedTypeReference<List<UserView>>() {
    };
    private Cache<Long, UserViewList> userViewListCache;
    private Cache<UUID, UserView> userViewCache;

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APIUser);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.APICoreUser).setPathSegments(segments);
    }

    @Autowired
    public UserController(RestTemplate restTemplate, Cache<Long, UserViewList> userViewListCache, Cache<UUID, UserView> userViewCache) {
        this.restTemplate = restTemplate;
        this.userViewListCache = userViewListCache;
        this.userViewCache = userViewCache;
    }

    @HystrixCommand(fallbackMethod = "getUsersFromCache")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserView>> getUsers() throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI().build();
        List<UserView> userViews;

        try {
            userViews = this.restTemplate.exchange(uri, HttpMethod.GET, null, UserController.UserListTypeRef).getBody();
            UserViewList list = new UserViewList(userViews);
            userViewListCache.put(0L, list);
            userViewCache.putAll(list.stream().collect(Collectors.toMap(UserView::getUserId, v -> v)));
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(UserViewList.class, ex);
        }

        return new ResponseEntity<>(userViews, HttpStatus.OK);
    }


    public ResponseEntity<List<UserView>> getUsersFromCache() {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<List<UserView>>(this.userViewListCache.get(0L), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "getUserFromCache")
    @RequestMapping(method = RequestMethod.GET, path = "/id/{userId}")
    public ResponseEntity<UserView> getUserById(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) throws URISyntaxException, NotFoundInDatabaseException {
        URI uri = makeURI("id", userId.toString()).build();
        UserView userView;

        try {
            userView = this.restTemplate.exchange(uri, HttpMethod.GET, null, UserView.class).getBody();
            this.userViewCache.put(userView.getUserId(), userView);
        } catch (Exception ex) {
            throw new NotFoundInDatabaseException(UserView.class, ex);
        }

        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    public ResponseEntity<UserView> getUserFromCache(UUID userId) {
        MultiValueMap<String, String> customHeaders = new HttpHeaders();
        customHeaders.add("fromCache", "true");
        customHeaders.add("isFallback", "true");

        return new ResponseEntity<>(this.userViewCache.get(userId), customHeaders, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    public ResponseEntity<UserView> getUserByUsername(
            @ApiParam(value = "username", required = true)
            @PathVariable("username")
                    String username
    ) throws URISyntaxException {
        URI uri = makeURI("username", username).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, UserView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserView> addUser(
            @ApiParam(value = "User", required = true)
            @RequestBody(required = true)
                    User user
    ) throws URISyntaxException {
        URI uri = makeURI().build();
        HttpEntity<User> body = new HttpEntity<>(user);

        return this.restTemplate.postForEntity(uri, body, UserView.class);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    public ResponseEntity<UserView> deleteUser(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) throws URISyntaxException {
        URI uri = makeURI(userId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.DELETE, null, UserView.class);
    }
}
