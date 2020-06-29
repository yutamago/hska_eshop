package de.hska.eshopapi.core.category.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import de.hska.eshopapi.core.category.dao.CategoryDAO;
import de.hska.eshopapi.core.category.model.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/category", name = "Category", produces = {"application/json"})
@Api(tags = "Category")
@Transactional
public class CategoryController {

    private CategoryDAO categoryDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }


    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed("category.read")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = new ArrayList<>(this.categoryDAO.findAll());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed("category.write")
    public ResponseEntity<Category> addCategory(
            @ApiParam(value = "Category", required = true)
            @RequestBody(required = true)
                    Category category
    ) {

        List<Category> categories = categoryDAO.findByName(category.getName());
        if (categories.size() > 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Category newCategory = categoryDAO.saveAndFlush(Category.makeNew(category));
        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.PUT, path = "/{categoryId}/addProduct/{productId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> addProduct(
            @ApiParam(value = "Category", required = true)
            @PathVariable("categoryId")
                    UUID categoryId,
            @ApiParam(value = "Product", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        List<Category> alreadyInCategories = categoryDAO.findByProductId(productId)
                .stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        if(!alreadyInCategories.isEmpty())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Category> category = categoryDAO.findById(categoryId);
        if(!category.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        category.get().getProductIds().add(productId);
        categoryDAO.saveAndFlush(category.get());

        return new ResponseEntity<>("Successfully added product to category", HttpStatus.OK);
    }


    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/id/{categoryId}")
    @RolesAllowed("category.read")
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

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, path = "/multiple-id")
    @RolesAllowed("category.read")
    public ResponseEntity<ArrayList<Category>> getCategoryByIds(
            @ApiParam(value = "category Ids", required = true)
            @RequestBody(required = true)
                    ArrayList<UUID> categoryIds
    ) {
        List<Category> categories = this.categoryDAO.findAllById(categoryIds);
        return new ResponseEntity<>(new ArrayList<>(categories), HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.GET, path = "/productid/{productId}")
    @RolesAllowed("category.read")
    public ResponseEntity<List<Category>> getCategoriesByProductId(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        List<Category> categories = this.categoryDAO.findByProductId(productId)
                .stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> deleteCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        final Optional<Category> category = categoryDAO.findById(categoryId);

        if (!category.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!category.get().getProductIds().isEmpty()) {
            return new ResponseEntity<>("Category still contains products. Must not be used by any products!", HttpStatus.LOCKED);
        }

        category.get().setDeleted(true);
        categoryDAO.save(category.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.PUT, path = "/restore/{categoryId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> restoreCategory(
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        final Optional<Category> category = categoryDAO.findDeletedById(categoryId);

        if (category.isPresent()) {
            category.get().setDeleted(false);
            categoryDAO.save(category.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteProductId/{productId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> deleteProductId(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId
    ) {
        List<Category> categories = this.categoryDAO.findByProductId(productId)
                .stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        if (!categories.isEmpty()) {

            for (Category category : categories) {
                category.getProductIds().removeIf(x -> x.equals(productId));
                categoryDAO.save(category);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.DELETE, path = "/restoreProductId/{productId}/fromCategory/{categoryId}")
    @RolesAllowed("category.write")
    public ResponseEntity<String> restoreProductId(
            @ApiParam(value = "product Id", required = true)
            @PathVariable("productId")
                    UUID productId,
            @ApiParam(value = "category Id", required = true)
            @PathVariable("categoryId")
                    UUID categoryId
    ) {
        Optional<Category> category = this.categoryDAO.findById(categoryId);
        if (!category.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        category.get().getProductIds().add(productId);
        categoryDAO.save(category.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
