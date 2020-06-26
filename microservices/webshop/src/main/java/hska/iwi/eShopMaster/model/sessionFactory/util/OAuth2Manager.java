package hska.iwi.eShopMaster.model.sessionFactory.util;

import hska.iwi.eShopMaster.model.businessLogic.manager.impl.AuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

public class OAuth2Manager {
    private static OAuth2Manager INSTANCE = new OAuth2Manager();

    private RestTemplate restTemplate;

    private OAuth2Manager() {
        restTemplate = new RestTemplate();
    }

    public static OAuth2Manager getInstance() {
        return INSTANCE;
    }

    private static final String CLIENT_ID = "eshop-client";
    private static final String CLIENT_SECRET = "123456";
    private static final String TOKEN_ADDRESS = "http://eshop-auth:8090/oauth/token";

    public static AuthToken authToken = null;
    public static String accessToken = "";

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken.getAccessToken());
        return headers;
    }
    public HttpEntity<?> getAuthBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, getAuthHeader());

        return httpEntity;
    }

    public String tryGetToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", getAuthorizationHeader());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("scope", "");

// Note the body object as first parameter!
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);


        System.out.println("HEADERS + BODY: " + httpEntity.toString());
        System.out.println("HEADERS: " + httpEntity.getHeaders().toString());
        System.out.println("BODY: " + httpEntity.getBody().toString());

        this.authToken = this.restTemplate.exchange(TOKEN_ADDRESS, HttpMethod.POST, httpEntity, AuthToken.class).getBody();
        System.out.println(":::::::::::::::::::::AUTH TOKEN:::::::::::::::::::: " + this.authToken);
        this.accessToken = this.authToken.getAccessToken();

        return this.accessToken;
    }

    private static String getAuthorizationHeader() {
        String ret = "Basic ";
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return ret + credentials;
    }
}
