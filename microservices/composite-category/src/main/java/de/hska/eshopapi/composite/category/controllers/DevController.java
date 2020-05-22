package de.hska.eshopapi.composite.category.controllers;

import io.swagger.annotations.Api;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final RestTemplate restTemplate;

    @Autowired
    public DevController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> setup() throws URISyntaxException {
        this.restTemplate.exchange(new URIBuilder("http://localhost:8080/core-user/dev").build(),
                HttpMethod.POST, null, Void.TYPE);


        return new ResponseEntity<>(HttpStatus.OK);
    }
}
