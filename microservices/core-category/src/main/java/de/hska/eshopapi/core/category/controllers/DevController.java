package de.hska.eshopapi.core.category.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.category.dao.CategoryDAO;
import de.hska.eshopapi.core.category.model.Category;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final CategoryDAO categoryDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DevController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("dev")
    public ResponseEntity<List<Category>> setup(
            @ApiParam(value = "Categories", required = true)
            @RequestBody(required = true)
                    ArrayList<Category> categories) {

        try {
            List<Category> newCategories = this.categoryDAO.saveAll(categories);
            return new ResponseEntity<>(newCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
