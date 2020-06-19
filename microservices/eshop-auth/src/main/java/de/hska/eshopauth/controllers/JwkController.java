package de.hska.eshopauth.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/oauth2", name = "OAuth2", produces = {"application/json"})
public class JwkController {

    private final JWKSet jwkSet;

    @Autowired
    public JwkController(JWKSet jwkSet) {
        this.jwkSet = jwkSet;
    }

    @RequestMapping(value = "keys", produces = "application/json; charset=UTF-8")
    public String keys() {
        System.out.println("------------getting oauth keys");
        return this.jwkSet.toString();
    }
}