package hska.iwi.eShopMaster.model.sessionFactory.util;

import com.opensymphony.xwork2.ActionContext;
import hska.iwi.eShopMaster.model.AuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class OAuth2Manager {

    private RestTemplate restTemplate;
    private static OAuth2Manager INSTANCE = new OAuth2Manager();
    public static OAuth2Manager getInstance() {
        return INSTANCE;
    }

    private OAuth2Manager() {
        restTemplate = new RestTemplate();

        Map<String, Object> session = ActionContext.getContext().getSession();
        authToken = (AuthToken) session.get("auth");
        if(authToken != null)
            accessToken = authToken.getAccessToken();
    }


    private static final String CLIENT_ID = "eshop-client";
    private static final String CLIENT_SECRET = "123456";
    private static final String TOKEN_ADDRESS = "http://eshop-auth:8090/oauth/token";

    private AuthToken authToken = null;
    private String accessToken = "";

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.asMediaType(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authToken.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        System.out.println("================== HEADERS FOR OAUTH2 REQUEST ==================== \n" + headers);
        return headers;
    }
    public HttpEntity<?> getAuthBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, getAuthHeader());

        return httpEntity;
    }

    public String authorize(String username, String password) {
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
        try {
            this.authToken = this.restTemplate.exchange(TOKEN_ADDRESS, HttpMethod.POST, httpEntity, AuthToken.class).getBody();
            System.out.println(":::::::::::::::::::::AUTH TOKEN:::::::::::::::::::: " + this.authToken);
            this.accessToken = this.authToken.getAccessToken();
        } catch(Exception ex) {
            System.out.println("::::::::::::::::::::: FAILED TO GET AUTH TOKEN:::::::::::::::::::: ");
            System.out.println(ex);
        }

        return this.accessToken;
    }

    private static String getAuthorizationHeader() {
        String ret = "Basic ";
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return ret + credentials;
    }
}
