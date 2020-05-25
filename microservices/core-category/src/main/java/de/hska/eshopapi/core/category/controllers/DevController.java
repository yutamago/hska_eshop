package de.hska.eshopapi.core.category.controllers;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ArrayList<Category>> setup(
            @ApiParam(value = "Categories", required = true)
            @RequestBody(required = true)
                    ArrayList<Category> categories) {
//        Category spielzeug = new Category();
//        spielzeug.setCategoryId(UUID.fromString("0ccf12d2-f2d2-4d51-816b-6e2624e5b664"));
//        spielzeug.setName("Spielzeug");
//        spielzeug.setProductIds(new HashSet<>(Collections.singletonList(UUID.fromString("1edca507-00a3-4d46-b87e-4dbcfa6096b5"))));
//
//        Category unterwaesche = new Category();
//        unterwaesche.setCategoryId(UUID.fromString("a2377110-b3a2-41a6-b033-1ec0f2547c82"));
//        unterwaesche.setName("Unterwäsche");
//        unterwaesche.setProductIds(new HashSet<>());
//
//        Category buecher = new Category();
//        buecher.setCategoryId(UUID.fromString("0c708787-0cff-4444-8b55-d7179388db10"));
//        buecher.setName("Bücher");
//        buecher.setProductIds(new HashSet<>(Collections.singletonList(UUID.fromString("fc927e8a-95d3-447f-bac6-759ac273e1e6"))));

        try {
            this.categoryDAO.saveAll(categories);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
