package hska.iwi.eShopMaster.api.Controller;

import hska.iwi.eShopMaster.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Controller(value = "Category")
public class CategoryApiController {

    private static final Logger log = LoggerFactory.getLogger(CategoryApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CategoryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @ApiOperation(value = "Add a new category", nickname = "addCategory", notes = "", response = Category.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Category",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Category.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid input")})
    @RequestMapping(value = "/category",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<List<Category>> addCategory(@ApiParam(value = "", required = true) @Valid @RequestBody Category body
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Category>>(objectMapper.readValue("[ {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n}, {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Category>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Category>>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Delete a category", nickname = "deleteCategory", notes = "", response = Category.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Category",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Category.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Category not found")})
    @RequestMapping(value = "/category/{categoryId}",
            produces = {"application/json"},
            method = RequestMethod.DELETE)
    public ResponseEntity<List<Category>> deleteCategory(@ApiParam(value = "category Id", required = true) @PathVariable("categoryId") Long categoryId
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Category>>(objectMapper.readValue("[ {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n}, {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Category>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Category>>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Returns all categories", nickname = "getCategories", notes = "", response = Category.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Category",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Category.class, responseContainer = "List")})
    @RequestMapping(value = "/category",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategories() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Category>>(objectMapper.readValue("[ {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n}, {\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"products\" : [ {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  }, {\n    \"price\" : \"price\",\n    \"name\" : \"name\",\n    \"details\" : \"details\",\n    \"id\" : 6\n  } ]\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Category>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Category>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
