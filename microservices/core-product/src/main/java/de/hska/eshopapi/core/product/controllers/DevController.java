package de.hska.eshopapi.core.product.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.product.dao.ProductDAO;
import de.hska.eshopapi.core.product.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final ProductDAO productDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DevController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("dev")
    public ResponseEntity<List<Product>> setup(
            @ApiParam(value = "Products", required = true)
            @RequestBody(required = true)
                    ArrayList<Product> products) {
        try {
            List<Product> newProducts = this.productDAO.saveAll(products);
            return new ResponseEntity<>(newProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
