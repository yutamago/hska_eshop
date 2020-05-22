package de.hska.eshopapi.core.product.controllers;

import de.hska.eshopapi.core.product.dao.ProductDAO;
import de.hska.eshopapi.core.product.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
public class ProductController {

    private ProductDAO productDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = new ArrayList<>(this.productDAO.findAll());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> addProduct(
            @ApiParam(value = "Product", required = true)
            @RequestBody(required = true)
                    Product product
    ) {

        List<Product> products = productDAO.findByName(product.getName());
        if(products.size() > 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        product.setProductId(null);
        Product newProduct = productDAO.save(product);

        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/id/{productId}")
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

    @RequestMapping(method = RequestMethod.GET, path = "/categoryId/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        List<Product> products = this.productDAO.findByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
    public ResponseEntity<Product> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        Optional<Product> productOptional = this.productDAO.findById(productId);
        if(productOptional.isPresent()) {
            productDAO.delete(productOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
