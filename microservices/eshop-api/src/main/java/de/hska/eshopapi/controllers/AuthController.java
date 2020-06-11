package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/auth", name = "OAuth2", produces = {"application/json"})
@Api(tags = "Auth")
public class AuthController {

    @Autowired
    public AuthController() {
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/register")
    public ResponseEntity<Boolean> register(
//            @ApiParam(value = "registerBody", required = true)
//            @RequestBody() Object registerBody
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/login")
    public ResponseEntity<Boolean> login(
//            @ApiParam(value = "loginBody", required = true)
//            @RequestBody() Object loginBody
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
