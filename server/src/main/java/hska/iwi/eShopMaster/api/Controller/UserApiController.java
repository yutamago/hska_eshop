package hska.iwi.eShopMaster.api.Controller;

import hska.iwi.eShopMaster.model.Login;
import hska.iwi.eShopMaster.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Api(value = "user", description = "the user API")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @ApiOperation(value = "Login", nickname = "login", notes = "Logs user in", response = Login.class, authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"User",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Login.class)})
    @RequestMapping(value = "/user/login",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Login> login(@ApiParam(value = "", required = true) @Valid @RequestBody Login body
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Login>(objectMapper.readValue("{\n  \"password\" : \"password\",\n  \"username\" : \"username\"\n}", Login.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Login>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Login>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Logout", nickname = "logout", notes = "", tags = {"User",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout successful")})
    @RequestMapping(value = "/user/logout",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> logout() {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Register", nickname = "register", notes = "", tags = {"User",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid input")})
    @RequestMapping(value = "/user/register",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> register(@ApiParam(value = "", required = true) @Valid @RequestBody User body
    ) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
