package de.hska.eshopapi.core.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.product.dao.ProductDAO;
import de.hska.eshopapi.core.product.model.Product;
import de.hska.eshopapi.core.product.model.ProductSearchOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
@Transactional
public class ProductController {

    private final ProductDAO productDAO;

    @Autowired
    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed("product.read")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = new ArrayList<>(this.productDAO.findAll());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("product.write")
    public ResponseEntity<Product> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody(required = true)
                    Product product
    ) {

        Product product_ = productDAO.findByName(product.getName());
        if (product_ != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Product newProduct = productDAO.saveAndFlush(Product.makeNew(product));

        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{productId}")
    @RolesAllowed("product.read")
    public ResponseEntity<Product> getProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        Optional<Product> productOptional = this.productDAO.findById(productId);
        return productOptional
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    @RolesAllowed("product.read")
    public ResponseEntity<List<Product>> searchProducts(
            @ApiParam(value = "search options", required = true)
            @Valid @RequestBody ProductSearchOptions searchOptions
    ) throws URISyntaxException {
        if (searchOptions.getMaxPrice() == null)
            searchOptions.setMaxPrice(new BigDecimal(Integer.MAX_VALUE));
        if (searchOptions.getMinPrice() == null)
            searchOptions.setMinPrice(new BigDecimal(0));

        List<Product> products = productDAO.search(searchOptions.getDescription(), searchOptions.getMinPrice(), searchOptions.getMaxPrice());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, path = "/categoryIds")
    @RolesAllowed("product.read")
    public ResponseEntity<List<Product>> getProductsByCategoryIds(
            @ApiParam(value = "category Ids", required = true)
            @RequestBody List<UUID> categories
    ) {
        List<Product> products = this.productDAO.findByCategoryIds(categories);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/categoryId/{categoryId}")
    @RolesAllowed("product.read")
    public ResponseEntity<List<Product>> getProductsByCategoryId(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        List<Product> products = this.productDAO.findByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    @RolesAllowed("product.write")
    public ResponseEntity<Product> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        final Optional<Product> product = productDAO.findById(productId);

        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productDAO.deleteById(productId);
        productDAO.flush();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteByCategoryId/{categoryId}")
    @RolesAllowed("product.write")
    public ResponseEntity<List<Product>> deleteByCategoryId(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        List<Product> products = this.productDAO.findByCategoryId(categoryId);
        if (!products.isEmpty()) {

            for (Product product : products
            ) {
                productDAO.deleteById(product.getProductId());
            }
        }
        productDAO.flush();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
