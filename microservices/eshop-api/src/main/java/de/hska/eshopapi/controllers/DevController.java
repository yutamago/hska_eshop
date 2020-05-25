package de.hska.eshopapi.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.RoutesUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final RestTemplate restTemplate;

    @Autowired
    public DevController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private static URIBuilder makeURI(String... path) throws URISyntaxException {
        List<String> segments = new ArrayList<>(Arrays.asList(path));
        return new URIBuilder(RoutesUtil.Localhost).setPathSegments(segments);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> setup() throws URISyntaxException {
        this.restTemplate.exchange(makeURI(RoutesUtil.APICoreUser, RoutesUtil.APIDev).build(),
                HttpMethod.POST, null, Void.TYPE);
        this.restTemplate.exchange(makeURI(RoutesUtil.APICompositeCategory, RoutesUtil.APIDev).build(),
                HttpMethod.POST, null, Void.TYPE);
        this.restTemplate.exchange(makeURI(RoutesUtil.APICompositeProduct, RoutesUtil.APIDev).build(),
                HttpMethod.POST, null, Void.TYPE);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
