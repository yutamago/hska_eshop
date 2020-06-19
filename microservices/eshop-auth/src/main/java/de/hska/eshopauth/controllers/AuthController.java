package de.hska.eshopauth.controllers;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/auth", name = "Auth", produces = {"application/json"})
@Api(tags = "Auth")
public class AuthController {

//    @RequestMapping(path = "login", method = RequestMethod.POST)
//    public ResponseEntity<String> login() {
//        return ResponseEntity.ok("lol hi");
//    }
//
//    @RequestMapping(path = "register", method = RequestMethod.POST)
//    public ResponseEntity<String> register() {
//        return ResponseEntity.ok("lol hi");
//    }

    @RequestMapping(path = "/user/me", method = RequestMethod.GET)
    public Principal user(Principal principal) {
        return principal;
    }
}
