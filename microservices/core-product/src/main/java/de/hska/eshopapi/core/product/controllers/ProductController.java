package de.hska.eshopapi.core.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.product.dao.ProductDAO;
import de.hska.eshopapi.core.product.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/product", name = "Product", produces = {"application/json"})
@Api(tags = "Product")
@Transactional
public class ProductController {

    private ProductDAO productDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = new ArrayList<>(this.productDAO.findAll());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
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
    public ResponseEntity<Product> getProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        Optional<Product> productOptional = this.productDAO.findById(productId);
        if (productOptional.isPresent()) {
            return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, path = "/categoryIds")
    public ResponseEntity<List<Product>> getProductsByCategoryIds(
            @ApiParam(value = "category Ids", required = true)
            @RequestBody List<UUID> categories
    ) {
        List<Product> products = this.productDAO.findByCategoryIds(categories)
                .stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/categoryId/{categoryId}")
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
    public ResponseEntity<Product> deleteProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        final Optional<Product> product = productDAO.findById(productId);

        if(product.isPresent()) {
            product.get().setDeleted(true);
            productDAO.save(product.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @HystrixCommand
    @RequestMapping(method = RequestMethod.PUT, path = "/restore/{productId}")

    public ResponseEntity<String> restoreProduct(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        final Optional<Product> product = productDAO.findDeletedById(productId);

        if(product.isPresent()) {
            product.get().setDeleted(false);
            productDAO.save(product.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteByCategoryId/{categoryId}")
    public ResponseEntity<List<Product>> deleteByCategoryId(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        List<Product> products = this.productDAO.findByCategoryId(categoryId);
        if(!products.isEmpty()){

            for (Product product: products
                 ) {
                product.setDeleted(true);
                productDAO.save(product);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.PUT, path = "/restoreByCategoryId/{categoryId}")
    public ResponseEntity<List<Product>> restoreByCategoryId(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        List<Product> products = this.productDAO.findByCategoryId(categoryId);
        if(!products.isEmpty()){

            for (Product product: products
            ) {
                product.setDeleted(false);
                productDAO.save(product);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
