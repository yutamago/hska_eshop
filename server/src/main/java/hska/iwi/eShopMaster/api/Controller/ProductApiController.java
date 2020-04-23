package hska.iwi.eShopMaster.api.Controller;

import hska.iwi.eShopMaster.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@Api(value = "product", description = "the product API")
public class ProductApiController {

    private static final Logger log = LoggerFactory.getLogger(ProductApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ProductApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @ApiOperation(value = "adds a new product", nickname = "addProduct", notes = "", response = Product.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Product",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class, responseContainer = "List")})
    @RequestMapping(value = "/product/{productId}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<List<Product>> addProduct(@ApiParam(value = "", required = true) @Valid @RequestBody Product body
            , @ApiParam(value = "product Id", required = true) @PathVariable("productId") Long productId
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Product>>(objectMapper.readValue("[ {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n}, {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Product>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "deletes a product", nickname = "deleteProduct", notes = "", response = Product.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Product",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class, responseContainer = "List")})
    @RequestMapping(value = "/product/{productId}",
            produces = {"application/json"},
            method = RequestMethod.DELETE)
    public ResponseEntity<List<Product>> deleteProduct(@ApiParam(value = "product Id", required = true) @PathVariable("productId") Long productId
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Product>>(objectMapper.readValue("[ {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n}, {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Product>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "returns Product details", nickname = "getProductDetails", notes = "", response = Product.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Product",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "product not found")})
    @RequestMapping(value = "/product/{productId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getProductDetails(@ApiParam(value = "product Id", required = true) @PathVariable("productId") Long productId
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Product>>(objectMapper.readValue("[ {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n}, {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Product>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "returns all products", nickname = "listAllProducts", notes = "", response = Product.class, responseContainer = "List", authorizations = {
            @Authorization(value = "store_auth", scopes = {
                    @AuthorizationScope(scope = "write:category", description = "creates/updates category"),
                    @AuthorizationScope(scope = "read:category", description = "read category"),
                    @AuthorizationScope(scope = "delete:category", description = "delete category"),
                    @AuthorizationScope(scope = "read:product", description = "read a category"),
                    @AuthorizationScope(scope = "write:product", description = "creates/updates product"),
                    @AuthorizationScope(scope = "delete:product", description = "delete product"),
                    @AuthorizationScope(scope = "login:user", description = "authorization for user to login")
            })}, tags = {"Product",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class, responseContainer = "List")})
    @RequestMapping(value = "/product",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<Product>> listAllProducts() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Product>>(objectMapper.readValue("[ {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n}, {\n  \"price\" : \"price\",\n  \"name\" : \"name\",\n  \"details\" : \"details\",\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Product>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
