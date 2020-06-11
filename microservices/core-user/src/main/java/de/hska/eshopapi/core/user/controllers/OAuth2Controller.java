package de.hska.eshopapi.core.user.controllers;

import io.swagger.annotations.Api;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/oauth2", name = "OAuth2", produces = {"application/json"})
@Api(tags = "OAuth2")
public class OAuth2Controller {
    @GetMapping("/infoToken")
    public String resource(@AuthenticationPrincipal Jwt jwt) {
        String test = jwt.getClaims();
        return String.format("Token info:\n user_name: "); //%s \n client_id: %s\n expiration: %s\n scopes: %s\n (subjectId: %s)" ,
//	            jwt.getClaims().get("user_name"),
//	            jwt.getClaims().get("client_id"),
//	            jwt.getClaims().get("exp"),
//	            jwt.getClaims().get("scope"),
//	            jwt.getSubject());
    }
}
