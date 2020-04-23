package hska.iwi.eShopMaster.api.Controller;

import io.swagger.annotations.*;
import hska.iwi.eShopMaster.model.Search;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class SearchApiController {

    private static final Logger log = LoggerFactory.getLogger(SearchApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SearchApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @ApiOperation(value = "Search for a product", nickname = "search", notes = "", response = Search.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })    }, tags={ "Search", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Search.class, responseContainer = "List") })
    @RequestMapping(value = "/search",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<Search>> search() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Search>>(objectMapper.readValue("[ {\n  \"minPrice\" : \"minPrice\",\n  \"description\" : \"description\",\n  \"maxPrice\" : \"maxPrice\"\n}, {\n  \"minPrice\" : \"minPrice\",\n  \"description\" : \"description\",\n  \"maxPrice\" : \"maxPrice\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Search>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Search>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
