package de.hska.eshopapi.controllers;

import de.hska.eshopapi.RoutesUtil;
import de.hska.eshopapi.model.User;
import de.hska.eshopapi.viewmodels.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user", name = "User", produces = {"application/json"})
@Api(tags = "User")
public class UserController {
    private final RestTemplate restTemplate;

    private static final ParameterizedTypeReference<List<UserView>> UserListTypeRef = new ParameterizedTypeReference<List<UserView>>() {
    };

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>();
        segments.add(RoutesUtil.APICoreUser);
        segments.add(RoutesUtil.APIUser);
        segments.addAll(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.Localhost).setPathSegments(segments);
    }

    @Autowired
    public UserController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserView>> getUsers() throws URISyntaxException {
        URI uri = makeURI().build();

        return this.restTemplate.exchange(uri, HttpMethod.GET, null, UserController.UserListTypeRef);
    }

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

    @RequestMapping(method = RequestMethod.GET, path = "/id/{userId}")
    public ResponseEntity<UserView> getUserById(
            @ApiParam(value = "user Id", required = true)
            @PathVariable("userId")
                    UUID userId
    ) throws URISyntaxException {
        URI uri = makeURI("id", userId.toString()).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, UserView.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/username/{username}")
    public ResponseEntity<UserView> getUserByUsername(
            @ApiParam(value = "username", required = true)
            @PathVariable("username")
                    String username
    ) throws URISyntaxException {
        URI uri = makeURI("username", username).build();
        return this.restTemplate.exchange(uri, HttpMethod.GET, null, UserView.class);
    }

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
