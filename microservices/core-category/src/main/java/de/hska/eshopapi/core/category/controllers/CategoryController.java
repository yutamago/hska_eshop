package de.hska.eshopapi.core.category.controllers;

import de.hska.eshopapi.core.category.dao.CategoryDAO;
import de.hska.eshopapi.core.category.model.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/category", name = "Category", produces = {"application/json"})
@Api(tags = "Category")
public class CategoryController {

    private CategoryDAO categoryDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = StreamSupport.stream(this.categoryDAO.findAll().spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Category> addCategory(
            @ApiParam(value = "Category", required = true)
            @RequestBody(required = true)
                    Category category
    ) {

        List<Category> categories = categoryDAO.findByName(category.getName());
        if(categories.size() > 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        category.setCategoryId(null);
        Category newCategory = categoryDAO.save(category);

        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/id/{categoryId}")
    public ResponseEntity<Category> getCategoryById(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        Optional<Category> categoryOptional = this.categoryDAO.findById(categoryId);
        return categoryOptional
                .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/productid/{productId}")
    public ResponseEntity<List<Category>> getCategoriesByProductId(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        List<Category> categories = this.categoryDAO.findByProductId(productId);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    public ResponseEntity<Category> deleteCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        Optional<Category> categoryOptional = this.categoryDAO.findById(categoryId);
        if(categoryOptional.isPresent()) {
            categoryDAO.delete(categoryOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
