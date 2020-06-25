package de.hska.eshopapi.core.user.controllers;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.jwt.Jwt;
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

//    @RequestMapping(path = "register", method = RequestMethod.POST)
//    public ResponseEntity<String> register() {
//        return ResponseEntity.ok("lol hi");
//    }

//    @RequestMapping(path = "/user/me", method = RequestMethod.GET)
//    public Principal user(Principal principal) {
//        System.out.println(">> LOGGING TOKEN");
//        System.out.println("TOKEN: " + principal.toString());
//
//        return principal;
//    }

    @RequestMapping(path ="/infoTokenEntity", method = RequestMethod.GET)
    public ResponseEntity<Principal> getInfoTokenEntity(final Principal principal) {
//        String test = jwt.getClaims();
//	    String username = jwt.getClaims().get("user_name"),
//	    String clientId =        jwt.getClaims().get("client_id"),
//	    String exp =        jwt.getClaims().get("exp"),
//	    String scope =        jwt.getClaims().get("scope"),
//	    String subject =        jwt.getSubject());
        return ResponseEntity.ok(principal); // String.format("Token info:\n %s", test); //%s \n client_id: %s\n expiration: %s\n scopes: %s\n (subjectId: %s)" ,
    }

    @RequestMapping(path ="/infoToken", method = RequestMethod.GET)
    public String getInfoToken(final Principal principal) {
//        String test = jwt.getClaims();
//	    String username = jwt.getClaims().get("user_name"),
//	    String clientId =        jwt.getClaims().get("client_id"),
//	    String exp =        jwt.getClaims().get("exp"),
//	    String scope =        jwt.getClaims().get("scope"),
//	    String subject =        jwt.getSubject());
        return principal.toString(); // String.format("Token info:\n %s", test); //%s \n client_id: %s\n expiration: %s\n scopes: %s\n (subjectId: %s)" ,
    }
}
